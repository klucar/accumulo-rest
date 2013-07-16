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
package org.apache.accumulo.rest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.minicluster.MiniAccumuloCluster;
import org.apache.accumulo.minicluster.MiniAccumuloConfig;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.fail;

public abstract class ResourceTest {

  protected static Connector connector = null;
  private static MiniAccumuloCluster cluster = null;
  private static MiniAccumuloConfig config = null; 
  private static Instance instance = null;
  
  @BeforeClass
  public static void setUpBefore() {
    // setup a mock accumulo instance
    if( null != connector ){
      // shouldn't reach here, but who knows what Maven is capable of.
      return;
    }
    File tmpDir = new File(FileUtils.getTempDirectory(), "macc-" + UUID.randomUUID().toString());
    
    try {
      config = new MiniAccumuloConfig(tmpDir, "secret");
      cluster = new MiniAccumuloCluster(config);
      cluster.start();
      instance = new ZooKeeperInstance(cluster.getInstanceName(),cluster.getZooKeepers());
    } catch (IOException e) {
      fail("IOException when Creating MiniAccumuloCluster: "+e.getMessage());
    } catch (InterruptedException e) {
      fail("InterruptedException when starting MiniAccumuloCluster: "+e.getMessage());
    }
    
  }

  public static Connector getConnector(){
    Connector connector = null;
    try {
      connector = instance.getConnector("root", new PasswordToken(config.getRootPassword()));
    } catch (AccumuloException e) {
      fail("Could not get connector from MiniAccumuloCluster: "+e.getMessage());
    } catch (AccumuloSecurityException e) {
      fail("MiniAccumuloConfig somehow lost the root password: "+e.getMessage());
    }
    return connector;
  }
  
  @AfterClass
  public static void tearDownAfter() {
    try {
      cluster.stop();
      //TODO delete temp directory? only if tests passed?
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  
}
