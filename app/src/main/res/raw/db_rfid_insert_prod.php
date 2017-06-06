<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $DATE = $_GET["date"];
 $STEP = $_GET["step"];
 $FOLDER = $_GET["folder"];
 $OPERATOR = $_GET["operator"];
 
 # Select all cards
 $sql = "INSERT INTO history VALUES ( '$DATE', '$STEP', '$FOLDER', '$OPERATOR' )";
 if ($con->query($sql) === TRUE) {
    echo "1";
} else {
	echo "0";
    #echo "Error: " . $sql . "<br>" . $con->error;
}

 mysqli_close($con);
 
 }