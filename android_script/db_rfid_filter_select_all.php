<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 # Select all cards
 $sql = "SELECT * FROM filter";
 $r = mysqli_query($con,$sql);
 
 # Keep name and type
 $result = array();
 while($row=mysqli_fetch_array($r)){
	array_push($result,array(
		"name"=>$row['name'],
		"date_start"=>$row['date_start'],
		"date_end"=>$row['date_end']
		)
	);
 }
 
 echo json_encode(array("result"=>$result));
 
 mysqli_close($con);
 
 }