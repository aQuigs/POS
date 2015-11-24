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

function enterLogin(event)
{
	if(event.keyCode == 13){
        $("#sign-in").click();
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

function performPost(url,callback) {
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("POST", url, true);
    xmlHttpRequest.onreadystatechange = function () {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            callback(xmlHttpRequest.responseText);
        }
    };
    xmlHttpRequest.send();
}
