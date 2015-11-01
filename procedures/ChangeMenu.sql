DROP PROCEDURE ChangeMenu;
DELIMITER //
CREATE PROCEDURE ChangeMenu(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN INewMenuName VARCHAR(256), IN IMenuId int(11), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
DECLARE VRestaurantId int(11);
Select userType, VRestaurantId;
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'admin')
    THEN
    SELECT restaurantId INTO VRestaurantId FROM UserInfo WHERE UserInfo.type = 'admin' AND UserInfo.username = IUsername AND UserInfo.password = IPassword;
    
    IF (VRestaurantId > 0)
        THEN
        UPDATE UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.restaurantId=MenuList.restaurantId SET menuName=INewMenuName WHERE menuId=IMenuId;
        
        IF ROW_COUNT() = 1
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



