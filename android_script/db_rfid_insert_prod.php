<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');
 
 $DATE = $_GET["date"];
 $STEP = $_GET["step"];
 $FOLDER = $_GET["folder"];
 $OPERATOR = $_GET["operator"];
 
 $ok = TRUE;
 # Add production to history
 $sql = "INSERT INTO history VALUES ( '$DATE', '$STEP', '$FOLDER', '$OPERATOR' )";
 if ($con->query($sql) === TRUE) {
    $ok = TRUE;
 } else {
	$ok = FALSE;
 }

 # Update Patient step
 $sql = "UPDATE patient SET production_state='$STEP' WHERE folder='$FOLDER'";
 if ($con->query($sql) === TRUE) {
	 $ok = $ok AND TRUE;
 }else{
	 $ok = $ok AND FALSE;
 }

 # Send result
 if( $ok ){
	echo ( "1" );
 }else{
	echo ( "0" );
 }
 
 mysqli_close($con);
 
 }