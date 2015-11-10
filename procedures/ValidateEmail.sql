DROP PROCEDURE ValidateEmail;
DELIMITER //
CREATE PROCEDURE ValidateEmail(IN IUnverifiedHash VARCHAR(256), OUT OReturnCode int(3))

ValidateEmail:BEGIN
IF EXISTS(SELECT unverifiedHash FROM UserInfo WHERE unverifiedHash=IUnverifiedHash)
    THEN
    UPDATE UserInfo SET unverifiedHash=NULL WHERE unverifiedHash=IUnverifiedHash;
    IF ROW_COUNT() > 0
        THEN
        SET OReturnCode = 0;         
    ELSE
        SET OReturnCode = -27;
    END IF;

ELSE
    SET OReturnCode = -26;

END IF;

END //
DELIMITER ;
