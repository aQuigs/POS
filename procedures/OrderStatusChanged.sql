DROP PROCEDURE OrderStatusChanged;
DELIMITER //
CREATE PROCEDURE OrderStatusChanged(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IOrderId INT(11), IN INewStatusName VARCHAR(256), IN ICurrentStatusName VARCHAR(256), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'waitress' OR userType = 'kitchen')
    THEN
    IF EXISTS(SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON MenuList.restaurantId=UserInfo.restaurantId AND UserInfo.type='kitchen' AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND MenuList.menuId=IMenuID)
        THEN
        UPDATE UserInfo INNER JOIN OrderList ON UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.type='kitchen' AND UserInfo.restaurantId=OrderList.restaurantId INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId SET OrderList.status=INewStatusName,OrderDetails.status=INewStatusName WHERE OrderList.orderId=IOrderId AND OrderDetails.orderId=IOrderId AND OrderList.status=ICurrentStatusName;
        IF (Row_Count() >0)
            THEN
            SET OReturnCode = 0;
        ELSE
            SET OReturnCode = -12;
        END IF;
    ELSE
        SET OReturnCode = -13;
    END IF;
ELSE
    SET OReturnCode = -14;
END IF;

END //
DELIMITER ;
                    

\c




