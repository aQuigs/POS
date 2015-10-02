var xmlHttpRequest = new XMLHttpRequest();

function login()
{
	xmlHttpRequest.open("POST", "loginVerification?username=" + document.getElementById('userName').value + "&password=" + document.getElementById('password').value, true);
	xmlHttpRequest.onreadystatechange = processTutorials;
	xmlHttpRequest.send();
}

function processLogin()
{
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
	{
		var dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/html");
	}
}

function findTutorials() {
	xmlHttpRequest.open("POST", "AjaxServlett01?username=" + document.getElementById('topicTextInput').value, true);
	xmlHttpRequest.onreadystatechange = processTutorials;
	xmlHttpRequest.send();
	
	document.getElementById('topicTextInput').value = "";
}

function processTutorials() {
	
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
	{
		var table = document.getElementById('tutorialsTable');
		table.innerHTML = "";
		
		var dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/html");
		
		var tutorials = dom.getElementsByTagName('password');
		var headRow = table.insertRow(0);
		var headCell = headRow.insertCell(0);
		headCell.style.backgroundColor = "LightGray";
		headCell.innerHTML = (dom.getElementsByTagName('userName'))[0].childNodes[0].nodeValue;
		
		for(var i = 0; i < tutorials.length; i++)
		{
			row = table.insertRow(i+1);
			cell = row.insertCell(0);
			cell.innerHTML = tutorials[i++].childNodes[0].nodeValue;
		}
		
	}
	
	
}
