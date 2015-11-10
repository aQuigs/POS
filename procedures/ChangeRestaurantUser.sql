DROP PROCEDURE ChangeRestaurantUser;
DELIMITER //
CREATE PROCEDURE ChangeRestaurantUser(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IOldUsername VARCHAR(64), IN INewUsername VARCHAR(64), IN INewRestaurantPassword VARCHAR(255), IN INewPassword VARCHAR(256), IN ISalt VARCHAR(255), IN INewEmail VARCHAR(255), IN INewType VARCHAR(10), OUT OReturnCode int(3))

change_restaurant_user:BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);

SELECT userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'admin')
    THEN
    SELECT restaurantId INTO VRestaurantId FROM UserInfo WHERE UserInfo.type = 'admin' AND UserInfo.username = IUsername AND UserInfo.password = IPassword;
    
    IF (VRestaurantId > 0)
        THEN
        IF EXISTS(SELECT username FROM UserInfo WHERE UserInfo.username = INewUsername OR UserInfo.email = INewEmail)
            THEN
            SET OReturnCode = -11;
        ELSE
            UPDATE UserInfo SET username = INewUsername, restaurantPassword=INewPassword, password= INewRestaurantPassword,salt=ISalt,type=INewType,email=INewEmail WHERE restaurantId=VRestaurantId AND username=IOldUsername;
            IF ROW_COUNT() > 0
                THEN
                SET OReturnCode = 0;         
            ELSE
                SET OReturnCode = -20;
            END IF;
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

