<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 # Select all cards
 $sql = "SELECT folder, name, surname, production_state FROM patient WHERE 1";
 $r = mysqli_query($con,$sql);
 
 #Send name and surname
 $result = array();
 while( $row = mysqli_fetch_array($r) ){
	array_push($result,array(
		"folder"=>$row['folder'],
		"name"=>$row['name'],
		"surname"=>$row['surname'],
		"step"=>$row['production_state']
		)
	);
 }
 
echo json_encode(array("result"=>$result));

 mysqli_close($con);
 
 }