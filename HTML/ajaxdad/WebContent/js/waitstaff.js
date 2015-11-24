if(getCookie("username") == "" || getCookie("password") == "")
{
    window.location.replace("/POS/login.html");
}

function ChangeFilledSeats(tableId, filledSeats, callback)
{
	performPost("ChangeFilledSeats?username=" + getCookie("username").toString() + "&password=" + getCookie("password") + "&tableId=" + tableId.toString() + "&filledSeats=" + filledSeats.toString(), callback);
}

function tableClicked(tableNum, capacity)
{
    var tableVar = "#table-" + tableNum;
    
    if(!$(tableVar).hasClass("taken"))
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
		                        $(tableVar).addClass("taken");
		                        $(tableVar).attr('filledSeats', filledSeats);
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
                        $(tableVar).removeClass("taken");
                        $(tableVar).attr('filledSeats', 0);
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
});
