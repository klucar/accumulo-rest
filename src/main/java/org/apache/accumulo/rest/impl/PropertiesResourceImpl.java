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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.conf.AccumuloConfiguration;
import org.apache.accumulo.rest.PropertiesResource;
import org.apache.accumulo.rest.data.Property;

import com.google.inject.Inject;

/**
 * 
 */
public class PropertiesResourceImpl implements PropertiesResource {

  private final Connector connector;
  
  @Inject
  public PropertiesResourceImpl(final Connector connector) {
    this.connector = connector;
  }
  
  public List<Property> getProperties(String category) {
    
    AccumuloConfiguration config = connector.getInstance().getConfiguration();
    
    List<Property> properties = new ArrayList<Property>();
    
    for( Iterator<Entry<String,String>> iter = config.iterator(); iter.hasNext(); ){
      Entry<String,String> entry = iter.next();
      String key = entry.getKey();
      String value = entry.getValue();
      if( key.startsWith(category) || category.equals("all")){
        // mask any property that contains the word password
        if( key.contains("password")){
          value = "********";
        }
        properties.add(new Property(key, value));
      }
    }
    return properties; //new PropertyListing(properties);
  }
  
  public List<Property> getProperties() {
    return getProperties("all");
  }
  
}
