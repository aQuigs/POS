

function logOff()
{
    setCookie("username", "", -1);    //Set cookie to expire in -1 days to delete
    setCookie("password", "", -1);    //Set cookie to expire in -1 days to delete
    setCookie("accountType", "", -1);    //Set cookie to expire in -1 days to delete
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

function getProgressBar(status) {
	var progress = getOrderProgress(status);
    var classes = status === "PAID" ? "progress-bar progress-bar-success" : "progress-bar progress-bar-info progress-bar-striped active";
    return '<div class="progress" style="width: 100%;">'
            + '<div class="'+classes+'" role="progressbar" aria-valuenow="' + progress + '" aria-valuemin="0" aria-valuemax="4" style="width: '+(progress*25)+'%">'
          + '<span class="sr-only">' + status + '</span>'
       + '</div></div>';
}

function populateHistory(responseText) {
    var historyList = $('#orderHistoryList');
    historyList.empty();

    var currentOrderId = 0;
    var currentAccordion;

    var entries = responseText.split('\n');
    for (var i = 0; i < entries.length; ++i) {
        var values = entries[i];
        if (values.length) {
            values = values.split(',');
            var restaurantName = values[0];
            var orderId = values[1];
            var orderStatus = values[2];
            var itemName = values[3];
            var imageUrl = values[4];
            var miscInfo = values[5];
            var itemStatus = values[6];

            // start a new accordion
            if (currentOrderId != orderId) {
                currentOrderId = orderId;
                historyList.append(
                        '<li class="accordion-item"><a href="#" class="item-content item-link">'
                        + '<div class="item-inner">'
                            + '<div class="item-title" style="white-space: normal;">' + restaurantName + '</div>'
                            + getProgressBar(orderStatus)
                        + '</div></a>'
                        + '<div class="accordion-item-content">'
                            + '<div class="content-block">'
                                + '<div class="list-block accordion-list">'
                                    + '<ul id="history-' + orderId + '"></ul></div></div></div></li>')
                currentAccordion = $('#history-'+orderId);
            }

            // add the item details to the current accordion
            var imgElement = imageUrl.length ? '<img src="/POS/images/'+imageUrl+'" style="width: 100%; height: 100%;">' : '';

            currentAccordion.append('<li class="accordion-item item-content" style="padding: 10px;">'
                       + '<div style="margin-right: 25px; width: 10vw; height: 100%; max-width: 100%; max-height: 100%;">'+imgElement+'</div>'
                           + '<div class="item-inner">'
                               + '<div class="item-title" style="width: 100%;">'
                                   + '<h3>' + itemName + '</h3>'
                                   + '<p>' + miscInfo + '</p>'
                                       + getProgressBar(itemStatus)
                               + '</div>'
                           + '</div>'
                       + '</div>'
                      + '</li>');
        }
    }
}

function getHistory() {
    performPost("GetOrderHistory?username="+getCookie("username").toString() + "&password=" + getCookie("password").toString(), populateHistory);
}

$(document).ready(function() {
    myApp.onPageInit('orderHistory', getHistory);
});
