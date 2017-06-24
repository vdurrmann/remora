<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 # Select all cards
 $sql = "SELECT * FROM patient WHERE production_state!='Retour Archives'";
 $r = mysqli_query($con,$sql);
 
 # Keep name and type
 $result = array();
 while ($row = mysqli_fetch_assoc($r)) {
	array_push( $result, array(
		"folder"=>$row["folder"],
		"name"=>$row["name"],
		"surname"=>$row["surname"],
		"step"=>$row["production_state"],
		"date"=>$row["date_delivery"],
		)
	);
 }

echo json_encode( array("result"=>$result) );
 
 mysqli_close($con);
 
 }