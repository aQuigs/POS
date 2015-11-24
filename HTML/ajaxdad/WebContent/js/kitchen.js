var xmlHttpRequest = new XMLHttpRequest();
var orders = 5
var newOrder = "";

if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	setCookie("password", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

function accordItem(orderNum, orderStatus)
{
	return '<li class="accordion-item">'
		 	+ '<a href="#" class="item-content item-link">'
		 		+ '<div class="item-inner">'
		 			+ '<div class="item-title">'
		 				+ 'Order ' + orderNum + ': ' + orderStatus
		 				+ '<p>'
		 					+ '<input type="button" value="In Progress" class="button button-fill" onclick="orderStarted(' + orderNum + ')">'
		 					+ '<input type="button" value="Order Finished" class="button button-fill" onclick="orderCooked(' + orderNum + ')">'
		 				+ '</p>' 		          	
		 			+ '</div>'
		 		+ '</div>'
		 	+ '</a>'
		 	+ '<div class="accordion-item-content">'
		 		+ '<div class="content-block">'
		 			+ '<div class="list-block">'
		 				+ '<ul id="order' + orderNum + '-items">'
		 				+ '</ul>'
		 			+ '</div>'
		 		+ '</div>'
		 	+ '</div>'
		 + '</li>';
}

function accordInnerItem(itemNum, itemStatus, itemName, miscInfo, subMenu)
{
	var itemColor = "deeppurple";
	
	if(subMenu == "appetizer")	
	{
		itemColor = "red";
	}
	else if(subMenu == "entree")
	{
		itemColor = "yellow";
	}
	else if(subMenu == "dessert")
	{
		itemColor = "green";
	}
	else
	{
		itemColor = "indigo";
	}
	
	return '<li class="swipeout color-' + itemColor + '">'
			+ '<div class="swipeout-content item-content border-red">'
				+ '<div class="item-inner">(' + itemStatus + ') ' + itemName + ': ' + miscInfo + '</div>'
			+ '</div>'
			+ '<div class="swipeout-actions-left"></div>'
			+ '<div class="swipeout-actions-right">'
				+ '<a href="#" class="swipeout-delete" onclick="itemCooked(' + itemNum + ')">Finished</a>'
			+ '</div>'
		+'</li>';
}

//AJAX for filling order queue
function fillOrderQueue()
{
    xmlHttpRequest.open("POST", "GetUnfinishedOrders?username=" + getCookie("username") + "&password=" + getCookie("password"), true);
    xmlHttpRequest.onreadystatechange = fillQueue;
    xmlHttpRequest.send();
}

function fillQueue()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        var orders = result.split(";;");
        var currentItem;
        var compareItem;
        var currentOrder = 0;
        var currentOrderId = "";
        
        for(i = 0; i < orders.length; i++)
        {
        	currentItem = orders[i].split("::");
        	
        	if(currentItem[0] != "" && currentItem[2] != "COOKED")
        	{
        		if(currentItem[0] != currentOrder )
            	{
            		$('#order-queue').append(accordItem(currentItem[0], currentItem[2]));
            		currentOrder = currentItem[0];
            		currentOrderId = '#order' + currentOrder + '-items';
            	}

        		$(currentOrderId).append(accordInnerItem(currentItem[1], currentItem[3], currentItem[4], currentItem[5], currentItem[6]));
        	}
        }
    }
}

window.onload = fillOrderQueue;

//AJAX for changing order status to finished
function orderCooked(orderId)
{
	myApp.alert("Waitress has been notified!");
    xmlHttpRequest.open("POST", "OrderCooked?username=" + getCookie("username") + "&password=" + getCookie("password") + "&orderId=" + orderId, true);
    xmlHttpRequest.onreadystatechange = orderStatusChanged;
    xmlHttpRequest.send();
}

function orderStatusChanged()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	$('#order-queue').empty();
    	fillOrderQueue();
    }
}

