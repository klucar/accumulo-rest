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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


import org.apache.accumulo.rest.inject.ProxyModule;
import org.apache.accumulo.rest.inject.ProxyModule.ProxyMap;
import org.apache.accumulo.rest.ProxyResource;

import com.google.inject.Inject;

/**
 * 
 */
public class ProxyResourceImpl implements ProxyResource {
  
  private final ProxyMap proxies;
  private final ResteasyClient client;

  @Inject
  public ProxyResourceImpl(final ProxyMap proxies) {
    this.proxies=proxies;
    this.client =  new ResteasyClientBuilder().build();
  }
  
  private String getProxy(String proxyKey){
    ResteasyWebTarget target = client.target(proxies.get(proxyKey));
    Response response = target.request().get();
    String value = response.readEntity(String.class);
    response.close();
    
    return value;
  }
  
  public String getMonitor() {

    return getProxy(ProxyModule.PROXY_ACCUMULO);
  }

  public String getMapReduce() {

    return getProxy(ProxyModule.PROXY_MAPREDUCE);
  }
  
  public String getHdfs() {

    return getProxy(ProxyModule.PROXY_HDFS);
  }
  
}
