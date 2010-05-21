![MediaShelf logo](http://www.yourmediashelf.com/images/Logo-April2008.jpg)

MediaShelf FedoraClient
=======================

A developer-friendly Java client for the Fedora Commons Repository REST API.

Create a new Fedora object as easily as:

    fedora.execute(ingest("test:pid").build());
    
Or, to add a datastream:
    
    File f = new File("/tmp/Hyperballad.mp3");
    fedora.execute(addDatastream("test:pid", "DS1")
                     .controlGroup("M").content(f).build());
                     
Optional properties are just that: optional. And when you do need one, it's 
at your fingertips rather than lost in an opaque series of null arguments. 
Compare:

    fedora.execute(addDatastream("test:pid", "DS2").controlGroup("M")
      .dsLocation("http://localhost:8080/fedora/get/fedora-system:ContentModel-3.0/DC")
      .mimeType("text/xml").build());
    
with something like the following:
    
    apim.addDatastream("test:pid", "DS3", null, null, true, "text/xml", null, 
    "http://localhost:8080/fedora/get/fedora-system:ContentModel-3.0/DC", "M", 
    "A", null, null, null);


Requirements
------------

* Fedora 3.3
* Java 6

Installation
------------

1. Download the source, e.g.

        git clone git://github.com/mediashelf/fedora-client.git

2. Build the project

        cd fedora-client
        mvn install
        
    fedora-client ships with a number of integration tests that require a 
    running instance of Fedora. Set the testing properties (e.g. username, 
    password) in pom.xml, or to skip the tests:
   
        mvn install -DskipTests
        

License & Copyright
-------------------

FedoraClient is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FedoraClient is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with FedoraClient.  If not, see <http://www.gnu.org/licenses/>.

Copyright &copy; 2010 MediaShelf
