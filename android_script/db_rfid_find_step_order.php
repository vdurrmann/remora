<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $STEP_NAME = $_GET["step"];
 
 # Select all cards
 $sql = "SELECT state_order FROM production_state WHERE name = '$STEP_NAME'";
 $r = mysqli_query($con,$sql);
 
 #Send name and surname
 $result = array();
 while( $row = mysqli_fetch_array($r) ){
	array_push($result,array(
		"order"=>$row['state_order']
		)
	);
 }
 
echo json_encode(array("result"=>$result));

 mysqli_close($con);
 
 }