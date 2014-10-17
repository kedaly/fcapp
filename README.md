Flight Center Demo / POC App
============================

Modules
=======
core - is the core business logic
command - is the command prompt
data - data files
rest - rest api server

Build
=====
This project is built to be compiled with Maven 3.x and above and Java 1.7+

The project is built with this command
    
    mvn clean install
    
Command Module
==============
The jar file in the target directory is the executable to invoke you must have the data files in the same directory as the jar file or set the FLIGHT_DATA environment file to point to the directory.

To Run
  
  	java -jar <jarfile> -o <orgin> -d <destination>
  	
Rest Module
===========
The jar file in the target directory is the executable to invoke you must have the data files in the same directory as the jar file or set the FLIGHT_DATA environment file to point to the directory.

To Run 
   
   java -jar <jarfile> 

Open a browser and this url will access the api

http://localhost:8080/searchFlights/{orig}/{destination}?format={text | json}

NOTES: The json format is for demo only as it simply serializes the List of return items.

You can also ssh into the service as well ssh -p 2000 user@localhost (on a linux workstation), the password is logged at the start of the process.
 