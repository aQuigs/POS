DROP PROCEDURE AddRestaurantUser;
DELIMITER //
CREATE PROCEDURE AddRestaurantUser(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN INewUsername VARCHAR(64), IN INewRestaurantPassword VARCHAR(255), IN INewPassword VARCHAR(256), IN ISalt VARCHAR(255), IN INewEmail VARCHAR(255), IN INewType VARCHAR(10), OUT OReturnCode int(3))

add_restaurant_user:BEGIN
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
            INSERT INTO UserInfo (username,restaurantPassword,password,salt,type,email,restaurantId) VALUES (INewUsername, INewRestaurantPassword, INewPassword, ISalt, INewType, INewEmail, VRestaurantId);             
            IF ROW_COUNT() > 0
                THEN
                SET OReturnCode = 0;         
            ELSE
                SET OReturnCode = -10;
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
