DROP PROCEDURE DeleteMenuItem;
DELIMITER //
CREATE PROCEDURE DeleteMenuItem(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IMenuItemId INT(11), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);

Select userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);


IF(userType = 'admin')
    THEN
    DELETE MenuDetails FROM UserInfo INNER JOIN MenuList ON UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.type=userType AND UserInfo.restaurantId=MenuList.restaurantId INNER JOIN MenuDetails ON MenuDetails.menuId=MenuList.menuId WHERE MenuDetails.menuItemId=IMenuItemId;
  
    IF ROW_COUNT() > 0
        THEN
            SET OReturnCode = 0;
        ELSE
            SET OReturnCode = -9;
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




