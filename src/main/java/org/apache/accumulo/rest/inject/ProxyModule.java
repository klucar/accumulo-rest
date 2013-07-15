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
package org.apache.accumulo.rest.inject;

import java.util.HashMap;
import java.util.Properties;

import org.apache.accumulo.rest.impl.ProxyResourceImpl;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class ProxyModule implements Module {
  
  public static final String PROXY_ACCUMULO = "rest.proxy.accumulo";
  public static final String PROXY_HDFS = "rest.proxy.hdfs";
  public static final String PROXY_MAPREDUCE = "rest.proxy.mapreduce";
  
  public void configure(final Binder binder) {
    Names.bindProperties(binder, loadProperties());
    binder.bind(ProxyMap.class).toProvider(ProxyProvider.class).in(Scopes.SINGLETON);
    
    // For now, all Resource classes that require injection must be added here.
    binder.bind(ProxyResourceImpl.class);
  }
  
  static class ProxyProvider implements Provider<ProxyMap> {
    
    private final String accumuloURL;
    private final String hdfsURL;
    private final String mapreduceURL;
    
    @Inject
    public ProxyProvider(@Named(PROXY_ACCUMULO) final String accumuloURL, 
        @Named(PROXY_HDFS) final String hdfsURL,
        @Named(PROXY_MAPREDUCE) final String mapreduceURL) {
      this.accumuloURL = accumuloURL;
      this.hdfsURL = hdfsURL;
      this.mapreduceURL = mapreduceURL;
    }
    
    public ProxyMap get() {

      ProxyMap pMap = new ProxyMap();

      pMap.put(PROXY_ACCUMULO, accumuloURL);
      pMap.put(PROXY_HDFS, hdfsURL);
      pMap.put(PROXY_MAPREDUCE, mapreduceURL);
      
      return pMap;
    }
    
  }
  
  public static class ProxyMap extends HashMap<String,String> {
    public String getURL(String proxyName){
      return this.get(proxyName);
    }
  }
  
  /*
   * Load properties required for injection
   */
  private Properties loadProperties() {
    Properties props = new Properties();

    // TODO read from accumulo-site.xml
    System.getenv("ACCUMULO_HOME");
    
    props.put(PROXY_ACCUMULO, "http://localhost:50095");
    props.put(PROXY_HDFS, "http://localhost:50070/dfshealth.jsp");
    props.put(PROXY_MAPREDUCE, "http://localhost:50030/jobtracker.jsp");
    
    return props;
  }
  
}
