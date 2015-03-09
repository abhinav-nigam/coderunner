# spring-boot-soap
The document lists the running instructions for CodeRunner. The instructions have commands for Ubuntu, for other platforms follow the same instructions but the commands will differ.

Prequisites
      1.  Java 8 is required. It can be installed on ubuntu as:
            sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
            sudo apt-get install oracle-java8-installer
      2.  Maven is required. It can be installed on ubuntu as:
	sudo apt-get install maven

Steps to run
      1.   Extract the submitted zip and move to code directory.   
      2.   Run mvn clean install inside the directory. This command might take some time as maven downloads all the project dependencies.   
      3. Run logserver using the log4j-server.xml file. The command for this is:
         java -cp <path to log4j.jar> org.apache.log4j.net.SimpleSocketServer <port>  src/main/resources/log4j-server.properties
	 On Ubuntu, this will look like:
         java -cp  ~/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar org.apache.log4j.net.SimpleSocketServer 4712 src/main/resources/log4j-server.properties   
      4.   Run mvn spring-boot:run to start the application. You might see some ASCII art on the console. You can also check the logs in log/coderunner-info.log.
      5.   Open a browser and go to page http://localhost:8080. You should find the coderunner application.

Troubleshooting:
Maven picks up java from the environment variable JAVA_HOME. If you get version mismatch errors, this could be the reason. Try setting JAVA_HOME to Java 8. Example: export JAVA_HOME=/usr/lib/jvm/java-8-oracle
spring-boot requires Maven 3, make sure you have the correct maven version by checking mvn -version

