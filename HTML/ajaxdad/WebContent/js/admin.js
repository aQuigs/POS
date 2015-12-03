var users = [];
var menuItems = [];
var oldUsername;
var currentItemId = "";
var menuId = "";

if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
    setCookie("password", "", -1);  //Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

//AJAX for Adding User
//Parameters:adminUsername, username, password, email, accountType
function addRestaurantUser()
{ 
	var username = document.getElementById('username').value.toString();
	var password = document.getElementById('password').value.toString();
	
    xmlHttpRequest.open("POST", "AddRestaurantUser?adminUsername=" + getCookie("username").toString() + "&adminPassword=" + getCookie("password").toString() + "&username=" + document.getElementById('username').value.toString() + "&password=" + document.getElementById('password').value.toString() + "&email=" + document.getElementById('email').value.toString() + "&accountType=" + document.getElementById('accountType').value.toString(), true);
    xmlHttpRequest.onreadystatechange = addUser;
    xmlHttpRequest.send();
}

function addUser()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	if(result == "invalid")
    	{
    		myApp.alert("You are not authorized to make the requested change.");
    	}
        else if (result == "failed")
        {
            myApp.alert("The action failed");
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
     xmlHttpRequest.open("POST", "DeleteRestaurantUser?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password").toString() + "&username=" + document.getElementById('username').value.toString(), true);
     xmlHttpRequest.onreadystatechange = deleteUser;
     xmlHttpRequest.send();
 }

 function deleteUser()
 {
      if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
      {
      	var result = xmlHttpRequest.responseText;
      	
      	if(result == "invalid")
      	{
      		myApp.alert("You don't have access to this. You are now leaving.");
      		setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
      	}
      	else if(result == "failed")
      	{
      		myApp.alert("The request you are trying to make is invalid.");
      	}
      	else if(result == "success")
      	{
      		myApp.alert("Account successfully deleted!");
      		$('#username').val("");
      		$('#password').val("");
      		$('#email').val("");
      		$('#usertype').val("");
      		getRestaurantUsers();
      		$('#user-item-after').text("");
      	}
      }
 }

//Load users and store since this will be needed for a couple pages
function getRestaurantUsers()
{
    xmlHttpRequest.open("POST", "GetRestaurantUsers?username=" + getCookie("username") + "&password=" + getCookie("password").toString(), true);
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
    	else if(result == "invalid")
    	{
    		myApp.alert("You are not authorized to make changes to account information.");
    	}
    	else
    	{
    		var userLines = result.split(";;");
    		$('#userSearch').html("");
    		var userSearch = $("#userSearch");
    		
    		for(i = 0; i < userLines.length - 1; i++)
    		{
    			var currentUser = userLines[i].split("::");
    			if(currentUser[0].length > 0)
    			{
	    			var user = {username: currentUser[0], password: currentUser[1], email: currentUser[2], accountType: currentUser[3]};
	    			users.push(user);
    			}
    		
    			userSearch.append('<option value="' + i + '">' + users[i].username + '</option>');
    		
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
	xmlHttpRequest.open("POST", "ChangeRestaurantUser?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password").toString() + "&oldUsername=" + oldUsername + "&username=" + document.getElementById('username').value.toString() + "&password=" + document.getElementById('password').value.toString() + "&email=" + document.getElementById('email').value.toString() + "&accountType=" + document.getElementById('usertype').value.toString(), true);
    xmlHttpRequest.onreadystatechange = changeUser;
    xmlHttpRequest.send();
}

function changeUser()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	
     	if(result == "invalid")
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
     		getRestaurantUsers();
     	}
     	else
     	{
     		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
     	}
     }
}

function getMenuDetails()
{
	$('#item').val("");
	$('#submenu').val("");
	$('#description').val("");
	$('#price').val("");
	$('#menu-item-after').text("");
	
	menuId = $('#menu-select').val();
	
	
	//myApp.alert(getCookie("username"));
    xmlHttpRequest.open("POST", "GetMenuDetails?menuId=" + menuId, true);
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
    	else if(result == "invalid")
    	{
    		myApp.alert("You are not authorized to make changes to account information.");
    	}
    	else
    	{
    		var menuLines = result.split(";;");
    		$('#menuSearch').html("");
    		var menuSearch = $("#menuSearch");
    		
    		menuItems = [];
    		
    		menuSearch.append('<option value="no">Select an Item</option>');
    		
    		for(i = 0; i < menuLines.length - 1; i++)
    		{
    			var currentMenuItem = menuLines[i].split("::");
    			
    			if(currentMenuItem[0].length > 0)
    			{
	    			var menuItem = {item: currentMenuItem[1], itemId: currentMenuItem[0], subMenu: currentMenuItem[4], description: currentMenuItem[2], price: currentMenuItem[3]};
	    			menuItems.push(menuItem);
    			}
    		
    			menuSearch.append('<option value="' + i + '">' + currentMenuItem[1] + '</option>');
    		
    		}
    	}
    }
}

