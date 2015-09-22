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

var xmlHttpRequest = new XMLHttpRequest();

function login()
{
	xmlHttpRequest.open("POST", "loginVerification?username=" + document.getElementById('username').value + "&password=" + document.getElementById('password').value, true);
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
			window.location.replace("http://localhost:8080/AjaxProject01/admin.html");
		}
		else if(accountType == "kitchen")
		{
			window.location.replace("http://localhost:8080/AjaxProject01/kitchen.html");
		}
		else if(accountType == "wait")
		{
			window.location.replace("http://localhost:8080/AjaxProject01/wait.html");
		}
		else if(accountType == "customer")
		{
			window.location.replace("http://localhost:8080/AjaxProject01/home.html");
		}
		else if(accountType == "unknown")
		{
			window.location.replace("http://localhost:8080/AjaxProject01/unknown.html");
		}
		
	}
}