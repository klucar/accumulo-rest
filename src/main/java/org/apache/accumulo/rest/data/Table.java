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
package org.apache.accumulo.rest.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Accumulo Table result type
 * 
 */
@XmlRootElement(name = "table")
public class Table {
  
  private String name;
  private String exists = null;
  private String offline = null;
  
  public Table() {}
  
  public Table(String name) {
    this.name = name;
  }

  public Table(String name, boolean exists) {
    this.name = name;
    this.setExists(exists);
  }

  public Table(String name, boolean exists, boolean offline) {
    this.name = name;
    this.setExists(exists);
    this.setOffline(offline);
  }

  @XmlElement
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  @XmlElement(required=false)
  public String getExists() {
    return exists;
  }

  public void setExists(boolean exists) {
    this.exists = Boolean.toString(exists);
  }

  @XmlElement(required=false)
  public String getOffline(){
    return offline;
  }
  
  private void setOffline(boolean offline) {
    this.offline = Boolean.toString(offline);
  }

  
}
