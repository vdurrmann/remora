package vd.remora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends Activity{

    // Widgets
    Button m_btn_validate;
    Spinner m_spinner_operator;
    Spinner m_spinner_steps;
    EditText m_txt_folder;
    TextView m_txt_patient;

    //Data
    protected ArrayList<String> m_operators = new ArrayList<>();
    protected ArrayList<String> m_steps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_add_prod );

        m_spinner_operator = (Spinner) findViewById( R.id.spinner_operator);
        m_spinner_steps = (Spinner) findViewById( R.id.spinner_step );

        m_txt_patient = (TextView)findViewById( R.id.txt_patient_name );

        m_txt_folder = (EditText) findViewById( R.id.folder );
        m_txt_folder.addTextChangedListener( folderTextWatcher );

        m_btn_validate = (Button)findViewById( R.id.btn_validate );
        m_btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduction();
            }
        });

        // Get saved data in case of configuration changes
        SavedData l_saved_data = (SavedData)getLastNonConfigurationInstance();
        if( l_saved_data != null ){
            m_operators = l_saved_data.operators();
            m_steps = l_saved_data.steps();
        }
        // Else, get data from previous intent
        else{
            m_operators = getIntent().getStringArrayListExtra( "Operators" );
            m_steps = getIntent().getStringArrayListExtra( "Steps" );
        }

        this.updateList( m_operators, R.id.spinner_operator );
        this.updateList( m_steps, R.id.spinner_step );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_preference:
                Intent intent = new Intent( this, PreferenceActivity.class );
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance(){
        SavedData l_saved_data = new SavedData();
        l_saved_data.setOperators( m_operators );
        l_saved_data.setSteps( m_steps );
        return l_saved_data;
    }

    protected void updateList( ArrayList<String> a_list, int a_widget_id ){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, a_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner l_spinner = (Spinner) findViewById( a_widget_id );
        l_spinner.setAdapter(adapter);
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

        DBScripts l_DBScripts = new DBScripts( this.getApplicationContext() );
        String url = l_DBScripts.createInsertProdURL( l_operator, l_step, l_folder );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String l_txt = response.compareTo("1") == 0 ? "Etape validée" : "Un problème est survenu";
                Toast.makeText( MainActivity.this, l_txt, Toast.LENGTH_LONG ).show();
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( MainActivity.this, error.getMessage().toString(),Toast.LENGTH_LONG ).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Query database to find patient name from input folder
     */
    protected void findPatientName(){
        String l_folder = formatFolder( m_txt_folder.getText().toString() );

        DBScripts l_DBScripts = new DBScripts( this.getApplicationContext() );
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

        RequestQueue requestQueue = Volley.newRequestQueue( this.getApplicationContext() );
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