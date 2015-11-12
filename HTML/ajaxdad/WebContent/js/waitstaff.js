if(getCookie("username") == "" || getCookie("password") == "")
{
	window.location.replace("/POS/login.html");
}

$(document).ready(function () {
    restaurantId=1; // TODO get this from server based on credentials
    getMenus();
});

function tableTaken(tableNum)
{
	var tableVar = "#table" + tableNum;
	$(tableVar).toggleClass("taken");
}

function multiTableTaken(tableNum)
{
	var tableVar = ".table" + tableNum;
	$(tableVar).toggleClass("taken");
}

function addRow(rowNum)
{
	return "<div id='row" + rowNum + "' class='row'></div><br />";
}

function addPointDiv(row, column)
{
	return "<div class='col-10 " + row + "-" + column + " empty'></div>";
}
function addTable(tableId, row, column)
{
	var coordinates = "." + row + "-" + column;
	var id = "table" + tableId;
	
	$(coordinates).addClass("table");
	$(coordinates).attr('id', id);
	$(coordinates).removeClass("empty");
	$(coordinates).attr("onclick", 'tableTaken(' + tableId + ');');
}

function addTablePart(tableId, row, column)
{
	var coordinates = "." + row + "-" + column;
	var id = "table" + tableId;
	
	$(coordinates).addClass("table");
	$(coordinates).addClass(id);
	$(coordinates).removeClass("empty");
	$(coordinates).attr("onclick", 'multiTableTaken(' + tableId + ');');
}

function createEmptyGrid(gridRows, gridColumns)
{
	var currentRow = "";
	for(i = 1; i <= gridColumns; i++)
	{
		$('#seating-grid').append(addRow(i));
		
		currentRow = "#row" + i;
		
		for(j = 1; j <= gridRows; j++)
		{
			$(currentRow).append(addPointDiv(i, j));
		}		
	}
}

function getTableLayout()
{ 
    xmlHttpRequest.open("POST", "GetTableLayout?username=" + getCookie("username").toString() + "&password=" + getCookie("password"), true);
    xmlHttpRequest.onreadystatechange = getLayout;
    xmlHttpRequest.send();
}

function getLayout()
{
    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
    {
    	//GridX, GridY then for each table,
    	//tableId, row, column, Width, Height, Capacity, Filled Seats, Booth(YES, NO, Partially)
    	var result = xmlHttpRequest.responseText.split("\n");
    	console.log(result);
    	var gridDimensions = result[0].split(",");
    	
    	createEmptyGrid(gridDimensions[0], gridDimensions[1]);
    	
    	var currentTable;
    	var currentPoint;
    	
    	for(i = 1; i < result.length; i++)	//Insert Tables
    	{
    		currentTable = result[i].split(",");
    		if(currentTable[0] != "")	//If table doesn't have id not going to process
    		{    			
    			//[3] is WIDTH and [4] is HEIGHT
    			if(currentTable[3] == 1 && currentTable[4] == 1)
        		{
        			addTable(currentTable[0], currentTable[1], currentTable[2]);
        		}
        		else if(currentTable[3] != 1 && currentTable[4] == 1)	//Deal with longer tables
        		{
        			//Create tables with width greater than 1
        			for(j = 0; j < currentTable[3]; j++)
        			{
        				addTablePart(currentTable[0], currentTable[1], (parseInt(currentTable[2]) + j));
        				
        				var currentColumn = (parseInt(currentTable[2]) + j);
        				var coordinates = "." + currentTable[1] + "-" + currentColumn;
        				
        				if(currentColumn == currentTable[2])
        				{
        					$(coordinates).addClass("multi-table-border-left");
        				}
        				else if(currentColumn == (parseInt(currentTable[2]) + parseInt(currentTable[3]) - 1))
        				{
        					$(coordinates).addClass("multi-table-border-right");
        				}
        				else
        				{
        					$(coordinates).addClass("multi-table-border-none");
        				}
        				
        			}
        		}
        		else if(currentTable[3] == 1 && currentTable[4] != 1)
        		{
        			//Create tables with height greater than 1
        			for(k = 0; k < currentTable[4]; k++)
        			{
        				addTablePart(currentTable[0], (parseInt(currentTable[1]) + k), currentTable[2]);
        				
        				var currentRow = (parseInt(currentTable[1]) + k);
        				var coordinates = "." + currentRow + "-" + currentTable[2];
        				
        				if(currentRow == currentTable[1])
        				{
        					$(coordinates).addClass("multi-table-border-top");
        				}
        				else if(currentRow == (parseInt(currentTable[1]) + parseInt(currentTable[4]) - 1))
        				{
        					$(coordinates).addClass("multi-table-border-bottom");
        				}
        				else
        				{
        					$(coordinates).addClass("multi-table-border-none");
        				}
        			}
        		}
        		else
        		{
        			for(m = 0; m < currentTable[4]; m++)
        			{	
        				var currentRow = (parseInt(currentTable[1]) + m);
        				
        				for(l = 0; l < currentTable[3]; l++)
            			{
            				addTablePart(currentTable[0], (parseInt(currentTable[1]) + m), (parseInt(currentTable[2]) + l));
            				
            				var currentColumn = (parseInt(currentTable[2]) + l);
            				var coordinates = "." + currentRow + "-" + currentColumn;
            				
            				if((currentRow == currentTable[1]) && (currentColumn == currentTable[2]))
            				{
            					$(coordinates).addClass("multi-table-top-left-corner");
            				}
            				else if((currentRow == currentTable[1]) && (currentColumn == (parseInt(currentTable[2]) + parseInt(currentTable[3]) - 1)))
            				{
            					$(coordinates).addClass("multi-table-top-right-corner");
            				}
            				else if((currentRow == (parseInt(currentTable[1]) + parseInt(currentTable[4]) - 1)) && (currentColumn == currentTable[2]))
            				{
            					$(coordinates).addClass("multi-table-bottom-left-corner");
            				}
            				else if((currentRow == (parseInt(currentTable[1]) + parseInt(currentTable[4]) - 1)) && (currentColumn == (parseInt(currentTable[2]) + parseInt(currentTable[3]) - 1)))
            				{
            					$(coordinates).addClass("multi-table-bottom-right-corner");
            				}
            				else
            				{
            					$(coordinates).addClass("multi-table-border-none");
            				}
            				
            			}
        			}
        		}
    		}
    	}
    }
}

myApp.onPageInit('seating', function (page) {
	getTableLayout();
})