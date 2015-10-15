var orderQuantity = [];
var orderSpecialInfo = [];
var orderItemID = [];
var orderItemName = [];

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

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
        var currentSubMenu = "";//currentItem[currentItem.length - 1];    
        var fullMenu= $('#fullMenu');
        var currentAccordian;
        
        
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

function submitOrder()
{
	var uri = "PlaceOrder?restaurantId=1"
		
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
    		myApp.alert("You done fucked up. You're order could not be processed");
    	}
    	
    }
}