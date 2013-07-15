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
package org.apache.accumulo.rest.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

/**
 * 
 */
public class IteratorResponse<T> implements StreamingOutput {

  private Iterator<T> iter;
  
  public IteratorResponse(Iterator<T> iter){
    this.iter = iter;
  }
  
  @Override
  public void write(OutputStream output) throws IOException, WebApplicationException {

    while(iter.hasNext()){
      // TODO, proper JAXB marshalling here
      output.write(iter.next().toString().getBytes());
      output.write("\n".getBytes());
      output.flush();
    }
    output.close();
  }
  
}
