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

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Guice Module to support dependency injection of Accumulo parameters
 * required to create a singleton Connector object for use by 
 * the various Resources.
 */
public class ConnectorModule implements Module {
  
  public void configure(final Binder binder) {
    //TODO bind to a properties file or Accumulo configuration or something like that.
    // bindProperty() is also available for Properties file binidings
    binder.bindConstant().annotatedWith(Names.named("instanceName")).to("accumulo");
    binder.bindConstant().annotatedWith(Names.named("zookeepers")).to("localhost:2181");
    binder.bindConstant().annotatedWith(Names.named("username")).to("root");
    binder.bindConstant().annotatedWith(Names.named("password")).to("secret");
    binder.bind(Connector.class).toProvider(ConnectorProvider.class).in(Scopes.SINGLETON);
    binder.bind(AccumuloResource.class);
  }
  
  static class ConnectorProvider implements Provider<Connector> {
    
    private final String instanceName;
    private final String zookeepers;
    private final String username;
    private final String password;
    
    @Inject
    public ConnectorProvider(@Named("instanceName") final String instanceName, @Named("zookeepers") final String zookeepers,
        @Named("username") final String username, @Named("password") final String password) {
      this.instanceName = instanceName;
      this.zookeepers = zookeepers;
      this.username = username;
      this.password = password;
    }
    
    public Connector get() {
      Instance inst = new ZooKeeperInstance(this.instanceName, zookeepers);
      PasswordToken token = new PasswordToken(this.password);
      Connector conn = null;
      try {
        conn = inst.getConnector(this.username, token);
      } catch (AccumuloException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (AccumuloSecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return conn;
    }
    
  }
  
}
