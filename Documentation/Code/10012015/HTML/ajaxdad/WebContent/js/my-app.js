var xmlHttpRequest = new XMLHttpRequest();

// Initialize your app
var myApp = new Framework7();

// Export selectors engine
var $$ = Dom7;

// Add views
var leftView = myApp.addView('.view-left', {
    // Because we use fixed-through navbar we can enable dynamic navbar
    dynamicNavbar: true
});
var mainView = myApp.addView('.view-main', {
    // Because we use fixed-through navbar we can enable dynamic navbar
    dynamicNavbar: true
});

myApp.onPageInit('about', function (page) {
  // Do something here for "about" page
  
})


function login()
{
    xmlHttpRequest.open("POST", "LoginVerification?username=" + document.getElementById('username').value + "&password=" + document.getElementById('password').value, true);
    xmlHttpRequest.onreadystatechange = processLogin;
    xmlHttpRequest.send();
}

function processLogin()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var accountType = xmlHttpRequest.responseText;
        
        if(accountType == "admin")
        {
        	setCookie("username", document.getElementById('username').value, 1);
            window.location.replace("/POS/admin.html");
        }
        else if(accountType == "kitchen")
        {
        	setCookie("username", document.getElementById('username').value, 1);
            window.location.replace("/POS/kitchen.html");
        }
        else if(accountType == "wait")
        {
        	setCookie("username", document.getElementById('username').value, 1);
            window.location.replace("/POS/wait.html");
        }
        else if(accountType == "customer")
        {
        	setCookie("username", document.getElementById('username').value, 1);
            window.location.replace("/POS/index.html");
        }
        else if(accountType == "invalid")
        {
            alert('Invalid username/password combination');
        }
        else if (accountType == "unverified")
        {
            alert('Please activate your account first; see your email for the activation link');
        }
        else if (accountType == "error")
        {
            alert('An error occurred');
        }
    }
}

function forgotRedirect()
{
    window.location.replace("/POS/forgot.html");
}

function registerRedirect()
{
    window.location.replace("/POS/register.html");
}

function forgot()
{
    xmlHttpRequest.open("POST", "ResetPassword?email=" + document.getElementById('email').value, true);
    xmlHttpRequest.onreadystatechange = processReset;
    xmlHttpRequest.send();
}

function processReset()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        if (result == "invalid")
        {
            alert("invalid email");
        }
        else if (result == "error")
        {
            alert("an error occurred");
        }
        else if (result == "success")
        {
            alert("An email with your new password has been sent to your account");
            window.location.replace("/POS/login.html");
        }
    }
}
function register()
{
    xmlHttpRequest.open("POST", "Registration?username=" + document.getElementById('username').value + "&password=" + document.getElementById('password').value + "&email=" + document.getElementById('email').value, true);
    xmlHttpRequest.onreadystatechange = processReset;
    xmlHttpRequest.send();
}

function processReset()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        if (result == "taken")
        {
            alert("Username/email already in use");
        }
        else if (result == "error" || result == "failure")
        {
            alert("An error occurred");
        }
        else if (result == "success")
        {
            alert("Your account has been created and a verification email has been sent to your email, please access the link inside to activate your account");
            window.location.replace("/POS/login.html");
        }
    }
}

//Kitchen Interface Swipe Notifications
$$('.in-progress').on('click', function () {
	  myApp.alert('The order has been updated to in progress!');
	}); 

$$('.order-finished').on('click', function () {
	  myApp.alert('Waitress has been notified!');
	});

//Cookie Functions
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}
