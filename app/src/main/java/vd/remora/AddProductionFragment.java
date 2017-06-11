package vd.remora;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vincent-LDLC on 07/06/2017.
 */

public class AddProductionFragment extends Fragment {

    // Widgets
    Button m_btn_validate;
    Spinner m_spinner_operator;
    Spinner m_spinner_steps;
    EditText m_txt_folder;
    TextView m_txt_patient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prod, container, false);

        m_spinner_operator = (Spinner) view.findViewById( R.id.spinner_operator);
        m_spinner_steps = (Spinner) view.findViewById( R.id.spinner_step );

        m_txt_patient = (TextView) view.findViewById( R.id.txt_patient_name );

        m_txt_folder = (EditText) view.findViewById( R.id.folder );
        m_txt_folder.addTextChangedListener( folderTextWatcher );

        m_btn_validate = (Button) view.findViewById( R.id.btn_validate );
        m_btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduction();
            }
        });

        // Fill UI
        MainActivity l_activity = (MainActivity)getActivity();
        this.updateList( l_activity.getOperators(), m_spinner_operator );
        this.updateList( l_activity.getSteps(), m_spinner_steps );

        return view;
    }

    protected void updateList( ArrayList<String> a_list, Spinner a_spinner ){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, a_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a_spinner.setAdapter(adapter);
    }

    protected void setValidateEnable( boolean a_enable ){
        m_btn_validate.setEnabled(a_enable);
    }

    // Text watcher on patient folder entries
    private TextWatcher folderTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if( s.toString().length() > 0 ) {
                findPatientName();
            }
            else{
                updatePatientName( "" );
                setValidateEnable(false);
            }
        }
    };

    protected void insertProduction(){
        String l_operator = m_spinner_operator.getSelectedItem().toString();
        String l_step = m_spinner_steps.getSelectedItem().toString();
        String l_folder = m_txt_folder.getText().toString();

        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(getContext()) );
        String url = l_DBScripts.createInsertProdURL( l_operator, l_step, l_folder );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String l_txt = response.compareTo("1") == 0 ? "Etape validée" : "Un problème est survenu";
                Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make( getView(), error.getMessage().toString(), Snackbar.LENGTH_LONG ).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( getActivity() );
        requestQueue.add(stringRequest);
    }

    /**
     * Query database to find patient name from input folder
     */
    protected void findPatientName(){
        String l_folder = formatFolder( m_txt_folder.getText().toString() );

        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(getContext()) );
        String url = l_DBScripts.createFindPatientURL( l_folder );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

                    // Update patient name
                    String l_patient_name = getResources().getString(R.string.no_patient);
                    boolean l_is_valid = false;
                    for (int i = 0; i < result.length(); i++){
                        JSONObject obj = result.getJSONObject(i);

                        String l_name = obj.getString(DBScripts.KEY_PATIENT_NAME);
                        String l_surname = obj.getString(DBScripts.KEY_PATIENT_SURNAME);
                        l_patient_name = l_name + " " + l_surname;
                        l_is_valid = true;
                    }

                    updatePatientName( l_patient_name );
                    setValidateEnable( l_is_valid );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updatePatientName( "An error occured" );
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( getActivity().getApplicationContext() );
        requestQueue.add(stringRequest);
    }

    void updatePatientName( String a_name ){
        m_txt_patient.setText( a_name );
    }

    String formatFolder( String a_folder ){
        try{
            Integer.parseInt(a_folder);//test it is an integer
            int length = a_folder.length();
            switch(length){
                case 1 : a_folder = "000"+a_folder;
                    break;
                case 2 : a_folder = "00"+a_folder;
                    break;
                case 3 : a_folder = "0"+a_folder;
                    break;
                default :break;
            }
        }
        catch(NumberFormatException e){}
        return a_folder;
    }

}
