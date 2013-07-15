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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.rest.ScannerResource;
import org.apache.accumulo.rest.data.IteratorResponse;

import com.google.inject.Inject;

/**
 * 
 */
public class ScannerResourceImpl implements ScannerResource {

  private final Connector connector;
  
  @Inject
  public ScannerResourceImpl(final Connector connector) {
    this.connector = connector;
  }

  @Override
  public IteratorResponse scan(String table, String auths, String range) {
    
    Scanner scanner = null;
    try {
      scanner = connector.createScanner(table, new Authorizations(auths));
    } catch (TableNotFoundException e) {
      e.printStackTrace();
    }
    
       
    return new IteratorResponse(scanner.iterator());
  }
  
}
