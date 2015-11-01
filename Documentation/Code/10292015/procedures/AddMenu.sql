DROP PROCEDURE AddMenu;
DELIMITER //
CREATE PROCEDURE AddMenu(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IMenuName VARCHAR(256), OUT OReturnCode int(3))

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
        IF EXISTS(SELECT * FROM MenuList WHERE MenuList.restaurantId = VRestaurantId AND MenuList.menuName = IMenuName)
            THEN
                SET OReturnCode = -7;
            ELSE
                
                INSERT MenuList (restaurantId,menuName) VALUES (VRestaurantId, IMenuName);
                IF ROW_COUNT() = 1
                    THEN
                        SELECT LAST_INSERT_ID() INTO OReturnCode;
                    ELSE
                        SET OReturnCode = -1;
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
