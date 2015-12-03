if(getCookie("username") == "" || getCookie("password") == "")
{
    window.location.replace("/POS/login.html");
}

function getHistory() {
    performPost("GetCancelableOrders?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString(), populateHistory);
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

function orderPaid(orderId){
    performPost("OrderPaid?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + orderId, 
    function (responseText) {
        if (responseText=="orderStatusChanged")
        {
            $('#order-accordion-'+orderId).remove();
            myApp.alert('Order Paid');
        }
        else
        {
            myApp.alert('Failed to mark as paid');
        }
    });    
}

function orderDelivered(orderId){
    performPost("OrderDelivered?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + orderId, 
    function (responseText) {
        if (responseText=="orderStatusChanged")
        {
            var mybutton = $('#orderToDeliveredButton-'+orderId);
            mybutton.attr('onclick', 'orderPaid(' + orderId + ')');
            mybutton.val('Order Paid');
            var myprogress = $('#progress-order-'+orderId);
            myprogress.css('width', '75%');
            var myprogresstext = $('#sr-order-'+orderId);
            myprogresstext.text('DELIVERED');
            
            var orderItems = $('#order-accordion-'+orderId).find('li');
            for (var i = 0; i < orderItems.length; ++i) {
                var itemHistoryId = $(orderItems[i]).attr('id');
                var itemHistoryValue = itemHistoryId.substring(12);
                var mybutton = $('#orderItemToDeliveredButton-'+itemHistoryValue);
                mybutton.attr('onclick', 'itemPaid(' + itemHistoryValue + ')');
                mybutton.val('Item Paid');
                var myprogress = $('#progress-'+itemHistoryValue);
                myprogress.css('width', '75%');
                var myprogresstext = $('#sr-'+itemHistoryValue);
                myprogresstext.text('DELIVERED');
            }

        }
        else
        {
            myApp.alert('Failed to mark order as delivered111');
        }        
    }); 
}

function orderCanceled(orderId){
    performPost("CancelOrder?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + orderId, 
    function (responseText) {
        if (responseText=="success")
        {
            $('#order-accordion-'+orderId).remove();
            myApp.alert('Order Cancelled');

        }
        else
        {
            myApp.alert('Failed to cancel order');
        }        
    });     
}

function itemPaid(orderItemId){
    performPost("ItemPaid?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&itemId=" + orderItemId,
    function (responseText) {
        if (responseText=="itemChanged" || responseText=="orderFullyChanged")
        {
            var parent = $('#itemHistory-' + orderItemId).parent();
            $('#itemHistory-' + orderItemId).remove();
            if (parent.is(":empty")) {
                parent.parent().parent().parent().parent().remove();
            }

            myApp.alert('Item Order Paid');

        }
        else
        {
            myApp.alert('Failed to mark item paid');
        }       
    });      
}

function itemDelivered(orderItemId){
    performPost("ItemDelivered?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&itemId=" + orderItemId,
    function (responseText) {
        if (responseText=="itemChanged" || responseText=="orderFullyChanged")
        {
            var mybutton = $('#orderItemToDeliveredButton-'+orderItemId);
            mybutton.attr('onclick', 'itemPaid(' + orderItemId + ')');
            mybutton.val('Item Paid');
            var myprogress = $('#progress-'+orderItemId);
            myprogress.css('width', '75%');
            var myprogresstext = $('#sr-'+orderItemId);
            myprogresstext.text('DELIVERED');
            
            var parent = $('#itemHistory-' + orderItemId).parent();
            var listofspans =  parent.parent().parent().parent().parent().find('span');
            for (var i = 0; i < listofspans.length; ++i) {
                if ($(listofspans[i]).text() == 'COOKED' && i != 0){
                    break;
                }
                if ((i+1) == listofspans.length){
                    var toGetOrderId = parent.parent().parent().parent().parent().attr('id');
                    gotOrderId = toGetOrderId.substring(16);                 
                    
                    var mybutton = $('#orderToDeliveredButton-'+gotOrderId);
                    mybutton.attr('onclick', 'orderPaid(' + gotOrderId + ')');
                    mybutton.val('Order Paid');
                    var myprogress = $('#progress-order-'+gotOrderId);
                    myprogress.css('width', '75%');
                    var myprogresstext = $('#sr-order-'+gotOrderId);
                    myprogresstext.text('DELIVERED');
                    
                    performPost("OrderDelivered?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&orderId=" + gotOrderId, function (responseText) {}); 
                    
                }
                    
            }
            
        }
        else
        {
            myApp.alert('Failed to mark order as delivered');
        }        
    });     
}

function itemCanceled(orderItemId){
    performPost("CancelOrderItem?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString() + "&itemId=" + orderItemId,
    function (responseText) {
        if (responseText=="success")
        {
            var parent = $('#itemHistory-' + orderItemId).parent();
            $('#itemHistory-' + orderItemId).remove();
            if (parent.is(":empty")) {
                parent.parent().parent().parent().parent().remove();
            }

            myApp.alert('Item Order Cancelled');

        }
        else
        {
            myApp.alert('Failed to cancel order Item');
        }        
    });      
}

function populateHistory(responseText) {
    var historyList = $('#orderHistoryList');
    historyList.empty();

    var currentOrderId = 0;
    var currentAccordion;

    var entries = responseText.split(';;');
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
            var imageUrl = values[6];
            var miscInfo = values[5];
            var userName = values[7];

            // start a new accordion
            if (currentOrderId != orderId) {
                currentOrderId = orderId;
                
                var appendString = '<div>Order by:  ' + userName + '</div>' +
                '<li id="order-accordion-'+orderId+'" class="accordion-item"><a href="#" class="item-content item-link">'
                        + '<div class="item-inner">'
                            + '<div class="item-title" style="white-space: normal;"></div>'
                            + getProgressBar(orderStatus, "order-" + orderId);
                
                appendString += '<p>';
                
                if (orderStatus=="DELIVERED")
                {
                    appendString += '<input type="button" value="Order Paid" class="button button-fill" onclick="orderPaid(' + orderId + ')"> ';
                }
                else if (orderStatus=="COOKED")
                {
                    appendString += '<input type="button" id="orderToDeliveredButton-' +orderId+ '" value="Order DELIVERED" class="button button-fill" onclick="orderDelivered(' + orderId + ')">';               
                }
                


                appendString +='<input type="button" value="Order Cancel" class="button button-fill" onclick="orderCanceled(' + orderId + ')">   </p>';
                            
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
                                       
               
                if (orderItemStatus=="DELIVERED")
                {
                    currentAccordionString += '<input type="button" value="Item Paid" class="button button-fill" onclick="itemPaid(' + orderItemId + ')"> ';
                }
                else if (orderItemStatus=="COOKED")
                {
                    currentAccordionString += '<input type="button" id="orderItemToDeliveredButton-' +orderItemId+ '"value="Item DELIVERED" class="button button-fill" onclick="itemDelivered(' + orderItemId + ')">';               
                }

                currentAccordionString += '<input type="button" value="Item Cancel" class="button button-fill" onclick="itemCanceled(' + orderItemId + ')">';
                                            
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

function ChangeFilledSeats(tableId, filledSeats, callback)
{
	performPost("ChangeFilledSeats?username=" + getCookie("username").toString() + "&password=" + getCookie("password") + "&tableId=" + tableId.toString() + "&filledSeats=" + filledSeats.toString(), callback);
}

function tableClicked(tbl)
{
    tbl = $(tbl);
    var capacity = tbl.attr('capacity');
    var tableNum = tbl.attr('tableId');
    if(!tbl.hasClass("taken"))
    {
        var modal = myApp.modal({
            title: 'Seat Table ' + tableNum,
            text: 'How many guests will be at this table?',
            afterText: '<div class="item-inner">' +
                           '<div class="item-input">' + 
                               '<div class="range-slider">' + 
                                      '<span id="sliderVal">0</span>' + 
                                   '<input id="sitSlider" type="range" min="0" max="' + capacity + '" value="0" step="1" oninput="showVal(this.value);">' + 
                               '</div></div></div>',
            buttons: [
            {
                text: 'Cancel'
            },
            {
                text: 'Ok',
                bold: true,
                onClick: function () {
                    var filledSeats = $("#sitSlider").val();
                    
                    if(filledSeats == 0)
                    {
                        myApp.alert('No guests were specified, table will not be sat.');
                    }
                    else
                    {
                        myApp.showIndicator();
                        
                        ChangeFilledSeats(tableNum, filledSeats, function(responseText) {
                        	if (responseText === "success") {
		                        myApp.alert('The table has been filled!');
		                        tbl.addClass("taken");
		                        tbl.attr('filledSeats', filledSeats);
		                        myApp.hideIndicator();
                        	} else {
                        		myApp.alert('An error occurred')
		                        myApp.hideIndicator();
                        	}
                        });
                        
                    }
                }
            },
            ]
        });
    }
    else
    {
        var modal = myApp.modal({
            title: 'Empty Table ' + tableNum,
            text: 'Are you sure you want to clear the table?',
            buttons: [
            {
                text: 'Cancel'
            },
            {
                text: 'Clear',
                bold: true,
                onClick: function () {
                    myApp.showIndicator();
                    
                    ChangeFilledSeats(tableNum, 0, function() {
                        tbl.removeClass("taken");
                        tbl.attr('filledSeats', 0);
                        myApp.alert("The table has been cleared!");
                        myApp.hideIndicator();
                    });
                }
            },
            ]
        });
    }
}

function showVal(value)
{
    document.getElementById("sliderVal").innerHTML = value;
}


$(document).ready(function () {
	$("#welcome-text").val("Welcome" + getCookie("username"));
    restaurantId=1; // TODO get this from server based on credentials
    getMenus();
    myApp.onPageInit('ordersInProgress', getHistory);
});
