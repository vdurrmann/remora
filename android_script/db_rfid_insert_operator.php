<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $ID = $_GET["id"];
 $NAME = $_GET["name"];
 
 # Select all cards
 $sql = "INSERT INTO card_rfid VALUES ( \"'$ID'\", \"Operateur\", \"'$NAME'\" )";
 if ($con->query($sql) === TRUE) {
    echo "1";
} else {
	echo "0";
    #echo "Error: " . $sql . "<br>" . $con->error;
}

 mysqli_close($con);
 
 }