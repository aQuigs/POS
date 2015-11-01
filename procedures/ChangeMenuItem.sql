DROP PROCEDURE ChangeMenuItem;
DELIMITER //
CREATE PROCEDURE ChangeMenuItem(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IMenuItemId INT(11), IN IMenuId INT(11), IN IItemName VARCHAR(256), IN ICost DECIMAL(6,2), IN ISubMenu VARCHAR(256), IN IDescription VARCHAR(256), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);

Select userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);


IF(userType = 'admin')
    THEN
    IF EXISTS(SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.restaurantId=MenuList.restaurantId AND MenuList.menuId=IMenuId WHERE UserInfo.type='admin')
        THEN
        UPDATE UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username=IUsername AND UserInfo.restaurantId=MenuList.restaurantId INNER JOIN MenuDetails ON MenuList.menuId=MenuDetails.menuId AND MenuDetails.menuItemId=IMenuItemId SET MenuDetails.menuId=IMenuId, MenuDetails.itemName=IItemName, MenuDetails.cost=ICost, MenuDetails.itemDescription = IDescription, MenuDetails.submenu = ISubMenu;

        IF ROW_COUNT() > 0
            THEN
                SET OReturnCode = 0;
            ELSE
                SET OReturnCode = -8;
        END IF;
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




