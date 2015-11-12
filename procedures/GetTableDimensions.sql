DROP PROCEDURE GetTableDimensions;
DELIMITER //
CREATE PROCEDURE GetTableDimensions(IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), OUT OGridWidth int, OUT OGridHeight int)

BEGIN
DECLARE gridWidth int;
DECLARE gridHeight int;

SELECT RestaurantList.tableGridWidth INTO gridWidth FROM RestaurantList INNER JOIN UserInfo ON UserInfo.restaurantId=RestaurantList.restaurantId AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND (UserInfo.type='admin' OR UserInfo.type='waitstaff');

SET OGridWidth = gridWidth;
    
SELECT RestaurantList.tableGridHeight INTO gridHeight FROM RestaurantList INNER JOIN UserInfo ON UserInfo.restaurantId=RestaurantList.restaurantId AND UserInfo.username=IUsername AND UserInfo.password=IPassword AND (UserInfo.type='admin' OR UserInfo.type='waitstaff');

SET OGridHeight = gridHeight;

END //
DELIMITER;