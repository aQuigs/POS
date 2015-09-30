#!/bin/bash

javac -cp WEB-INF/lib/javax.mail.jar:WEB-INF/lib/driver.jar:WEB-INF/javax.jar:WEB-INF/classes WEB-INF/classes/MySQLUtilities.java WEB-INF/classes/ServletUtilities.java WEB-INF/classes/LoginVerification.java WEB-INF/classes/Registration.java WEB-INF/classes/EmailSender.java WEB-INF/classes/ValidateEmail.java WEB-INF/classes/ResetPassword.java WEB-INF/classes/GetUnfinishedOrders.java WEB-INF/classes/ItemStarted.java WEB-INF/classes/ItemCooked.java && jar -cvf POS.war .


