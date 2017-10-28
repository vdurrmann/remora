<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 # Connect database
 require_once('db_connect.php');

  
 # Get last history
 $select_last = "SELECT date, patient_folder
		FROM history
		INNER JOIN patient ON history.patient_folder = patient.folder
		ORDER BY date DESC
		LIMIT 1";

 $r = mysqli_query($con,$select_last);
 
 while ($row = mysqli_fetch_assoc($r)) {
	$LAST_DATE = $row["date"];
	$FOLDER = $row["patient_folder"];
 }
 
 # Remove last history
 $remove_history = "DELETE FROM history WHERE date = \"$LAST_DATE\"";
 $r = mysqli_query($con,$remove_history);
 
 # Find previous production state for patient
 $select_previous_state = 
		"SELECT history.production_state
		FROM history
		INNER JOIN patient ON history.patient_folder = patient.folder
        WHERE patient_folder = \"$FOLDER\"
		ORDER BY date DESC
		LIMIT 1";

 $r = mysqli_query($con,$select_previous_state);
 $PREVIOUS_STATE = "";
 while ($row = mysqli_fetch_assoc($r)) {
	$PREVIOUS_STATE = $row["production_state"];	
 }
 
 # Update patient with previous history
 $update_patient = "UPDATE patient
		SET production_state = \"$PREVIOUS_STATE\"
		WHERE folder = \"$FOLDER\"";
		
 $r = mysqli_query($con,$update_patient);
 
 echo("1");
 
mysqli_close($con);
 
}