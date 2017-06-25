package vd.remora.Patient;

import java.util.ArrayList;

public interface PatientListenerInterface {

    void setPatients( ArrayList<Patient> a_patients );

    /**
     * Result of findPatient
     * @param a_patient_name Patient's name. "" if not found
     */
    void onPatientFound( String a_patient_name );

    void onProductionStepUpdated( boolean l_ok );
}
