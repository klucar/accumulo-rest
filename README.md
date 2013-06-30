# Accumulo REST

Accumulo doesn't have a standard rest interface. This is an attempt to fix that. 
It tries to emulate the Accumulo Client API as close as possible.  

## How-To

Add the following properties to your `accumulo-site.xml` modifying where appropriate:

    <property>                                                                    
      <name>rest.username</name>                                                  
      <value>root</value>                                                         
    </property>                                                                   
    <property>                                                                    
      <name>rest.password</name>                                                  
      <value>secret</value>                                                       
    </property>                                                                   
    <property>                                                                    
      <name>rest.instance</name>                                                  
      <value>accumulo</value>                                                     
    </property>                                                                   
    <property>                                                                    
      <name>rest.token.type</name>                                                
      <value>org.apache.accumulo.core.client.security.tokens.PasswordToken</value>
    </property>                                                                           
    <property>                                                                    
      <name>rest-zookeepers</name>                                                
      <value>localhost:2181</value>                                               
    </property>                         
    <property>                                                                    
      <name>rest.proxy.accumulo</name>                                            
      <value>http://localhost:50095</value>                                       
    </property>                                                                   
    <property>                                                                    
      <name>rest.proxy.hdfs</name>                                                
      <value>http://localhost:50070/dfshealth.jsp</value>                         
    </property>                                                                   
    <property>                                                                    
      <name>rest.proxy.mapreduce</name>                                           
      <value>http://localhost:50030/jobtracker.jsp</value>                        
    </property>      

Fire up a local jetty to test things out

    mvn jetty:run

Point a browser to [http://localhost:8080/accumulo/](http://localhost:8080/accumulo)

To deploy to your existing evironment

    mvn package war:war    

will create `target/accumulo-rest.war`

## Tech Bits

* JAX-RS webapp that uses JBoss RESTEasy.
* Google Guice injects a singleton connector into the REST resources and inject necessary Accumulo configuration information to the connector (username, password, zookeepers, etc).
* Root path contains a Single Page Application (SPA) built on AngularJS to interact with the REST api.
* Everything built as if Accumulo adopts it, (e.g. package names are org.apache.accumulo)
* Provies a Proxy service for the standard Accumulo Monitor, Hadoop HDFS, and Hadoop MapReduce monitor pages.

## What's implemented

I'm starting with GET operations to figure out the base REST API.

* Properties/[{prefix}] - Properties defined in the 
* Admin/TableOperations
* Admin/TableOperations/list
* Admin/TableOperations/listSplits/{tablename}[?maxsplits=<int>]
* Admin/TableOperations/exists/{tablename}
* Admin/TableOperations/create/{tablename}[?limitversion=false/true,timetype=MILLIS/LOGICAL]
* Admin/TableOperations/offline/{tablename}
* Admin/TableOperations/online/{tablename}
* Admin/SecurityOperations -- placeholder for now
* Admin/InstanceOperations -- placeholder for now
* Proxy/Monitor
* Proxy/Mapreduce
* Proxy/Hdfs

* js_api/ - Automatically generated javascript API (thanks RESTEasy)

### License

 [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html)
