DROP PROCEDURE ValidateUser;
DELIMITER //
CREATE PROCEDURE ValidateUser (IN IUsername VARCHAR(64), IN IPassword VARCHAR(256), OUT OType VARCHAR(10))
BEGIN
	SELECT type INTO OType FROM UserInfo
WHERE
username = IUsername
AND
password = IPassword
;

END //
DELIMITER ;