function updateMenuInfo()
{
	var menuVal = $('#menuSearch').val()
	
	console.log(menuItems);
	
	currentItemId = menuItems[menuVal].itemId;
	$('#item').val(menuItems[menuVal].item);
	$('#submenu').val(menuItems[menuVal].subMenu);
	$('#description').val(menuItems[menuVal].description);
	$('#price').val(menuItems[menuVal].price);
	
	$('#save-button').css("display", "inline");
	$('#add-button').css("display", "none");
	$('#add-new-button').css("display", "inline");
	$('#delete-button').css("display", "inline");
}

function addNewDisplay()
{
	$('#item').val("");
	$('#submenu').val("");
	$('#description').val("");
	$('#price').val("");
	
	$('#save-button').css("display", "none");
	$('#add-button').css("display", "inline");
	$('#add-new-button').css("display", "none");
	$('#delete-button').css("display", "none");
}

function addMenuItem()
{ 
	var imageUpload = document.getElementById('imageUpload');
	var formData = new FormData();
	if(imageUpload.files.length > 0)
	{
		formData.append("file", imageUpload.files[0]);
	}
	
    xmlHttpRequest.open("POST", "AddMenuItem?adminUsername=" + getCookie("username").toString() + "&adminPassword=" + getCookie("password").toString() + "&menuId=" + menuId + "&itemName=" + document.getElementById('item').value.toString() + "&cost=" + document.getElementById('price').value.toString() + "&submenu=" + document.getElementById('submenu').value.toString() + "&description=" + document.getElementById('description').value.toString(), true);
    xmlHttpRequest.onreadystatechange = addItem;
    xmlHttpRequest.send(formData);
}

function addItem()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	console.log(result);
    	if(result == "failed")
    	{
    		myApp.alert("Adding the item failed");
    	}
    	else if(result == "error")
    	{
    		myApp.alert("There was an error adding the item.");
    	}
    	else if (parseInt(result) != NaN)
    	{
            myApp.alert("Item successfully created!");
            getMenuDetails();
        }
        else
        {
            myApp.alert(result);
    	}
    }
}

function deleteMenuItem()
{
    xmlHttpRequest.open("POST", "DeleteMenuItem?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password").toString() + "&menuItemId=" + currentItemId, true);
    xmlHttpRequest.onreadystatechange = deleteItem;
    xmlHttpRequest.send();
}

function deleteItem()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	
     	if(result == "invalid")
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
     		$('#item').val("");
     		$('#submenu').val("");
     		$('#description').val("");
     		$('#price').val("");
     		getMenuDetails();
     		$('#menu-item-after').text("");
     	}
     	else
     	{
     		myApp.alert("Whoops! Something went wrong. Your request could not be processed");
     	}
     }
}

function changeMenuItem()
{
    var imageUpload = document.getElementById('imageUpload');
    var formData = new FormData();
    if(imageUpload.files.length > 0)
    {
        formData.append("file", imageUpload.files[0]);
    }
    
    xmlHttpRequest.open("POST", "ChangeMenuItem?adminUsername=" + getCookie("username").toString() + "&adminPassword=" + getCookie("password").toString() + "&menuId=" + menuId + "&menuItemId=" + currentItemId + "&name=" + document.getElementById('item').value.toString() + "&cost=" + document.getElementById('price').value.toString() + "&submenu=" + document.getElementById('submenu').value.toString() + "&description=" + document.getElementById('description').value.toString(), true);
    xmlHttpRequest.onreadystatechange = changeItem;
    xmlHttpRequest.send(formData);
}

function changeItem()
{
	 
     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
     {
     	var result = xmlHttpRequest.responseText;
     	console.log(result);
     	
     	if(result == "invalid")
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

function updateSubMenu()
{
	var menuName = $('#subMenuSearch option:selected').text();
	$('#menuName').val(menuName);
}

function changeSubMenu()
{
	xmlHttpRequest.open("POST", "ChangeMenu?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password") + "&menuId=" + $('#subMenuSearch option:selected').val() + "&menuName=" + $('#menuName').val(), true);
    xmlHttpRequest.onreadystatechange = changeMenu;
    xmlHttpRequest.send();
}

function changeMenu()
{
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
		var result = xmlHttpRequest.responseText;
		
		var newName = $('#menuName').val();
		console.log();
		$('#subMenuSearch option:selected').text(newName);
		$('#sub-menu-item-after').text(newName);
		
		myApp.alert("Menu Name Updated Successfully!");
    }
}

