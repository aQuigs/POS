if(getCookie("username") == "" || getCookie("password") == "")
{
    window.location.replace("/POS/login.html");
}

function getOrderProgress(status) {
    if (status === "PLACED") {
        return 0;
    } else if (status === "STARTED") {
        return 1;
    } else if (status === "COOKED") {
        return 2;
    } else if (status === "DELIVERED") {
        return 3;
    } else if (status === "PAID") {
        return 4;
    } else {
        return 0;
    }
}

function getProgressBar(status, id) {
	var progress = getOrderProgress(status);
    var classes = status === "PAID" ? "progress-bar progress-bar-success" : "progress-bar progress-bar-info progress-bar-striped active";
    return '<div class="progress" style="width: 100%;">'
            + '<div class="'+classes+'" role="progressbar" aria-valuenow="' + progress + '" aria-valuemin="0" aria-valuemax="4" id="progress-' + id + '" style="width: '+(progress*25)+'%">'
          + '<span class="sr-only" id="sr-' + id+'">' + status + '</span>'
       + '</div></div>';
}

function orderCooked(orderId){
    performPost("OrderCooked?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + orderId, 
    function (responseText) {
        if (responseText=="orderStatusChanged")
        {
            $('#order-accordion-'+orderId).remove();
            myApp.alert('Order Cooked');
        }
        else
        {
            myApp.alert('Failed to mark order as cooked');
        }
    });    
}

function orderStarted(orderId){
    performPost("OrderUncooked?username=" + getCookie("username") + "&password=" + getCookie("password") + "&orderId=" + orderId, 
    function (responseText) {
        if (responseText=="orderStatusChanged")
        {
            var mybutton = $('#orderCooked-'+orderId);
            mybutton.attr('onclick', 'orderCooked(' + orderId + ')');
            mybutton.val('Order Cooked');
            var myprogress = $('#progress-order-'+orderId);
            myprogress.css('width', '25%');
            var myprogresstext = $('#sr-order-'+orderId);
            myprogresstext.text('STARTED');
            
            var orderItems = $('#order-accordion-'+orderId).find('li');
            for (var i = 0; i < orderItems.length; ++i) {
                var itemHistoryId = $(orderItems[i]).attr('id');
                var itemHistoryValue = itemHistoryId.substring(12);
                var mybutton = $('#itemCooked-'+itemHistoryValue);
                mybutton.attr('onclick', 'itemCooked(' + itemHistoryValue + ')');
                mybutton.val('Item Cooked');
                var myprogress = $('#progress-'+itemHistoryValue);
                myprogress.css('width', '25%');
                var myprogresstext = $('#sr-'+itemHistoryValue);
                myprogresstext.text('STARTED');
            }

        }
        else
        {
            myApp.alert('Failed to mark order as started');
        }        
    }); 
}


function itemCooked(orderItemId){
    performPost("ItemCooked?itemId=" + orderItemId + "&username=" + getCookie("username") + "&password=" + getCookie("password"),
    function (responseText) {
        if (responseText=="itemChanged" || responseText=="orderFullyChanged")
        {
            var parent = $('#itemHistory-' + orderItemId).parent();
            $('#itemHistory-' + orderItemId).remove();
            if (parent.is(":empty")) {
                parent.parent().parent().parent().parent().remove();
            }

            myApp.alert('Item Order Cooked');

        }
        else
        {
            myApp.alert('Failed to mark item Cooked');
        }       
    });      
}

