DROP PROCEDURE GetTableLayout;
DELIMITER //
CREATE DEFINER=CURRENT_USER PROCEDURE GetTableLayout(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), OUT OReturnCode int(3), OUT OGridWidth int, OUT OGridHeight int)

BEGIN
DECLARE userType varchar(10);
DECLARE gridWidth int;
DECLARE gridHeight int;
CALL ValidateUser(IUsername, IPassword, userType);

IF(userType = 'admin' OR userType = 'waitstaff')
    THEN
    CALL GetTableDimensions(IUsername, IPassword, gridWidth, gridHeight);
    
    If(gridHeight >=0)
        THEN
        SELECT TableList.tableId,TableList.x,TableList.y,TableList.width,TableList.height,TableList.capacity,TableList.filledSeats,TableList.booth FROM UserInfo INNER JOIN TableList ON UserInfo.restaurantId=TableList.restaurantId AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND (UserInfo.type='admin' OR UserInfo.type='waitstaff');
        IF (Row_Count() > 0)
            THEN
            SET OReturnCode = 0;
            SET OGridHeight = gridHeight;
            SET OGridWidth = gridWidth;
        ELSE
            SET OReturnCode = -30;
        END IF;
    ELSE
        SET OReturnCode = -31;
    END IF;
ELSE
    SET OReturnCode = -32;
END IF;

END //
DELIMITER ;
                    

\c