function getSubMenus()
{
	//myApp.alert(getCookie("username"));
    xmlHttpRequest.open("POST", "ListMenus?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password") + "&restaurantId=" + getCookie("restaurantId"), true);
    xmlHttpRequest.onreadystatechange = listMenus;
    xmlHttpRequest.send();
}

function listMenus()
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
    		var menuLines = result.split(";;");
    		$('#subMenuSearch').html("");
    		var subMenuSearch = $("#subMenuSearch");
    		
    		subMenuSearch.append('<option value="no"selected>Select Menu</option>');
    		
    		for(i = 0; i < menuLines.length - 1; i++)
    		{
    			var currentMenuItem = menuLines[i].split("::");
    			if(currentMenuItem[0].length > 0)
    			{
	    			var menuItem = {item: currentMenuItem[1], itemId: currentMenuItem[0], subMenu: currentMenuItem[4], description: currentMenuItem[2], price: currentMenuItem[3]};
	    			menuItems.push(menuItem);
    			}
    		
    			subMenuSearch.append('<option value="' + menuItems[i].itemId + '"selected>' + menuItems[i].item + '</option>');
    		
    		}
    	}
    	
    }
}

function addRestaurantMenu()
{ 
	var menuName = document.getElementById('menuName').value.toString();
	
    xmlHttpRequest.open("POST", "AddMenu?adminUsername=" + getCookie("username").toString() + "&adminPassword=" + getCookie("password") + "&menuName=" + menuName, true);
    xmlHttpRequest.onreadystatechange = addMenu;
    xmlHttpRequest.send();
}

function addMenu()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	myApp.alert(result);
    	
    	if(result == "invalid")
    	{
    		myApp.alert("You are not authorized to make the requested change.");
    	}
        else if (result == "failed")
        {
            myApp.alert("The action failed");
        }
    	else if(result == "taken")
    	{
    		myApp.alert("The menu name selected is already taken. Please choose a different name for this menu.");
    	}
    	else if(result == "error")
    	{
    		myApp.alert("Error adding the new sub menu");
    	}
    	else
    	{
    		myApp.alert("Menu successfully created!");
    	}
    }
}

function changePopupSlider(value) {
    $('#sliderVal').val(value);
}

function changePopupText(value) {
    $('#sitSlider').val(value);
}

function tableClicked(tbl) {
    tbl = $(tbl);
    var modal = myApp.modal({
        title: 'Edit Table',
        text: 'How many guests can be at this table?',
        afterText: '<div class="item-inner">' +
                       '<div class="item-input">' + 
                           '<div class="range-slider">' + 
                                  '<input type="number" value="4" id="sliderVal" oninput="changePopupText(this.value);">' + 
                               '<input  style="padding-top: 10px; padding-bottom: 10px;" id="sitSlider" type="range" min="0" max="15" value="4" step="1" oninput="changePopupSlider(this.value);">' + 
                           '</div>' +
                       '</div>' +
                       '<select id="boothType" class="form-control">' +
                          '<option id="boothStatusY" value="YES">Booth</option>' +
                          '<option id="boothStatusN" value="NO">Chairs</option>' +
                          '<option id="boothStatusP" value="PARTIALLY">Partial Booth</option>' +
                       '</div>' +
                    '</select>',
        buttons: [
        {
            text: 'Cancel'
        },
        {
            text: 'Delete',
            bold: true,
            onClick: function() {
                myApp.confirm('Are you sure you want to delete this user?', function () {
                    tbl.remove();
                });
            }
        },
        {
            text: 'Save',
            bold: true,
            onClick: function () {
                tbl.attr('capacity',$("#sliderVal").val());
                var booth = $("#boothType").val();
                tbl.attr('booth', booth);
                addBoothClass(tbl, booth);
            }
        }
        ]
    });

    var currentBooth = tbl.attr('booth');
    if (currentBooth == "YES") {
        $("#boothStatusY").attr('selected','selected');
    } else if (currentBooth == "PARTIALLY") {
        $("#boothStatusP").attr('selected','selected');
    } else {
        $("#boothStatusN").attr('selected','selected');
    }

    var currentCapacity = tbl.attr('capacity');
    $('#sliderVal').val(currentCapacity);
    $('#sitSlider').val(currentCapacity);
}

