function logOff()
{
	setCookie("username", "", -1);	//Set cookie to expire in -1 days to delete
	window.location.replace("/POS/login.html");
}