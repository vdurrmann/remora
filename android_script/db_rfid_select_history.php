<?php 

 ########################################
 # Select History ordered by date
 # Give "limit" parameter with number to history to get
 # If number is not valid, then only 1 history is fetch (the last one)
 ########################################
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');

 $NBHIST = (int)$_GET["limit"];
 if( $NBHIST <= 0 ){
	$NBHIST = 1;
 }
 
 # Select all cards
 $sql = "SELECT * FROM history ORDER BY date DESC LIMIT $NBHIST";

 $r = mysqli_query($con,$sql);
 
 # Keep name and type
 $result = array();
 while ($row = mysqli_fetch_assoc($r)) {
	array_push( $result, array(
		"date"=>$row["date"],
		"step"=>$row["production_state"],
		"folder"=>$row["patient_folder"],
		"operator"=>$row["operator"],
		)
	);
 }

echo json_encode( array("result"=>$result) );
 
 mysqli_close($con);
 
 }