function itemStarted(orderItemId){
    performPost("ItemStarted?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&itemId=" + orderItemId,
    function (responseText) {
        if (responseText=="itemChanged" || responseText=="orderFullyChanged")
        {
            var mybutton = $('#itemCooked-'+orderItemId);
            mybutton.attr('onclick', 'itemCooked(' + orderItemId + ')');
            mybutton.val('Item Cooked');
            var myprogress = $('#progress-'+orderItemId);
            myprogress.css('width', '25%');
            var myprogresstext = $('#sr-'+orderItemId);
            myprogresstext.text('STARTED');
            
            var parent = $('#itemHistory-' + orderItemId).parent();
            var listofspans =  parent.parent().parent().parent().parent().find('span');
            for (var i = 0; i < listofspans.length; ++i) {
                if ($(listofspans[i]).text() == 'STARTED' && i != 0){
                    break;
                }
                if ((i+1) == listofspans.length){
                    var toGetOrderId = parent.parent().parent().parent().parent().attr('id');
                    gotOrderId = toGetOrderId.substring(16);                 
                    
                    var mybutton = $('#itemStarted-'+gotOrderId);
                    mybutton.attr('onclick', 'itemCooked(' + gotOrderId + ')');
                    mybutton.val('Order Cooked');
                    var myprogress = $('#progress-order-'+gotOrderId);
                    myprogress.css('width', '25%');
                    var myprogresstext = $('#sr-order-'+gotOrderId);
                    myprogresstext.text('STARTED');
                    
                    performPost("OrderCooked?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + gotOrderId, function (responseText) {}); 
                    
                }
                    
            }
            
        }
        else
        {
            myApp.alert('Failed to mark order as delivered');
        }        
    });     
}

function fillCookedQueue()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
        var historyList = $('#cooked-order-queue');
        historyList.empty();

        var currentOrderId = 0;
        var currentAccordion;
    
        var result = xmlHttpRequest.responseText;
        var entries = result.split(';;');
        for (var i = 0; i < entries.length; ++i) {
            var values = entries[i];
            if (values.length) {
                values = values.split('::');
                //var restaurantName = values[0];
                var orderId = values[0];
                var orderItemId = values[1]; 
                var orderStatus = values[2];
                var orderItemStatus = values[3];
                var itemName = values[4];
                var miscInfo = values[5];
                var subMenu = values[6];        
                // start a new accordion
                if (currentOrderId != orderId) {
                    currentOrderId = orderId;
                    
                    var appendString = '<div>Order by:  ' + userName + '</div> +
                    '<li id="order-accordion-'+orderId+'" class="accordion-item"><a href="#" class="item-content item-link">'
                            + '<div class="item-inner">'
                                + '<div class="item-title" style="white-space: normal;"></div>'
                                + getProgressBar(orderStatus, "order-" + orderId);
                    
                    appendString += '<p>';
                    
                    if (orderStatus=="PLACED")
                    {
                        appendString += '<input type="button" id="orderPlaced-' +orderId+ '" value="Order Placed" class="button button-fill" onclick="orderPlaced(' + orderId + ')">';               
                    }
                    
                    appendString +='<input type="button" value="Order Cooked" class="button button-fill" onclick="orderCooked(' + orderId + ')">   </p>';
                                
                    appendString += '</div></a>'
                            + '<div class="accordion-item-content">'
                                + '<div class="content-block">'
                                    + '<div class="list-block accordion-list">'
                                        + '<ul id="history-' + orderId + '"></ul></div></div></div></li>';
                                        
                    
                    historyList.append(appendString);
                    currentAccordion = $('#history-'+orderId);
                }

                // add the item details to the current accordion
                var imgElement = imageUrl.length ? '<img src="/POS/images/'+imageUrl+'" style="width: 100%; height: 100%;">' : '';

                if(orderItemStatus=="PAID")
                {
                    
                }
                else
                {
                    var currentAccordionString = '<li id="itemHistory-' + orderItemId + '" class="accordion-item item-content" style="padding: 10px;">'
                           + '<div style="margin-right: 25px; width: 10vw; height: 100%; max-width: 100%; max-height: 100%;">'+imgElement+'</div>'
                               + '<div class="item-inner">'
                                   + '<div class="item-title" style="width: 100%;">'
                                       + '<h3>' + itemName + '</h3>'
                                       + '<p>' + miscInfo + '</p>'
                                           + getProgressBar(orderItemStatus, orderItemId)
                                           + '<p>';
                                           
                   
                    if (orderItemStatus=="PLACED")
                    {
                        currentAccordionString += '<input type="button" id="itemStarted-' +orderItemId+ '"value="Item Started" class="button button-fill" onclick="itemStarted(' + orderItemId + ')">';               
                    }

                    currentAccordionString += '<input type="button" value="Item Cooked" class="button button-fill" onclick="itemCooked(' + orderItemId + ')">';
                                                
                    currentAccordionString += '</p>';
                    '</div>'
                               + '</div>'
                           + '</div>'
                          + '</li>';          
                    currentAccordion.append(currentAccordionString);                
                }

            }
        }
         
     

    }
}

function fillCookedOrderQueue()
{
    xmlHttpRequest.open("POST", "GetUnfinishedOrders?username=" + getCookie("username") + "&password=" + getCookie("password"), true);
    xmlHttpRequest.onreadystatechange = fillCookedQueue;
    xmlHttpRequest.send();
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
				+ '<a href="#" class="swipeout-delete" onclick="ItemPaid(' + itemNum + ')">Finished</a>'
			+ '</div>'
		+'</li>';
}


$(document).ready(function () {
    restaurantId=1; // TODO get this from server based on credentials    
    myApp.onPageInit('cookedOrders', function (page) {
        fillCookedOrderQueue();
    });
});
