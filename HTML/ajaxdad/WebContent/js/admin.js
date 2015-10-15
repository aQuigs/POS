var users = [];
var menuItems = [];
var oldUsername;

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

//AJAX for Adding User
//Parameters:adminUsername, username, password, email, accountType
function addRestaurantUser()
{ 
	var username = document.getElementById('username').value.toString();
	var password = document.getElementById('password').value.toString();
	
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
}
    
  //AJAX for Deleting User
  //Parameters:adminUsername, username, password, email, accountType
 function deleteRestaurantUser()
 {
     xmlHttpRequest.open("POST", "DeleteRestaurantUser?adminUsername=" + getCookie("username") + "&username=" + document.getElementById('username').value.toString(), true);
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
      		myApp.alert("You don't have access to this. You are now leaving.");
      		setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
      	}
      	else if(result == "invalid")
      	{
      		myApp.alert("The request you are trying to make is invalid.");
      	}
      	else if(result == "success")
      	{
      		myApp.alert("Account successfully deleted!");
      	}
      	else
      	{
      		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
      	}
      }
 }

//Load users and store since this will be needed for a couple pages
function getRestaurantUsers()
{
	setCookie("username", "admin", 1);	//Need to take out eventually
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
    		var userLines = result.split("\n");
    		$('#userSearch').html("");
    		var userSearch = $("#userSearch");
    		
    		for(i = 0; i < userLines.length - 1; i++)
    		{
    			var currentUser = userLines[i].split(",");
    			if(currentUser[0].length > 0)
    			{
	    			var user = {username: currentUser[0], password: currentUser[1], email: currentUser[2], accountType: currentUser[3]};
	    			users.push(user);
    			}
    		
    			userSearch.append('<option value="' + i + '"selected>' + users[i].username + '</option>');
    		
    		}
    	}
    	
    }
}

function updateUserInfo()
{
	var userVal = $('#userSearch').val()
	oldUsername = users[userVal].username;
	$('#username').val(users[userVal].username);
	$('#password').val(users[userVal].password);
	$('#email').val(users[userVal].email);
	$('#usertype').val(users[userVal].accountType);
}

function changeRestaurantUser()
{
	xmlHttpRequest.open("POST", "ChangeRestaurantUser?adminUsername=" + getCookie("username") + "&oldUsername=" + oldUsername + "&username=" + document.getElementById('username').value.toString() + "&password=" + document.getElementById('password').value.toString() + "&email=" + document.getElementById('email').value.toString() + "&accountType=" + document.getElementById('usertype').value.toString(), true);
    xmlHttpRequest.onreadystatechange = changeUser;
    xmlHttpRequest.send();
}

function changeUser()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	
     	if(result == "Invalid admin account")
     	{
     		myApp.alert("You don't have access to this. You are now leaving.");
     		setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
     	}
     	else if(result == "invalid")
     	{
     		myApp.alert("The request you are trying to make is invalid.");
     	}
     	else if(result == "success")
     	{
     		myApp.alert("Account successfully updated!");
     	}
     	else
     	{
     		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
     	}
     }
}

function getMenuDetails()
{
	//myApp.alert(getCookie("username"));
    xmlHttpRequest.open("POST", "GetMenuDetails?menuId=1", true);
    xmlHttpRequest.onreadystatechange = getMenu;
    xmlHttpRequest.send();
}

function getMenu()
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
    		var menuLines = result.split("\n");
    		$('#menuSearch').html("");
    		var menuSearch = $("#menuSearch");
    		
    		for(i = 0; i < menuLines.length - 1; i++)
    		{
    			var currentMenuItem = menuLines[i].split(",");
    			if(currentMenuItem[0].length > 0)
    			{
	    			var menuItem = {item: currentMenuItem[1], itemId: currentMenuItem[0], subMenu: currentMenuItem[4], description: currentMenuItem[2], price: currentMenuItem[3]};
	    			menuItems.push(menuItem);
    			}
    		
    			menuSearch.append('<option value="' + i + '"selected>' + menuItems[i].item + '</option>');
    		
    		}
    	}
    	
    }
}

function updateMenuInfo()
{
	var menuVal = $('#menuSearch').val()
	
	$('#item').val(menuItems[menuVal].item);
	$('#itemId').val(menuItems[menuVal].itemId);
	$('#submenu').val(menuItems[menuVal].subMenu);
	$('#description').val(menuItems[menuVal].description);
	$('#price').val(menuItems[menuVal].price);
}

function addMenuItem()
{ 
    xmlHttpRequest.open("POST", "AddMenuItem?adminUsername=" + getCookie("username").toString() + "&menuId=1&itemName=" + document.getElementById('item').value.toString() + "&cost=" + document.getElementById('price').value.toString() + "&submenu=" + document.getElementById('submenu').value.toString() + "&description=" + document.getElementById('description').value.toString(), true);
    xmlHttpRequest.onreadystatechange = addItem;
    xmlHttpRequest.send();
}

function addItem()
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
    		myApp.alert("The item name selected is already taken. Please choose a different name.");
    	}
    	else if(result == "success")
    	{
    		myApp.alert("Item successfully created!");
    	}
    	else
    	{
    		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
    	}
    }
}

function deleteMenuItem()
{
    xmlHttpRequest.open("POST", "DeleteMenuItem?adminUsername=" + getCookie("username") + "&menuItemId=" + document.getElementById('itemId').value.toString(), true);
    xmlHttpRequest.onreadystatechange = deleteItem;
    xmlHttpRequest.send();
}

function deleteItem()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	
     	if(result == "Invalid admin account")
     	{
     		myApp.alert("You don't have access to this. You are now leaving.");
     		setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
     	}
     	else if(result == "invalid")
     	{
     		myApp.alert("The request you are trying to make is invalid.");
     	}
     	else if(result == "success")
     	{
     		myApp.alert("Item successfully deleted!");
     	}
     	else
     	{
     		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
     	}
     }
}

function changeMenuItem()
{
	xmlHttpRequest.open("POST", "ChangeMenuItem?adminUsername=" + getCookie("username") + "&menuItemId=" + document.getElementById('itemId').value.toString() + "&menuId=1&name=" + document.getElementById('item').value.toString() + "&cost=" + document.getElementById('price').value.toString() + "&submenu=" + document.getElementById('submenu').value.toString() + "&description=" + document.getElementById('description').value.toString(), true);
    xmlHttpRequest.onreadystatechange = changeItem;
    xmlHttpRequest.send();
}

function changeItem()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	
     	if(result == "Invalid admin account")
     	{
     		myApp.alert("You don't have access to this. You are now leaving.");
     		setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
     	}
     	else if(result == "invalid")
     	{
     		myApp.alert("The request you are trying to make is invalid.");
     	}
     	else if(result == "success")
     	{
     		myApp.alert("Item Changed Successfully!");
     	}
     	else
     	{
     		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
     	}
     }
}

myApp.onPageInit('manageUsers', function (page) {
	getRestaurantUsers();
	$('#usertype').attr('readonly', true);
	
	$$('.confirm-ok').on('click', function () {
	    myApp.confirm('Are you sure you want to delete this user?', function () {
	    	deleteRestaurantUser();
	    });
	});
});

myApp.onPageInit('manageMenu', function (page) {
	getMenuDetails();
	$('#itemId').attr('readonly', true);
	//$('#submenu').attr('readonly', true);
	
	$$('.confirm-ok').on('click', function () {
	    myApp.confirm('Are you sure you want to delete this item?', function () {
	    	deleteMenuItem();
	    });
	});
});