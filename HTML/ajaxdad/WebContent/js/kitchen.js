var orders = 5
var newOrder = "";

function changeOrderQueue()
{
	for(i = 0; i < orders; i++)
	{
		newOrder += '<li class="swipeout">'
		       + '<div class="swipeout-content item-content">'
		         + '<div class="item-media">'
		        	 + '<h4>Order 1:</h4>'
		         + '</div>'
		       + '</div>'
		       + '<div class="swipeout-actions-left">'
		         + '<a href="#" class="in-progess bg-green">In Progress</a>'
		       + '</div>'
		       + '<div class="swipeout-actions-right">'
		         + '<a href="#" class="order-finished swipeout-delete">Order Up!</a>'
		       + '</div>'
		     + '</li>'
	}
	document.getElementById("order-queue").innerHTML = newOrder;
}

window.onload = changeOrderQueue;

function logOff()
{
	window.location.replace("/CSETester/login.html");
}