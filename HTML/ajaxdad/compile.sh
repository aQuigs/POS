#!/bin/bash

javac -cp WEB-INF/lib/driver.jar:WEB-INF/javax.jar:WEB-INF/classes WEB-INF/classes/MySQLUtilities.java WEB-INF/classes/loginVerification.java && jar -cvf loginVerification.war .
