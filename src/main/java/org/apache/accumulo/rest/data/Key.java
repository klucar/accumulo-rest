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
 * Accumulo Split result type
 * 
 */
@XmlRootElement(name = "key")
public class Key {
  
  private String row, cf, cq, cv;
  private boolean deleted;
  private long timestamp;
  
  public Key() {}
  
  public Key(org.apache.accumulo.core.data.Key key) {
    //TODO interpret key parts as other than string (BASE64 everything?)
    this.setRow(key.getRow().toString());
    this.setCf(key.getColumnFamily().toString());
    this.setCq(key.getColumnQualifier().toString());
    this.setCv(key.getColumnVisibility().toString());
    this.setDeleted(key.isDeleted());
    this.setTimestamp(key.getTimestamp());
  }

  /**
   * @return the row
   */
  @XmlElement
  public String getRow() {
    return row;
  }

  /**
   * @param row the row to set
   */
  public void setRow(String row) {
    this.row = row;
    
  }

  /**
   * @return the cf
   */
  @XmlElement
  public String getCf() {
    return cf;
  }

  /**
   * @param cf the cf to set
   */
  public void setCf(String cf) {
    this.cf = cf;
    
  }

  /**
   * @return the cq
   */
  @XmlElement
  public String getCq() {
    return cq;
  }

  /**
   * @param cq the cq to set
   */
  public void setCq(String cq) {
    this.cq = cq;
    
  }

  /**
   * @return the cv
   */
  @XmlElement
  public String getCv() {
    return cv;
  }

  /**
   * @param cv the cv to set
   */
  public void setCv(String cv) {
    this.cv = cv;
    
  }

  /**
   * @return the deleted
   */
  @XmlElement
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * @param deleted the deleted to set
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
    
  }

  /**
   * @return the timestamp
   */
  @XmlElement
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
    
  }


}
