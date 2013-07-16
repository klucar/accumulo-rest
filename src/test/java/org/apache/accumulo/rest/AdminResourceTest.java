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

import static org.junit.Assert.*;

import java.util.List;

import org.apache.accumulo.rest.data.Table;
import org.apache.accumulo.rest.impl.AdminResourceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class AdminResourceTest extends ResourceTest {
  
  private static AdminResource resource;
  
  @Before
  public void setUp() {
    // grab a connector and create a resource
    resource = new AdminResourceImpl(ResourceTest.getConnector());
  }
  
  @After
  public void tearDown() {
    
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableList()}.
   */
  @Test
  public void testGetTableList() {
    boolean foundMetadata = false;
    
    List<Table> tables = resource.getTableList();
    for( Table table : tables){
      if( table.getName().equals("!METADATA") ){
        foundMetadata = true;
      }
    }
    // TODO this method could be avoided if Table, etc. implemented equals(Object o)
    assertTrue( foundMetadata );
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableSplits(java.lang.String, int)}.
   */
  @Test
  public void testGetTableSplits() {
    // TODO implement
    // fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableExists(java.lang.String)}.
   */
  @Test
  public void testGetTableExists() {
    // TODO implement
    //fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableCreate(java.lang.String, java.lang.String, java.lang.String)}.
   */
  @Test
  public void testGetTableCreate() {
    // TODO implement
    //fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableOffline(java.lang.String)}.
   */
  @Test
  public void testGetTableOffline() {
    // TODO implement
    //fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getTableOnline(java.lang.String)}.
   */
  @Test
  public void testGetTableOnline() {
    // TODO implement
    // fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getInstanceOperations()}.
   */
  @Test
  public void testGetInstanceOperations() {
    // TODO implement
    // fail("Not yet implemented");
  }
  
  /**
   * Test method for {@link org.apache.accumulo.rest.impl.AdminResourceImpl#getSecurityOperations()}.
   */
  @Test
  public void testGetSecurityOperations() {
    // TODO implement
    // fail("Not yet implemented");
  }
  
}
