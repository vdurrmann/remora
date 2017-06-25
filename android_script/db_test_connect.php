<?php
	define('DB_USER', $_GET["user"]);
	define('DB_PASSWORD', $_GET["password"]);
	define('DB_SERVER', $_GET["server"]);
	define('DB_NAME', $_GET["database"]);

	#Connect to server
	$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_NAME) or die('Unable to connect');
	
	#force datachar set to utf8_decode
	if ( $con->connect_error ) {
		echo( "0");
	}
	else{
		echo ("1");
	}
?>