DROP PROCEDURE DeleteRestaurantUser;
DELIMITER //
CREATE PROCEDURE DeleteRestaurantUser(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IUsernameToDelete VARCHAR(64), OUT OReturnCode int(3))

delete_restaurant_user:BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'admin')
    THEN
    SELECT restaurantId INTO VRestaurantId FROM UserInfo WHERE UserInfo.type = 'admin' AND UserInfo.username = IUsername AND UserInfo.password = IPassword;
    
    IF (VRestaurantId > 0)
        THEN
        DELETE FROM UserInfo WHERE username=IUsernameToDelete AND restaurantId=VRestaurantId AND username!= IUsername;
            IF ROW_COUNT() > 0
                THEN
                SET OReturnCode = 0;         
            ELSE
                SET OReturnCode = -22;
            END IF;
    ELSE
        SET OReturnCode = -2;
    END IF;
ELSEIF (userType = 'kitchen')
    THEN
        SET OReturnCode = -3;
ELSEIF (userType = 'customer')
    THEN
        SET OReturnCode = -4;
ELSEIF (userType = 'waitstaff')
    THEN
        SET OReturnCode = -5;
ELSE
    SET OReturnCode = -6;
END IF;
END //
DELIMITER ;
                    

\c

