var xmlHttpRequest = new XMLHttpRequest();
var orders = 5
var newOrder = "";

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	setCookie("password", "", -1);	//Set cookie to expire in -1 days to delete
	setCookie("accountType", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

function accordItem(orderNum, orderStatus)
{
	return '<li class="accordion-item">'
		 	+ '<a href="#" class="item-content item-link">'
		 		+ '<div class="item-inner">'
		 			+ '<div class="item-title">'
		 				+ 'Order ' + orderNum + ': ' + orderStatus
		 				+ '<p class="buttons-row">'
		 					+ '<input type="button" value="In Progress" class="button button-fill">'
		 					+ '<input type="button" value="Order Finished" class="button button-fill">'
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
				+ '<a href="#" class="swipeout-delete">Finished</a>'
			+ '</div>'
		+'</li>';
}

//AJAX for filling order queue
function fillOrderQueue()
{
    xmlHttpRequest.open("POST", "GetUnfinishedOrders?username=" + getCookie("username") + "&password=" + getCookie("password").toString(), true);
    xmlHttpRequest.onreadystatechange = fillQueue;
    xmlHttpRequest.send();
}

function fillQueue()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        myApp.alert(result);
        var orders = result.split("\n");
        var currentItem;
        var compareItem;
        var currentOrder = 0;
        var currentOrderId = "";
        
        for(i = 0; i < orders.length; i++)
        {
        	currentItem = orders[i].split(",");
        	
        	if(currentItem[0] != "")
        	{
        		if(currentItem[0] != currentOrder )
            	{
            		$('#order-queue').append(accordItem(currentItem[0], currentItem[2]));
            		currentOrder = currentItem[0];
            		currentOrderId = '#order' + currentOrder + '-items';
            	}

        		$(currentOrderId).append(accordInnerItem(i, currentItem[3], currentItem[4], currentItem[5], currentItem[6]));
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
    	
    	console.log(result);
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

