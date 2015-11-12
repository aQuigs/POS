#!/bin/bash

javac -cp WEB-INF/lib/javax.mail.jar:WEB-INF/lib/driver.jar:WEB-INF/lib/commons-io.jar:WEB-INF/lib/commons-fileupload.jar:WEB-INF/javax.jar:WEB-INF/classes WEB-INF/classes/MySQLUtilities.java WEB-INF/classes/ServletUtilities.java WEB-INF/classes/LoginVerification.java WEB-INF/classes/Registration.java WEB-INF/classes/EmailSender.java WEB-INF/classes/ValidateEmail.java WEB-INF/classes/ResetPassword.java WEB-INF/classes/GetUnfinishedOrders.java WEB-INF/classes/ItemStarted.java WEB-INF/classes/ItemCooked.java WEB-INF/classes/OrderStarted.java WEB-INF/classes/OrderCooked.java WEB-INF/classes/GetRestaurantUsers.java WEB-INF/classes/DeleteRestaurantUser.java WEB-INF/classes/AddRestaurantUser.java WEB-INF/classes/ChangeRestaurantUser.java WEB-INF/classes/ListMenus.java WEB-INF/classes/GetMenuDetails.java WEB-INF/classes/AddMenu.java WEB-INF/classes/ChangeMenu.java WEB-INF/classes/DeleteMenu.java WEB-INF/classes/AddMenuItem.java WEB-INF/classes/DeleteMenuItem.java WEB-INF/classes/ChangeMenuItem.java WEB-INF/classes/ListRestaurants.java WEB-INF/classes/PlaceOrder.java WEB-INF/classes/PlaceOrder.java WEB-INF/classes/OrderUncooked.java WEB-INF/classes/FileUploadUtility.java WEB-INF/classes/GetTableLayout.java WEB-INF/classes/EditTableLayout.java WEB-INF/classes/ChangeFilledSeats.java && jar -cvf POS.war .