//AJAX for changing order status to started
function orderStarted(orderId)
{
	myApp.alert("Current order has been updated");
    xmlHttpRequest.open("POST", "OrderStarted?username=" + getCookie("username") + "&password=" + getCookie("password") + "&orderId=" + orderId, true);
    xmlHttpRequest.onreadystatechange = orderStatusChanged;
    xmlHttpRequest.send();
}

function cookedAccordItem(orderNum, orderStatus)
{
	return '<li class="accordion-item">'
		 	+ '<a href="#" class="item-content item-link">'
		 		+ '<div class="item-inner">'
		 			+ '<div class="item-title">'
		 				+ 'Order ' + orderNum + ': ' + orderStatus
		 				+ '<p>'
		 					+ '<input type="button" value="Return To Queue" class="button button-fill" onclick="cookedOrderStarted(' + orderNum + ')">'
		 				+ '</p>' 		          	
		 			+ '</div>'
		 		+ '</div>'
		 	+ '</a>'
		 	+ '<div class="accordion-item-content">'
		 		+ '<div class="content-block">'
		 			+ '<div class="list-block">'
		 				+ '<ul id="order' + orderNum + '-items">'
		 				+ '</ul>'
		 			+ '</div>'
		 		+ '</div>'
		 	+ '</div>'
		 + '</li>';
}

function cookedAccordInnerItem(itemNum, itemStatus, itemName, miscInfo, subMenu)
{
	var itemColor = "deeppurple";
	
	if(subMenu == "appetizer")	
	{
		itemColor = "red";
	}
	else if(subMenu == "entree")
	{
		itemColor = "yellow";
	}
	else if(subMenu == "dessert")
	{
		itemColor = "green";
	}
	else
	{
		itemColor = "indigo";
	}
	
	return '<li class="swipeout color-' + itemColor + '">'
			+ '<div class="swipeout-content item-content border-red">'
				+ '<div class="item-inner">(' + itemStatus + ') ' + itemName + ': ' + miscInfo + '</div>'
			+ '</div>'
		+'</li>';
}

//AJAX for filling order queue
function fillCookedOrderQueue()
{
    xmlHttpRequest.open("POST", "GetUnfinishedOrders?username=" + getCookie("username") + "&password=" + getCookie("password"), true);
    xmlHttpRequest.onreadystatechange = fillCookedQueue;
    xmlHttpRequest.send();
}

function fillCookedQueue()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        var orders = result.split(";;");
        var currentItem;
        var compareItem;
        var currentOrder = 0;
        var currentOrderId = "";
        
        for(i = 0; i < orders.length; i++)
        {
        	currentItem = orders[i].split("::");
        	
        	if(currentItem[0] != "" && currentItem[2] == "COOKED")
        	{
        		if(currentItem[0] != currentOrder )
            	{
            		$('#cooked-order-queue').append(cookedAccordItem(currentItem[0], currentItem[2]));
            		currentOrder = currentItem[0];
            		currentOrderId = '#order' + currentOrder + '-items';
            	}

        		$(currentOrderId).append(cookedAccordInnerItem(i, currentItem[2], currentItem[4], currentItem[5], currentItem[6]));
        	}
        }
    }
}

function cookedOrderStatusChanged()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	console.log(result);
    	$('#cooked-order-queue').empty();
    	fillCookedOrderQueue();
    }
}

//AJAX for changing order status to started
function cookedOrderStarted(orderId)
{
    xmlHttpRequest.open("POST", "OrderUncooked?username=" + getCookie("username") + "&password=" + getCookie("password") + "&orderId=" + orderId, true);
    xmlHttpRequest.onreadystatechange = cookedOrderStatusChanged;
    xmlHttpRequest.send();
}

//AJAX for changing order status to started
function itemCooked(itemId)
{
    xmlHttpRequest.open("POST", "ItemCooked?itemId=" + itemId + "&username=" + getCookie("username") + "&password=" + getCookie("password"), true);
    xmlHttpRequest.onreadystatechange = itemDone;
    xmlHttpRequest.send();
}

function itemDone()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	console.log(result);
    	$('#cooked-order-queue').empty();
    	fillCookedOrderQueue();
    }
}

myApp.onPageInit('cookedOrders', function (page) {
	fillCookedOrderQueue();
});