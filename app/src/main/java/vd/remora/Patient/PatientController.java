package vd.remora.Patient;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vd.remora.DBScripts;
import vd.remora.DataBaseErrorListener;
import vd.remora.R;


public class PatientController {

    private ArrayList<Patient> m_patients = new ArrayList<>();
    private PatientListenerInterface m_listener = null;
    private DataBaseErrorListener m_error_listener = null;

    public void setListener( PatientListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( DataBaseErrorListener a_listener ){ m_error_listener = a_listener; }

    public void fetchOnDB( Context a_context ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.createSelectPatientURL();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                _responseToLists(response, m_patients);
                _notifyListener();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _notifyErrorListener( error.toString() );
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( a_context );
        requestQueue.add(stringRequest);
    }

    public void findPatient( final Context a_context, String a_folder ){
        String l_formatted_folder = Patient.formatFolder(a_folder);

        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.findPatientURL( l_formatted_folder );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

                    // Update patient name
                    String l_patient_name = "";
                    for (int i = 0; i < result.length(); i++){
                        JSONObject obj = result.getJSONObject(i);

                        String l_name = obj.getString(DBScripts.KEY_PATIENT_NAME);
                        String l_surname = obj.getString(DBScripts.KEY_PATIENT_FIRSTNAME);
                        l_patient_name = l_name + " " + l_surname;
                    }
                    m_listener.onPatientFound( l_patient_name );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _notifyErrorListener( error.getMessage() );
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( a_context );
        requestQueue.add(stringRequest);
    }


    private void _responseToLists(String response, ArrayList<Patient> a_patients){
        a_patients.clear();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++){
                JSONObject obj = result.getJSONObject(i);

                String l_folder = obj.getString(DBScripts.KEY_PATIENT_FOLDER);
                String l_name = obj.getString(DBScripts.KEY_PATIENT_NAME);
                String l_firstname = obj.getString(DBScripts.KEY_PATIENT_FIRSTNAME);
                String l_step = obj.getString(DBScripts.KEY_PATIENT_STEP);
                String l_date = obj.getString(DBScripts.KEY_PATIENT_DATE);

                Patient l_patient = new Patient( l_folder, l_name, l_firstname, l_step, l_date );
                a_patients.add( l_patient );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void _notifyListener(){
        m_listener.setPatients( m_patients );
    }

    private void _notifyErrorListener( String a_error ){ m_error_listener.onError(a_error); }

}
