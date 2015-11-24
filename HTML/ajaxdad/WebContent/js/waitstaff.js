if(getCookie("username") == "" || getCookie("password") == "")
{
    window.location.replace("/POS/login.html");
}

function ChangeFilledSeats(tableId, filledSeats, callback)
{
	performPost("ChangeFilledSeats?username=" + getCookie("username").toString() + "&password=" + getCookie("password") + "&tableId=" + tableId.toString() + "&filledSeats=" + filledSeats.toString(), callback);
}

function tableTaken(tableNum, capacity)
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

function getTableLayout()
{ 
	performPost("GetTableLayout?username=" + getCookie("username").toString() + "&password=" + getCookie("password"), getLayout);
}

var gridDimensions;

function handleResize()
{
    if (gridDimensions)
    {       
        var parent = $('#seating-grid');
        var hcol = parent.height() / (parseInt(gridDimensions[0]));
        var wcol = parent.width()  / (parseInt(gridDimensions[1]));
        var padding = wcol * 0.1;

        parent.children('div').each(function () {
            $(this).width($(this).attr('w') *wcol-padding);
            $(this).height($(this).attr('h')*hcol-padding);

            $(this).css({left: ($(this).attr('x')-1)*wcol + "px", top: ($(this).attr('y')-1)*hcol + "px", position: 'absolute'});
        });
    }
}

function getLayout(responseText)
{
    var lastResult = responseText.split(";;");

    gridDimensions = lastResult[0].split("::");
    
    var parent = $('#seating-grid');
    parent.empty();

    for (var i = 1; i < lastResult.length; ++i)
    {
        if (lastResult[i].length)
        {
            var attribs = lastResult[i].split("::");
            var tableId = parseInt(attribs[0]);
            var x = parseInt(attribs[1]);
            var y = parseInt(attribs[2]);
            var w = parseInt(attribs[3]);
            var h = parseInt(attribs[4]);
            var capacity = parseInt(attribs[5]);
            var filledSeats = parseInt(attribs[6]);
            var booth = attribs[7];
            parent.append("<div class='table' id='table-" + tableId + "' onclick='tableTaken("+tableId+","+capacity+");'></div>");

            var d = $('#table-' + tableId)

            d.attr('x',x);
            d.attr('y',y);
            d.attr('w',w);
            d.attr('h',h);
            d.attr('capacity',capacity);
            d.attr('filledSeats',filledSeats);
            d.attr('booth',booth);
            if (filledSeats !== 0) {
                d.addClass('taken');
            }
        }
    }
    handleResize();
}

$(document).ready(function () {
    restaurantId=1; // TODO get this from server based on credentials
    getMenus();

	$(window).resize(handleResize);

	myApp.onPageInit('seating', function (page) {
	    getTableLayout();
	});
});
