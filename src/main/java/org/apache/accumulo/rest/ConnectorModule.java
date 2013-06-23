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

import java.util.Properties;

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
 * Guice Module to support dependency injection of Accumulo parameters required to create a singleton Connector object for use by the various Resources.
 */
public class ConnectorModule implements Module {
  
  public static final String REST_INSTANCE = "accumulo.rest.instance";
  public static final String REST_USERNAME = "accumulo.rest.username";
  public static final String REST_PASSWORD = "accumulo.rest.password";
  public static final String REST_ZOOKEEPERS = "accumulo.rest.zookeepers";
  
  public void configure(final Binder binder) {
    Names.bindProperties(binder, loadProperties());
    binder.bind(Connector.class).toProvider(ConnectorProvider.class).in(Scopes.SINGLETON);
    binder.bind(AccumuloResource.class);
  }
  
  static class ConnectorProvider implements Provider<Connector> {
    
    private final String instanceName;
    private final String zookeepers;
    private final String username;
    private final String password;
    
    @Inject
    public ConnectorProvider(@Named(REST_INSTANCE) final String instanceName, @Named(REST_ZOOKEEPERS) final String zookeepers,
        @Named(REST_USERNAME) final String username, @Named(REST_PASSWORD) final String password) {
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
        throw new RuntimeException("Accumulo internal exception: ", e);
      } catch (AccumuloSecurityException e) {
        throw new RuntimeException("Accumulo security exception: ", e);
      }
      return conn;
    }
    
  }
  
  /*
   * Load properties required for injection
   */
  private Properties loadProperties() {
    // TODO Load properties from appropriate place...
    Properties props = new Properties();
    
    props.put(REST_USERNAME, "root");
    props.put(REST_PASSWORD, "secret");
    props.put(REST_INSTANCE, "accumulo");
    props.put(REST_ZOOKEEPERS, "localhost:2181");
    
    return props;
  }
  
}
