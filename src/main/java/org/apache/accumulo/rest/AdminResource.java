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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.client.admin.TimeType;
import org.apache.accumulo.rest.data.Split;
import org.apache.accumulo.rest.data.SplitListing;
import org.apache.accumulo.rest.data.Table;
import org.apache.accumulo.rest.data.TableListing;
import org.apache.hadoop.io.Text;

import com.google.inject.Inject;

/**
 * 
 */
@Path("Admin")
public class AdminResource {
  
  private final Connector connector;
  
  @Inject
  public AdminResource(final Connector connector) {
    this.connector = connector;
  }
  
  @Path("/TableOperations/list")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public TableListing getTableList() {
    // TODO not sure if I need to keep instantiating these
    TableOperations tops = connector.tableOperations();
    
    List<Table> tables = new ArrayList<Table>();
    for(String name : tops.list()){
      tables.add( new Table(name));
    }
    return new TableListing(tables);
  }

  @Path("/TableOperations/listSplits/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public SplitListing getTableSplits(@PathParam("tablename") String tablename,
                                     @DefaultValue("-1")@QueryParam("maxsplits") int maxsplits) {
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
    for( Text split : splits ){
      splitList.add( new Split( split.toString() ));
    }
    return new SplitListing(splitList);
  }

  @Path("/TableOperations/exists/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableExists(@PathParam("tablename") String tablename) {
    TableOperations tops = connector.tableOperations();

    boolean exists = tops.exists(tablename);
    
    return new Table(tablename, exists);
  }

  @Path("/TableOperations/create/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableCreate(@PathParam("tablename") String tablename,
                              @DefaultValue("true") @QueryParam("limitversion") String limitversion,
                              @DefaultValue("MILLIS")@QueryParam("timetype") String timetype) {
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

  @Path("/TableOperations/offline/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableOffline(@PathParam("tablename") String tablename) {
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

  @Path("/TableOperations/online/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableOnline(@PathParam("tablename") String tablename) {
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
  
  @Path("/InstanceOperations")
  @GET
  // @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.TEXT_PLAIN)
  public String getInstanceOperations() {
    
    return "Instance Operations";
  }

  @Path("/SecurityOperations")
  @GET
  // @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.TEXT_PLAIN)
  public String getSecurityOperations() {
    
    return "Security Operations";
  }
  
}
