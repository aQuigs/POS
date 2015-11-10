if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

$(document).ready(function () {
    restaurantId=1; // TODO get this from server based on credentials
    getMenus();
});

function tableTaken(tableNum)
{
	var tableVar = "#table" + tableNum;
	//myApp.alert("shalom!");
	$(tableVar).toggleClass("taken");
}

function doubleTaken(tableNum)
{
	var tableVar = ".double" + tableNum;
	//myApp.alert("shalom!");
	$(tableVar).toggleClass("taken");
}


