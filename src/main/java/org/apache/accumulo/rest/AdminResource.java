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

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.accumulo.rest.data.InstanceData;
import org.apache.accumulo.rest.data.Split;
import org.apache.accumulo.rest.data.Table;

/**
 * 
 */
@Path("Admin")
public interface AdminResource {
  
  @Path("/TableOperations/list")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public List<Table> getTableList();
  
  @Path("/TableOperations/listSplits/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public List<Split> getTableSplits(@PathParam("tablename") String tablename,
                                     @DefaultValue("-1")@QueryParam("maxsplits") int maxsplits);    

  @Path("/TableOperations/exists/{tablename}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableExists(@PathParam("tablename") String tablename);
  
  @Path("/TableOperations/create/{tablename}")
  @PUT
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableCreate(@PathParam("tablename") String tablename,
                              @DefaultValue("true") @QueryParam("limitversion") String limitversion,
                              @DefaultValue("MILLIS")@QueryParam("timetype") String timetype);
  @Path("/TableOperations/offline/{tablename}")
  @PUT
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableOffline(@PathParam("tablename") String tablename);
  
  @Path("/TableOperations/online/{tablename}")
  @PUT
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Table getTableOnline(@PathParam("tablename") String tablename);
  
  @Path("/InstanceOperations")
  @GET
  // @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.TEXT_PLAIN)
  public String getInstanceOperations();

  // I don't think this is an InstanceOperations thing, but seems to fit here for now
  @Path("/InstanceOperations/instanceData")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public InstanceData getInstanceData();
  
  @Path("/SecurityOperations")
  @GET
  // @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.TEXT_PLAIN)
  public String getSecurityOperations();
  
}
