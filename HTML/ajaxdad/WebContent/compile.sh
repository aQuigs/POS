#!/bin/bash

javac -cp WEB-INF/lib/javax.mail.jar:WEB-INF/lib/driver.jar:WEB-INF/javax.jar:WEB-INF/classes WEB-INF/classes/MySQLUtilities.java WEB-INF/classes/loginVerification.java WEB-INF/classes/registration.java WEB-INF/classes/EmailSender.java && jar -cvf loginVerification.war .