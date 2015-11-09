DROP PROCEDURE DeleteMenu;
DELIMITER //
CREATE PROCEDURE DeleteMenu(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IMenuId int(11), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);
DECLARE tempValue int(10);
Select userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'admin')
    THEN
    SELECT restaurantId INTO VRestaurantId FROM UserInfo WHERE UserInfo.type = 'admin' AND UserInfo.username = IUsername AND UserInfo.password = IPassword;
    
    IF (VRestaurantId > 0)
        THEN
        DELETE MenuDetails FROM UserInfo INNER JOIN MenuList ON UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.type=userType AND UserInfo.restaurantId=MenuList.restaurantId INNER JOIN MenuDetails ON MenuDetails.menuId=MenuList.menuId WHERE MenuList.menuId=IMenuId;
        DELETE MenuList FROM UserInfo INNER JOIN MenuList ON UserInfo.type=userType AND UserInfo.username=IUserName AND UserInfo.restaurantId=MenuList.restaurantId AND menuId=IMenuId;
        
        IF ROW_COUNT() > 0
            THEN
            SET OReturnCode = 0;
            ELSE
            SET OReturnCode = -1;
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



