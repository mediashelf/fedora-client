![MediaShelf fedora-client logo](http://mediashelf.github.com/fedora-client/images/fedora-client.png)

MediaShelf fedora-client
========================

A developer-friendly Java client for the Fedora Commons Repository REST API.

Create a new Fedora object as easily as:

    ingest("test:pid").execute(client);
    
Or, to add a datastream:
    
    File f = new File("/tmp/Hyperballad.mp3");
    addDatastream("test:pid", "DS1").controlGroup("M").content(f).execute(client);
                     
Optional properties are just that: optional. And when you do need one, it's 
clearly labeled rather than lost in an opaque series of null arguments. 
Compare:

    addDatastream("test:pid", "DS2").controlGroup("M")
      .dsLocation("http://localhost:8080/fedora/get/fedora-system:ContentModel-3.0/DC")
      .mimeType("text/xml").execute(client);
    
with something like the following:
    
    apim.addDatastream("test:pid", "DS3", null, null, true, "text/xml", null, 
    "http://localhost:8080/fedora/get/fedora-system:ContentModel-3.0/DC", "M", 
    "A", null, null, null);


Requirements
------------

* Fedora 3.5 (earlier 3.x versions should work as well, minus some features)
* Java 6

Installation
------------

fedora-client is now available via [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.yourmediashelf.fedora.client%22).
As of version 0.1.8, fedora-client is provided as two artifacts:

* fedora-client-core, which supports Fedora's REST API and riSearch
* fedora-client-messaging, which supports Fedora's Messaging

Both artifacts are also available as *-with-dependencies* jars, which bundle their respective
dependencies in a single jar for use with non-Maven projects.

Building from source
--------------------

1. Download the source, e.g.

        git clone git://github.com/mediashelf/fedora-client.git

2. Build the project

        cd fedora-client
        mvn install
        
    fedora-client ships with a number of integration tests that require a 
    running instance of Fedora that has the ResourceIndex module enabled. 
    Set the testing properties (e.g. username, password) in pom.xml, or to 
    skip the integration tests entirely:
   
        mvn install -DskipTests
        

License & Copyright
-------------------

fedora-client is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

fedora-client is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.

Copyright &copy; 2010-2011 MediaShelf
