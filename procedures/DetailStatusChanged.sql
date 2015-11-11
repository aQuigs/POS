DROP PROCEDURE DetailStatusChanged;
DELIMITER //
CREATE PROCEDURE DetailStatusChanged(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), IN IDetailId INT(11), IN INewStatusName VARCHAR(256), IN ICurrentStatusName VARCHAR(256), OUT OReturnCode int(3))

BEGIN
DECLARE userType varchar(10);
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'kitchen')
    THEN
    UPDATE UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.type='kitchen' INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId SET OrderDetails.status=INewStatusName WHERE OrderDetails.status=ICurrentStatusName AND OrderDetails.detailId=IDetailId;                    
    IF (Row_Count() >0)
        THEN
        SET OReturnCode = 0;
    ELSE
        SET OReturnCode = -12;
    END IF;
ELSEIF (userType = 'waitress')
    THEN
    UPDATE UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND UserInfo.type='waitress' INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId SET OrderDetails.status=INewStatusName WHERE OrderDetails.status=ICurrentStatusName AND OrderDetails.detailId=IDetailId;
    IF (Row_Count() >0)
        THEN
        SET OReturnCode = 0;
    ELSE
        SET OReturnCode = -13;
    END IF;
ELSE
    SET OReturnCode = -14;
END IF;

END //
DELIMITER ;
                    

\c




