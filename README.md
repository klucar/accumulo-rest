# Accumulo REST

Accumulo doesn't have a standard rest interface. This is an attempt to fix that. 
It tries to emulate the Accumulo Client API as close as possible.  

## How-To

    git clone ....

    mvn jetty:run

Point a browser to [http://localhost:8080/accumulo-rest/](http://localhost:8080/accumulo-rest)

To deploy to your existing evironment

    mvn package war:war    

will create `target/accumulo-rest.war`

## Tech Bits

* JAX-RS webapp that uses JBoss RESTEasy.
* Google Guice injects a singleton connector into the REST resources and inject necessary Accumulo configuration information to the connector (username, password, zookeepers, etc).
* Root path contains a Single Page Application (SPA) built on AngularJS to interact with the REST api.
* Everything built as if Accumulo adopts it, (e.g. package names are org.apache.accumulo)

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

* js_api/ - Automatically generated javascript API (thanks RESTEasy)


