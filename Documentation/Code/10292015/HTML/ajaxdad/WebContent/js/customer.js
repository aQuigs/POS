if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

$(document).ready(function() {
	getRestaurants();
});


