
function getTableLayout()
{ 
	performPost("GetTableLayout?username=" + getCookie("username").toString() + "&password=" + getCookie("password"), getLayout);
}

var gridDimensions;

function getColumnFromX(x) {
    var parent = $('#seating-grid');
    var h = parseInt(gridDimensions[0]);
    var w = parseInt(gridDimensions[1]);
    var hcol = parent.height() / h;
    var wcol = parent.width()  / w;
    var padding = wcol * 0.1;

    if (x % wcol > wcol-padding)
        return -1;
    return Math.floor(x / wcol);    
}

function getRowFromY(y) {
    var parent = $('#seating-grid');
    var h = parseInt(gridDimensions[0]);
    var w = parseInt(gridDimensions[1]);
    var hcol = parent.height() / h;
    var wcol = parent.width()  / w;
    var padding = wcol * 0.1;

    if (y % hcol > hcol-padding)
        return -1;
    return Math.floor(y / hcol);    
}

function drawPreviewDiv(c,r,w,h, changePos) {
    var previewDiv = $('#preview-div');
    var parent = $('#seating-grid');
    if (!previewDiv.length) {
        parent.append("<div class='table preview' id='preview-div'></div>");
        previewDiv = $('#preview-div');
    }

    var hcol = parent.height() / (parseInt(gridDimensions[0]));
    var wcol = parent.width()  / (parseInt(gridDimensions[1]));
    var padding = wcol * 0.1;

    previewDiv.css({left: c*wcol + "px", top: r*hcol + "px", position: 'absolute'});
    previewDiv.width(w*wcol - padding);
    previewDiv.height(h*hcol - padding);

    if (changePos) {
        previewDiv.attr('x',c+1);
        previewDiv.attr('y',r+1);
    } else {
        previewDiv.attr('w',w);
        previewDiv.attr('h',h);
    }

    return previewDiv;
}

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

function findGridForPosition(x,y) {
    var parent = $('#seating-grid');
    var hcol = parent.height() / (parseInt(gridDimensions[0]));
    var wcol = parent.width()  / (parseInt(gridDimensions[1]));
    var padding = wcol * 0.1;

    
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
            parent.append("<div class='table' id='table-" + tableId + "' onclick='tableClicked("+tableId+","+capacity+");'></div>");

            var d = $('#table-' + tableId)

            d.attr('tableId',tableId);
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

$(document).ready(function() {    
    $(window).resize(handleResize);

    myApp.onPageInit('seating', function (page) {
        getTableLayout();
    });
});
