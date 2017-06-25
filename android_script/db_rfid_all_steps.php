<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 # Select all cards
 $sql = "SELECT * FROM production_state";
 $r = mysqli_query($con,$sql);
 
 # Keep name and type
 $result = array();
 while($row=mysqli_fetch_array($r)){
	array_push($result,array(
		"name"=>$row['name']
		)
	);
 }
 
 echo json_encode(array("result"=>$result));
 
 mysqli_close($con);
 
 }