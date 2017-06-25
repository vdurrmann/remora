<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $NAME = $_GET["name"];
 
 # Select all cards
 $sql = "DELETE FROM production_state WHERE name='$NAME'";
 if ($con->query($sql) === TRUE) {
    echo "1";
} else {
	echo "0";
    #echo "Error: " . $sql . "<br>" . $con->error;
}

 mysqli_close($con);
 
 }