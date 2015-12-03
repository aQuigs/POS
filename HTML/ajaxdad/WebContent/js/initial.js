$(function() {
    
    var $formLogin = $('#login-form');
    var $formLost = $('#lost-form');
    var $formRegister = $('#register-form');
    var $divForms = $('#div-forms');
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;

    $("form").submit(function () {
        switch(this.id) {
            case "login-form":
                var $lg_username=$('#login_username').val();
                var $lg_password=$('#login_password').val();
                
                login($lg_username, $lg_password);
                
                return false;
                break;
            case "lost-form":
                var $ls_email=$('#lost_email').val();
                
                forgot($ls_email);
                
                return false;
                break;
            case "register-form":
                var $rg_username=$('#register_username').val();
                var $rg_password=$('#register_password').val();
                var $rg_email=$('#register_email').val();
                
                register($rg_username, $rg_email, $rg_password);
                
                return false;
                break;
            default:
                return false;
        }
        return false;
    });
    
    $('#login_register_btn').click( function () { modalAnimate($formLogin, $formRegister) });
    $('#register_login_btn').click( function () { modalAnimate($formRegister, $formLogin); });
    $('#login_lost_btn').click( function () { modalAnimate($formLogin, $formLost); });
    $('#lost_login_btn').click( function () { modalAnimate($formLost, $formLogin); });
    $('#lost_register_btn').click( function () { modalAnimate($formLost, $formRegister); });
    $('#register_lost_btn').click( function () { modalAnimate($formRegister, $formLost); });
    
    function modalAnimate ($oldForm, $newForm) {
        var $oldH = $oldForm.height();
        var $newH = $newForm.height();
        $divForms.css("height",$oldH);
        $oldForm.fadeToggle($modalAnimateTime, function(){
            $divForms.animate({height: $newH}, $modalAnimateTime, function(){
                $newForm.fadeToggle($modalAnimateTime);
            });
        });
    }
    
    function msgFade ($msgId, $msgText) {
        $msgId.fadeOut($msgAnimateTime, function() {
            $(this).text($msgText).fadeIn($msgAnimateTime);
        });
    }
    
    function msgChange($divTag, $iconTag, $textTag, $divClass, $iconClass, $msgText) {
        var $msgOld = $divTag.text();
        msgFade($textTag, $msgText);
        $divTag.addClass($divClass);
        $iconTag.removeClass("glyphicon-chevron-right");
        $iconTag.addClass($iconClass + " " + $divClass);
        setTimeout(function() {
            msgFade($textTag, $msgOld);
            $divTag.removeClass($divClass);
            $iconTag.addClass("glyphicon-chevron-right");
            $iconTag.removeClass($iconClass + " " + $divClass);
  		}, $msgShowTime);
    }
    
  //AJAX Functions for login modal
    function login(username, password)
    {
        xmlHttpRequest.open("POST", "LoginVerification?username=" + username + "&password=" + password, true);
        xmlHttpRequest.onreadystatechange = processLogin;
        xmlHttpRequest.send();
    }

    function processLogin()
    {
        if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
        {
            var results = xmlHttpRequest.responseText.split("::");
            if (results.length == 3)
            {
            	console.log(results);
                var accountType = results[0];
                var restaurantId = results[2];
                var location = "";
                
                if(accountType == "admin")
                {
                	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Welcome Admin User");
                    location = "/POS/admin.html";
                }
                else if(accountType == "kitchen")
                {
                	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Welcome Kitchen Member!");
                    location = "/POS/kitchen.html";
                }
                else if(accountType == "waitstaff")
                {
                	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Welcome Wait Staff Member!");
                    location = "/POS/waitstaff.html";
                }
                else if(accountType == "customer")
                {
                	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Welcome!");
                    location = "/POS/index.html";
                }
                else
                {
                	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "Invalid Credentials");
                }
                
                var tempUser = $('#login_username').val();

                setCookie("username", tempUser, 1);
                setCookie("password", results[1], 1);
                setCookie("accountType", accountType);
                setCookie("restaurantId", restaurantId);
                
                window.location.replace(location);
            }
            else if(xmlHttpRequest.responseText == "invalid")
            {
            	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "Invalid Credentials");
            }
            else if (xmlHttpRequest.responseText == "unverified")
            {
                msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "Please activate your account first; see your email for the activation link");
            }
            else if (xmlHttpRequest.responseText == "error")
            {
            	msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "An error has occurred. Please try again later");
            }
        }
    }
    
    function register(username, email, password)
    {
        xmlHttpRequest.open("POST", "Registration?username=" + username + "&password=" + password + "&email=" + email, true);
        xmlHttpRequest.onreadystatechange = registerAccount;
        xmlHttpRequest.send();
    }

    function registerAccount()
    {
        if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
        {
            var result = xmlHttpRequest.responseText;
            
            console.log(result)
            
            if (result == "error" || result == "failed")
            {
            	msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "error", "glyphicon-remove", "Register error");
            }
            else if (result == "success")
            {
            	msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "success", "glyphicon-ok", "Registered Successfully! An email will be sent momentarily");
            }
            else
            {
            	msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "error", "glyphicon-remove", result);
            }
        }
    }
    
    function forgot(email)
    {
        xmlHttpRequest.open("POST", "ResetPassword?email=" + email, true);
        xmlHttpRequest.onreadystatechange = forgotPassword;
        xmlHttpRequest.send();
    }

    function forgotPassword()
    {
        if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
        {
            var result = xmlHttpRequest.responseText;
            
            console.log(result);
            
            if (result == "invalid")
            {
            	msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "error", "glyphicon-remove", "Invalid Email");
            }
            else if (result == "error")
            {
            	msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "error", "glyphicon-remove", "An error has occurred");
            }
            else if (result == "success")
            {
            	msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "success", "glyphicon-ok", "An email will be sent shortly with your new password");
                //window.location.replace("/POS/login.html");
            }
            else
            {
            	msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "error", "glyphicon-remove", result);
            }
        }
    }
});

