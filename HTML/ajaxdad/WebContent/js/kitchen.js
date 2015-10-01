var xmlHttpRequest = new XMLHttpRequest();
var orders = 5
var newOrder = "";

function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}

//AJAX for filling order queue
function fillOrderQueue()
{
    xmlHttpRequest.open("POST", "GetUnfinishedOrders?username=" + getCookie("username"), true);
    xmlHttpRequest.onreadystatechange = fillQueue;
    xmlHttpRequest.send();
}

function fillQueue()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var result = xmlHttpRequest.responseText;
        var orders = result.split("\n");
        var currentItem = orders[0].split(",");
        var compareItem;
        var currentOrder = currentItem[0];
        var orderQueue = '<li class="swipeout">'
		       				+ '<div class="swipeout-content item-content">'
		       					+ '<div class="item-media">'
		       						+ '<ul>'
		       							+ '<li><h4>Order' + currentItem[0] + '(' + currentItem[2] + '):</h4></li>'
        
        for(i = 0; i < orders.length; i++)	
        {
        	if(i > 0)
        	{
        		currentItem = orders[i].split(",");
        	}
        	
        	if(currentItem[0] != "")
        	{
        		if(currentItem[0] == currentOrder)
        		{
        			orderQueue += '<li><h5>(' + currentItem[3] + ')' + currentItem[4] + '</h5></li>'
        		}
        		else
        		{
        			currentOrder = currentItem[0];
        			orderQueue += '</ul>'
   	 		         		+ '</div>'
   	 		         		+ '</div>'
   	 		         		+ '<div class="swipeout-actions-left">'
   	 		         		+ '<a href="#" onclick="orderStarted(' + currentItem[1] + ')" class="in-progess bg-green">In Progress</a>'
   	 		         		+ '</div>'
   	 		         		+ '<div class="swipeout-actions-right">'
   	 		         		+ '<a href="#" onclick="orderCooked(' + currentItem[1] + ')" class="order-finished swipeout-delete">Order Up!</a>'
   	 		         		+ '</div>'
   	 		         		+ '</li>'
   	 		         		+ '<li class="swipeout">'
   	 		         			+ '<div class="swipeout-content item-content">'
   	 		         				+ '<div class="item-media">'
   	 		         					+ '<ul>'
   	 		         						+ '<li><h4>Order' + currentItem[0] + '(' + currentItem[2] + '):</h4></li>'
   	 		         						+ '<li><h5>(' + currentItem[3] + ')' + currentItem[4] + '</h5></li>'
        		}
        		
        		if(currentItem[5] != "")
        		{
        			orderQueue += '<p>-' + currentItem[5] + '</p>'
        		}
        	}
        }
        
        orderQueue += '</ul>'
	         + '</div>'
	       + '</div>'
	       + '<div class="swipeout-actions-left">'
	         + '<a href="#" onclick="orderStarted(' + currentItem[1] + ')" class="in-progess bg-green">In Progress</a>'
	       + '</div>'
	       + '<div class="swipeout-actions-right">'
	         + '<a href="#" onclick="orderCooked(1)" class="order-finished swipeout-delete">Order Up!</a>'
	       + '</div>'
	     + '</li>'
        
        document.getElementById("order-queue").innerHTML = orderQueue;
    }
}

window.onload = fillOrderQueue;

//AJAX for changing order status to finished
function orderCooked(orderId)
{
	myApp.alert("Waitress has been notified!");
    xmlHttpRequest.open("POST", "OrderCooked?username=" + getCookie("username") + "&orderId=" + orderId, true);
    xmlHttpRequest.onreadystatechange = orderStatusChanged;
    xmlHttpRequest.send();
}

function orderStatusChanged()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	var result = xmlHttpRequest.responseText;
    	
    	myApp.alert(result);
    }
}

//AJAX for changing order status to started
function orderStarted(orderId)
{
	myApp.alert("Current order has been updated");
    xmlHttpRequest.open("POST", "OrderStarted?username=" + getCookie("username") + "&orderId=" + orderId, true);
    xmlHttpRequest.onreadystatechange = orderStatusChanged;
    xmlHttpRequest.send();
}

