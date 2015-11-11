DROP PROCEDURE ResetPassword;
DELIMITER //
CREATE PROCEDURE ResetPassword(IN IEmail VARCHAR(256), IN INewGeneratedPassword VARCHAR(256), IN INewGeneratedSalt VARCHAR(256), IN INewGeneratedHashedPassword VARCHAR(256), OUT OReturnCode int(3))

ResetPassword:BEGIN
DECLARE userName varchar(255);

SELECT username INTO userName FROM UserInfo WHERE email=IEmail;
IF ROW_COUNT() > 0
    THEN
    Update UserInfo SET password=INewGeneratedPassword,salt=INewGeneratedSalt WHERE username=userName;
    IF ROW_COUNT() > 0
        THEN
        SET OReturnCode = 0;         
    ELSE
        SET OReturnCode = -28;
    END IF;
ELSE
    SET OReturnCode = -29;
END IF;


END //
DELIMITER ;
