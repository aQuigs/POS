function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

//AJAX for Adding User
//Parameters:adminUsername, username, password, email, accountType
function addRestaurantUser()
{ 
	alert("shalom");
	var username = document.getElementById('username').value.toString();
	var password = document.getElementById('password').value.toString();
	myApp.alert(username + " " + password);
    xmlHttpRequest.open("POST", "AddRestaurantUser?adminUsername=" + getCookie("username").toString() + "&username=" + document.getElementById('username').value.toString() + "&password=" + document.getElementById('password').value.toString() + "&email=" + document.getElementById('email').value.toString() + "&accountType=" + document.getElementById('accountType').value.toString(), true);
    xmlHttpRequest.onreadystatechange = addUser;
    xmlHttpRequest.send();
}

function addUser()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	if(result == "Invalid admin account")
    	{
    		myApp.alert("You are not authorized to make the requested change.");
    	}
    	else if(result == "taken")
    	{
    		myApp.alert("The username selected is already taken. Please choose a different username.");
    	}
    	else if(result == "success")
    	{
    		myApp.alert("Account successfully created!");
    	}
    	else
    	{
    		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
    	}
    }
    
  //AJAX for Deleting User
  //Parameters:adminUsername, username, password, email, accountType
  function deleteRestaurantUser()
  {
      xmlHttpRequest.open("POST", "DeleteRestaurantUser?adminUsername=" + getCookie("username") + "&username=" + document.getElementById('username'), true);
      xmlHttpRequest.onreadystatechange = deleteUser;
      xmlHttpRequest.send();
  }

  function deleteUser()
  {
      if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
      {
      	var result = xmlHttpRequest.responseText;
      	
      	if(result == "Invalid admin account")
      	{
      		myApp.alert("Who the hell do you think you are trying to access this?");
      	}
      	else if(result == "invalid")
      	{
      		myApp.alert("The request you are trying to make is invalid you fucking idiot.");
      	}
      	else if(result == "success")
      	{
      		myApp.alert("Account successfully created!");
      	}
      	else
      	{
      		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
      	}
      }
  }
}

//Load users and store since this will be needed for a couple pages
function getRestaurantUsers()
{
	setCookie("username", "admin", 1);
	//myApp.alert(getCookie("username"));
    xmlHttpRequest.open("POST", "GetRestaurantUsers?username=" + getCookie("username"), true);
    xmlHttpRequest.onreadystatechange = getUsers;
    xmlHttpRequest.send();
}

function getUsers()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	if(result == "error")
    	{
    		myApp.alert("An error occurred retrieving the data!");
    	}
    	else if(result == "Invalid admin account")
    	{
    		myApp.alert("You are not authorized to make changes to account information.");
    	}
    	else
    	{
    		myApp.alert(result);
    		var users = result.split("\n");
    	}
    	
    }
}

window.onload = getRestaurantUsers;