function changeGridSize() {
    var h = $('#gridHeight').val();
    var w = $('#gridWidth').val();

    if (h < 1 || w < 1) {
        h = Math.max(1,h);
        w = Math.max(1,w);
        $('#gridHeight').val(h);
        $('#gridWidth').val(w);
    } else {
        newGridDimensions(h, w);
        handleResize();
    }
}

function saveLayout() {
    var uri = "EditTableLayout?adminUsername=" + getCookie("username").toString() 
    + "&adminPassword=" + getCookie("password").toString() + "&gridWidth="+gridDimensions[1]+"&gridHeight="+gridDimensions[0];

    var tables = $('.table');
    for (var i = 0; i < tables.length; ++i) {
        var t = $(tables[i]);
        if (t.css('visibility') === "hidden") {
            t.remove();
        } else if (!t.hasClass('preview')) {
            uri += '&tableId=' + t.attr('tableId');
            uri += '&x=' + t.attr('x');
            uri += '&y=' + t.attr('y');
            uri += '&width=' + t.attr('w');
            uri += '&height=' + t.attr('h');
            uri += '&capacity=' + t.attr('capacity');
            uri += '&booth=' + t.attr('booth');
        }
    }

    performPost(uri, function(responseText) {
        if (responseText && responseText != "error") {
            myApp.alert("Layout has been successfully changed");
        } else {
            myApp.alert("Error saving layout");
        }
    });
}

var clickStatus = 3;
var preview;

function beginAddTable() {
    clickStatus = 1;
}

function clearTableLayout() {
    myApp.confirm('Are you sure you want to clear the table layout?', function () {
        $(".table").remove();
    });
}

function handleMouseMove(event, changePos) {
    var parentOffset = $('#seating-grid').offset(); 
    var relX = event.pageX - parentOffset.left;
    var relY = event.pageY - parentOffset.top
    var mouseC = getColumnFromX(relX);
    var mouseR = getRowFromY(relY);

    if (clickStatus == 1) {
        preview = drawPreviewDiv(mouseC, mouseR, 1, 1, true);
        if (mouseR != -1 && mouseC != -1) {
            preview.css('visibility', 'visible');
        } else {
            preview.css('visibility', 'hidden');
        }
    } else if (clickStatus == 2) {
        if (mouseR != -1 && mouseC != -1) {
            var x = preview.attr('x')-1;
            var y = preview.attr('y')-1;
            var x1 = Math.min(x, mouseC);
            var x2 = Math.max(x, mouseC);
            var y1 = Math.min(y, mouseR);
            var y2 = Math.max(y, mouseR);

            preview = drawPreviewDiv(x1,y1, x2-x1+1, y2-y1+1, changePos);
        }
    }
}

function getInitMenus()
{
    xmlHttpRequest.open("POST", "ListMenus?adminUsername=" + getCookie("username") + "&adminPassword=" + getCookie("password") + "&restaurantId=" + getCookie("restaurantId"), true);
    xmlHttpRequest.onreadystatechange = displayMenus;
    xmlHttpRequest.send();
}

function displayMenus()
{
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
	{
		var result = xmlHttpRequest.responseText;
		var menuSelect = $('#menu-select');
		var menus = result.split(";;");
		
		for(i = 0; i < menus.length; i++)
		{
			var currentMenu = menus[i].split("::");
			if(currentMenu[1])
			{
				menuSelect.append("<option value='" + currentMenu[0] + "'>" + currentMenu[1] + "</option>");
			}
		}
		
		//getMenuDetails();
	}
}

$(document).ready(function() {

    myApp.onPageInit('seating', function() {
        var background = $('#seating-grid');
        background.mousemove(handleMouseMove);

        background.mouseup(function(event) {
            if (clickStatus == 1) {
                if (preview && preview.length && preview.css('visibility') === 'visible') {
                    clickStatus = 2;
                }
            } else if (clickStatus == 2) {
                handleMouseMove(event, true);
                preview.removeClass('preview');
                clickStatus = 3;
                preview.attr('tableId',0);
                preview.attr('capacity',4);
                preview.attr('filledSeats',0);
                preview.attr('booth','NO');
                preview.attr('onclick','tableClicked(this);');
                preview.removeAttr('id');
                preview = undefined;
            }
        });
    });

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
    	getInitMenus();
        //getMenuDetails();
        
        $$('.confirm-ok').on('click', function () {
            myApp.confirm('Are you sure you want to delete this item?', function () {
                deleteMenuItem();
            });
        });
    });

    myApp.onPageInit('getSubMenus', function (page) {
        getSubMenus();
        
        $$('.confirm-ok').on('click', function () {
            myApp.confirm('Are you sure you want to delete this menu?', function () {
                deleteMenu();
            });
        });
    });
});
