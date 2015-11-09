
var restaurantId, restaurantName, menuId, menuName;

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	setCookie("password", "", -1);	//Set cookie to expire in -1 days to delete
	setCookie("accountType", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
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

function selectRestaurant(rId, rName) {
	restaurantId=rId;
	restaurantName=rName;
	return true;
}

function populateRestaurants(responseText) {
	var ulParent = $('#restaurantList');
	var restaurants = responseText.split("\n");
	ulParent.empty();
	for (var i = 0; i < restaurants.length; ++i) {
		var restaurantInfo = restaurants[i].split(',');
		if (restaurantInfo.length == 2) {	
			ulParent.append('<li><a href="pickMenu.html" onclick="selectRestaurant('+restaurantInfo[0]+',\''+restaurantInfo[1]
				+'\');" data-view=".view-main" class="item-link close-panel"><div class="item-content"><div class=item-inner><div class="item-title">'
				+ restaurantInfo[1]+'</div></div></div></a></li>');
		}
	}
}

function getRestaurants() {
	performPost("ListRestaurants", populateRestaurants)
}

function selectMenu(mId, mName) {
	menuId=mId;
	menuName=mName;
	return true;
}

function populateMenus(responseText) {
	var ulParent = $('#menuList');
	var menus = responseText.split("\n");
	ulParent.empty();
	for (var i = 0; i < menus.length; ++i) {
		var menu = menus[i].split(',');
		if (menu.length == 2) {	
			ulParent.append('<li><a href="ordering.html" onclick="selectMenu('+menu[0]+',\''+menu[1]
				+'\');" data-view=".view-main" class="item-link close-panel"><div class="item-content"><div class=item-inner><div class="item-title">'
				+ menu[1]+'</div></div></div></a></li>');
		}
	}
}

var orderQuantity = [];
var orderSpecialInfo = [];
var orderItemID = [];
var orderItemName = [];

function updateOrder(itemNum)
{
	var sliderName = "sliderItem" + itemNum;
	var txtName =  "txtItem" + itemNum;
	var displayName = "name" + itemNum;
	var quantity = $("#" + sliderName).val();
	var specialInfo = $("#" + txtName).val();
	var name = $("#" + displayName).text();
	
	orderQuantity.push(quantity);
	orderSpecialInfo.push(specialInfo);
	orderItemID.push(itemNum);
	orderItemName.push(name);
	
	myApp.alert("Item added to order!");
}

function removeOrderItem(itemNum)
{
	
}

function getMenuItems()
{
    xmlHttpRequest.open("POST", "GetMenuDetails?menuId=1", true);
    xmlHttpRequest.onreadystatechange = fillMenu;
    xmlHttpRequest.send();
}

function fillMenu()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        
        var menuItems = result.split("\n");
        var subMenuCount = 0;
        var currentSubMenu;   
        var fullMenu= $('#fullMenu');
        var currentAccordian;
        
        fullMenu.empty();
        
        for(i = 0; i < menuItems.length; i++)
        {
        	var currentItem = menuItems[i].split(",");
        	
        	if(currentItem.length == 5)
        	{
        		if(currentSubMenu != currentItem[4])
            	{
            		currentSubMenu = currentItem[4];
            		subMenuCount++;
            		
            		fullMenu.append('<li class="accordion-item"><a href="#" class="item-content item-link">'
                        + '<div class="item-inner">'
                            + '<div class="item-title">' + currentItem[currentItem.length - 1] + '</div>'
                        + '</div></a>'
                        + '<div class="accordion-item-content">'
                            + '<div class="content-block">'
                                + '<div class="list-block accordion-list">'
    			                    + '<ul id="subMenu' + subMenuCount + '"></ul></div></div></div></li>');
            		currentAccordian=$("#subMenu"+subMenuCount);
            	}
            	
            	currentAccordian.append('<li class="accordion-item"><a href="#" class="item-content item-link">'
    	        	       + '<div class="item-inner">'
                           + '<div class="item-title">'
    		                   + '<h3>$' + currentItem[3] + ' <span id="name' + currentItem[0] + '">' +  currentItem[1] + '</span></h3>'
    		                   + '<p>' + currentItem[2] + '</p>'
    	                   + '</div>'
                       + '</div></a>'
                       + '<div class="accordion-item-content">'
                           + '<div class="content-block">'
                               + '<div class="item-inner">'
                                   + '<div class="item-input">'
                                       + '<div class="range-slider">'
                                           + '<input type="range" id="sliderItem' + currentItem[0] + '" min="1" max="10" value="0" step="1">'
                                       + '</div>'
                                   + '</div>'
                               + '</div>'
                               + '<div class="item-inner">'
                                   + '<div class="item-input">'
                                       + '<input type="text" id="txtItem' + currentItem[0] + '" placeholder="Special Instructions">'
                                   + '</div>'
                               + '</div>'
                               + '<a href="#" class="button button-big button-green" onclick="updateOrder(' + currentItem[0] + ')">Add To Order</a>'
                           + '</div>'
                       + '</div>'
                   + '</li>');
            }
        }
    }
}

function submitOrder()
{
	var uri = "PlaceOrder?restaurantId=" + restaurantId;
	if (getCookie("accountType").toString() === "customer")
	{
		uri += "&customerUsername=" + getCookie("username").toString() + "&customerPassword=" + getCookie("password").toString();
	}	
	
	for(i = 0; i < orderQuantity.length; i++)
	{
		uri += "&itemId=" + orderItemID[i] + "&quantity=" + orderQuantity[i] + "&miscInfo=" + orderSpecialInfo[i];
	}
		
    xmlHttpRequest.open("POST", uri, true);
    xmlHttpRequest.onreadystatechange = orderSubmissionResponse;
    xmlHttpRequest.send();
}

function orderSubmissionResponse()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	if(result == "success")
    	{
    		myApp.alert("You're order has been sent!");
    	}
    	else if(result == "error")
    	{
    		myApp.alert("Something went wrong. You're order could not be processed");
    	}
    	else
    	{
    		myApp.alert("Placing order failed. You're order could not be processed");
    	}
    	
    }
}

function getMenus() {
	performPost("ListMenus?restaurantId="+restaurantId,populateMenus);	
}

$(document).ready(function() {
	// populate restaurant list
	myApp.onPageInit("pickMenu", getMenus);
	myApp.onPageInit("pickRestaurant", getRestaurants);
	myApp.onPageReinit("pickRestaurant", getRestaurants);

	myApp.onPageInit('ordering', function (page) {
		getMenuItems();
	});

	myApp.onPageInit('currentOrder', function (page) {
		order = $("#orderItemList");
		if(orderQuantity.length == orderSpecialInfo.length && orderSpecialInfo.length == orderItemID.length && orderItemID.length == orderItemName.length)
		{
			for(i = 0; i < orderQuantity.length; i++)
			{
				order.append('<li class="accordion-item"><a href="#" class="item-content item-link">'
				         + '<div class="item-inner">'
				          +  '<div class="item-title">'
				           + '<h4>' + orderItemName[i] + '</h4>'
				          	 + '<p>Quantity: ' + orderQuantity[i] + '</p>'
				          	 + '<p>'+ orderSpecialInfo[i] + '</p>'
						   + '</div>'
				         + '</div></a>'
				       + '<div class="accordion-item-content">'
				         + '<div class="content-block">'
				           + '<a href="#" class="button button-big button-green" onclick="removeOrderItem(' + orderItemID[i] + ')">Remove From Order</a>'
				         + '</div>'
				       + '</div>'
				     + '</li>');
			}
		}
	});
});
