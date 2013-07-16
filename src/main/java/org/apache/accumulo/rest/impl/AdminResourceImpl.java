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
import java.util.Collection;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.client.admin.TimeType;
import org.apache.accumulo.rest.AdminResource;
import org.apache.accumulo.rest.data.InstanceData;
import org.apache.accumulo.rest.data.Split;
import org.apache.accumulo.rest.data.Table;
import org.apache.hadoop.io.Text;

import com.google.inject.Inject;

/**
 * 
 */
public class AdminResourceImpl implements AdminResource {
  
  private final Connector connector;
  
  @Inject
  public AdminResourceImpl(final Connector connector) {
    this.connector = connector;
  }
  
  public List<Table> getTableList() {
    // TODO not sure if I need to keep instantiating these
    TableOperations tops = connector.tableOperations();
    
    List<Table> tables = new ArrayList<Table>();
    for (String name : tops.list()) {
      tables.add(new Table(name));
    }
    return tables;
  }
  
  public List<Split> getTableSplits(String tablename, int maxsplits) {
    // TODO not sure if I need to keep instantiating these
    TableOperations tops = connector.tableOperations();
    
    Collection<Text> splits = null;
    try {
      if (maxsplits <= 0) {
        splits = tops.listSplits(tablename);
      } else {
        splits = tops.listSplits(tablename, maxsplits);
      }
    } catch (TableNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (AccumuloSecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (AccumuloException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    List<Split> splitList = new ArrayList<Split>();
    for (Text split : splits) {
      splitList.add(new Split(split.toString()));
    }
    return splitList;
  }
  
  public Table getTableExists(String tablename) {
    TableOperations tops = connector.tableOperations();
    
    boolean exists = tops.exists(tablename);
    
    return new Table(tablename, exists);
  }
  
  public Table getTableCreate(String tablename, String limitversion, String timetype) {
    TableOperations tops = connector.tableOperations();
    
    try {
      
      tops.create(tablename, Boolean.parseBoolean(limitversion), TimeType.valueOf(timetype));
      
    } catch (AccumuloException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (AccumuloSecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TableExistsException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    boolean exists = tops.exists(tablename);
    
    return new Table(tablename, exists);
  }
  
  public Table getTableOffline(String tablename) {
    TableOperations tops = connector.tableOperations();
    
    try {
      
      tops.offline(tablename);
      
    } catch (AccumuloException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (AccumuloSecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TableNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    boolean exists = tops.exists(tablename);
    
    return new Table(tablename, exists, true);
  }
  
  public Table getTableOnline(String tablename) {
    TableOperations tops = connector.tableOperations();
    
    try {
      tops.online(tablename);
    } catch (AccumuloException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (AccumuloSecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TableNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    boolean exists = tops.exists(tablename);
    
    return new Table(tablename, exists, false);
  }
  
  public InstanceData getInstanceData() {
    Instance inst = connector.getInstance();
    String instName = inst.getInstanceName();
    String instID = inst.getInstanceID();
    
    return new InstanceData(instName, instID);
  }
  
  public String getInstanceOperations() {
    
    return "Instance Operations";
  }
  
  public String getSecurityOperations() {
    
    return "Security Operations";
  }
  
}
