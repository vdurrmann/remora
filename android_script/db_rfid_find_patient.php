<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $FOLDER = $_GET["folder"];
 
 # Select all cards
 $sql = "SELECT name, surname FROM patient WHERE folder = '$FOLDER'";
 $r = mysqli_query($con,$sql);
 
 #Send name and surname
 $result = array();
 while( $row = mysqli_fetch_array($r) ){
	array_push($result,array(
		"name"=>$row['name'],
		"surname"=>$row['surname']
		)
	);
 }
 
echo json_encode(array("result"=>$result));

 mysqli_close($con);
 
 }