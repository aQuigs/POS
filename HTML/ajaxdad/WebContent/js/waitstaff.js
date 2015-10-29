if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

$(document).ready(function () {
    restaurantId=1; // TODO get this from server based on credentials
    getMenus();
});