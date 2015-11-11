DROP PROCEDURE AddMenuItem;
DELIMITER //
CREATE PROCEDURE AddMenuItem(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IMenuId INT(11), IN IItemName VARCHAR(100), IN ICost DECIMAL(6,2), IN ISubMenu VARCHAR(64), IN IDescription VARCHAR(255), IN IImageUrl VARCHAR(255),  OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);

Select userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);


IF(userType = 'admin')
    THEN
     IF EXISTS(SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON MenuList.restaurantId=UserInfo.restaurantId AND UserInfo.type='admin' AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND MenuList.menuId=IMenuID)
        THEN
        INSERT INTO MenuDetails (menuId,itemName,cost,itemDescription, submenu, imageUrl) VALUES (IMenuId, IItemName, ICost, IDescription, ISubMenu, IImageUrl);
        IF ROW_COUNT() > 0
            THEN
                SELECT LAST_INSERT_ID() INTO OReturnCode;
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

