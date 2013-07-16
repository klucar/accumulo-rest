# Accumulo REST

Accumulo doesn't have a standard rest interface. This is an attempt to fix that. 
It tries to emulate the Accumulo Client API as close as possible. Hopefully it 
will at least trigger a REST design discussion for Accumulo.

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

Start Accumulo, Clone the repo, fire up a local jetty to test things out

    mvn jetty:run

Point a browser to [http://localhost:8080/accumulo/](http://localhost:8080/accumulo)

To deploy to your existing evironment

    mvn package war:war    

will create `target/accumulo-rest.war`

## Caveats

* I've only tested this on my local machine, never in a multi-server environment. Although my machine is Bo$$, I'm sure I screwed something up.
* Absolutely no http security is built in. none. yet. So don't go deploying this.

## Tech Bits

* JAX-RS webapp that uses JBoss RESTEasy.
* Google Guice injects a singleton connector into the REST resources, handling necessary Accumulo configuration information (username, password, zookeepers, etc).
* Root path contains a Single Page Application (SPA) built on AngularJS to interact with the REST api.
* Everything built as if Accumulo adopts it, (e.g. package names are org.apache.accumulo)
* Provies a Proxy service for the standard Accumulo Monitor, Hadoop HDFS, and Hadoop MapReduce monitor pages.

## What's implemented

I'm starting with GET operations to figure out the base REST API.

* Properties/[{prefix}] - Properties defined in accumulo configuration.
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
* Scanner/scan[?table=<string>,
* js_api/ - Automatically generated javascript API (thanks RESTEasy)

## TODO

* Flesh out Remainder of REST API.
* Javascript test framework
* Implement server tests -- start embedded Jetty and use HttpClient code?
* Charting magic for new Monitor
* Get Proxy links re-routed correctly
* REST documentation - [Swagger?](https://developers.helloreverb.com/swagger/) or something like it?
* Determine Shell route - steal current Monitor like is stubbed or pure javascript version
* HDFS Javascript Shell? - seperate project?
* Breakout parts into separate projects?
* Determine a security method. (OAuth2?)
* Are base64 scan results ok?
* Put this list into GitHub

### License

 [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html)
