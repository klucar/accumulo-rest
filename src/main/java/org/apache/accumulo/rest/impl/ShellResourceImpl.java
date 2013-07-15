/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.rest.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jline.ConsoleReader;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.util.shell.Shell;
import org.apache.accumulo.rest.ShellResource;
import org.apache.accumulo.rest.data.ShellResponse;

import com.google.inject.Inject;

/**
 * 
 */
public class ShellResourceImpl implements ShellResource {

  private Map<String,ShellExecutionThread> userShells = new HashMap<String,ShellExecutionThread>();
  private ExecutorService service = Executors.newCachedThreadPool();

  @Inject
  public ShellResourceImpl(final Connector connector) {
  }
  
  @Override
  public ShellResponse execShellCommand(String command, String uuid) {

    if( !userShells.containsKey(uuid)){
      try {
        ShellExecutionThread shellThread = new ShellExecutionThread("mockuser", "mockpassword", "mockinstance");
        service.submit(shellThread);
        userShells.put(uuid, shellThread);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        // authentication handling here.
        e.printStackTrace();
        return new ShellResponse("something bad this way went");
      }
    }

    ShellExecutionThread shellThread = userShells.get(uuid);
    if (command == null) {
      // the command is null, just print prompt
      return new ShellResponse(shellThread.getPrompt());
    }
    shellThread.addInputString(command);
    shellThread.waitUntilReady();
    if (shellThread.isDone()) {
      // the command was exit, invalidate session
      userShells.remove(uuid);
      return null;
    }
    // get the shell's output
    StringBuilder sb = new StringBuilder();
    sb.append(shellThread.getOutput().replace("<", "&lt;").replace(">", "&gt;"));
    if (sb.length() == 0 || !(sb.charAt(sb.length() - 1) == '\n')){
      sb.append("\n");
    }
    // check if shell is waiting for input
    if (!shellThread.isWaitingForInput()){
      sb.append(shellThread.getPrompt());
    }
    // check if shell is waiting for password input
    if (shellThread.isMasking()){
      sb.append("*");
    }
    
    return new ShellResponse(sb.toString());
  }
 
  
  
  // Blatently stolen from Accumulo Shell Servlet, but everything there was private
  private static class StringBuilderOutputStream extends OutputStream {
    StringBuilder sb = new StringBuilder();
    
    @Override
    public void write(int b) throws IOException {
      sb.append((char) (0xff & b));
    }
    
    public String get() {
      return sb.toString();
    }
    
    public void clear() {
      sb.setLength(0);
    }
    
  }
  
  private static class ShellExecutionThread extends InputStream implements Runnable {
    private Shell shell;
    StringBuilderOutputStream output;
    private String cmd;
    private int cmdIndex;
    private boolean done;
    private boolean readWait;
    
    private ShellExecutionThread(String username, String password, String mock) throws IOException {
      this.done = false;
      this.cmd = null;
      this.cmdIndex = 0;
      this.readWait = false;
      this.output = new StringBuilderOutputStream();
      ConsoleReader reader = new ConsoleReader(this, new OutputStreamWriter(output));
      this.shell = new Shell(reader, new PrintWriter(output));
      shell.setLogErrorsToConsole();
      if (mock != null) {
        if (shell.config("--fake", "-u", username, "-p", password))
          throw new IOException("mock shell config error");
      } else if (shell.config("-u", username, "-p", password)) {
        throw new IOException("shell config error");
      }
    }
    
    @Override
    public synchronized int read() throws IOException {
      if (cmd == null) {
        readWait = true;
        this.notifyAll();
      }
      while (cmd == null) {
        try {
          this.wait();
        } catch (InterruptedException e) {}
      }
      readWait = false;
      int c;
      if (cmdIndex == cmd.length())
        c = '\n';
      else
        c = cmd.charAt(cmdIndex);
      cmdIndex++;
      if (cmdIndex > cmd.length()) {
        cmd = null;
        cmdIndex = 0;
        this.notifyAll();
      }
      return c;
    }
    
    @Override
    public synchronized void run() {
      Thread.currentThread().setName("shell thread");
      while (!shell.hasExited()) {
        while (cmd == null) {
          try {
            this.wait();
          } catch (InterruptedException e) {}
        }
        String tcmd = cmd;
        cmd = null;
        cmdIndex = 0;
        try {
          shell.execCommand(tcmd, false, true);
        } catch (IOException e) {}
        this.notifyAll();
      }
      done = true;
      this.notifyAll();
    }
    
    public synchronized void addInputString(String s) {
      if (done)
        throw new IllegalStateException("adding string to exited shell");
      if (cmd == null) {
        cmd = s;
      } else {
        throw new IllegalStateException("adding string to shell not waiting for input");
      }
      this.notifyAll();
    }
    
    public synchronized void waitUntilReady() {
      while (cmd != null) {
        try {
          this.wait();
        } catch (InterruptedException e) {}
      }
    }
    
    public synchronized String getOutput() {
      String s = output.get();
      output.clear();
      return s;
    }
    
    public String getPrompt() {
      return shell.getDefaultPrompt();
    }
    
    public void printInfo() throws IOException {
      shell.printInfo();
    }
    
    public boolean isMasking() {
      return shell.isMasking();
    }
    
    public synchronized boolean isWaitingForInput() {
      return readWait;
    }
    
    public boolean isDone() {
      return done;
    }
  }
  
}
