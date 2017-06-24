<?php
	define('DB_USER', $_GET["user"]);
	define('DB_PASSWORD', $_GET["password"]);
	define('DB_SERVER', $_GET["server"]);
	define('DB_NAME', $_GET["database"]);

	#Connect to server
	$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_NAME) or die('Unable to connect');
	
	#force datachar set to utf8_decode
	if (!$con->set_charset("utf8")) {
		printf("Erreur lors du chargement du jeu de caractères utf8 : %s\n", $con->error);
		exit();
	}
?>