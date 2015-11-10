DROP PROCEDURE Registration;
DELIMITER //
CREATE PROCEDURE Registration(IN INewUsername VARCHAR(64), IN INewPasswordHash VARCHAR(256), IN ISalt VARCHAR(255), IN INewEmail VARCHAR(255), IN IUnverifiedHash VARCHAR(256), OUT OReturnCode int(3))

Registration:BEGIN
IF EXISTS(SELECT username FROM UserInfo WHERE UserInfo.username = INewUsername OR UserInfo.email = INewEmail)
    THEN
    SET OReturnCode = -24;
ELSE
    INSERT INTO UserInfo (username,password,salt,email,type,unverifiedHash) VALUES (INewUsername, INewPasswordHash, ISalt, INewEmail, 'customer',IUnverifiedHash);
    IF ROW_COUNT() > 0
        THEN
        SET OReturnCode = 0;         
    ELSE
        SET OReturnCode = -25;
    END IF;
END IF;

END //
DELIMITER ;
