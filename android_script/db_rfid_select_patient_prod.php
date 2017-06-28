<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $ORDER = $_GET["step_order"];
 
 # Select
 $sql = "SELECT folder, patient.name, surname, date_delivery, production_state
FROM patient
INNER JOIN production_state 
ON patient.production_state = production_state.name
AND state_order<='$ORDER'
ORDER BY state_order DESC, date_delivery DESC, production_state ASC";
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