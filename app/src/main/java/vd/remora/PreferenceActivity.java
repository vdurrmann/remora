package vd.remora;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PreferenceActivity extends Activity {

    EditText m_txt_server;
    EditText m_txt_username;
    EditText m_txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_preferences );

        m_txt_server = (EditText)findViewById( R.id.pref_server );
        m_txt_username = (EditText)findViewById( R.id.pref_username );
        m_txt_password = (EditText)findViewById( R.id.pref_password );

        this.readPreferences();

        Button l_btn_save = (Button)findViewById( R.id.btn_save );
        l_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                switchToMainActivity();
            }
        });

        Button l_btn_test = (Button)findViewById(R.id.pref_btn_test);
        l_btn_test.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savePreferences();
                _testConnection();
            }
        });
    }

    /**
     * Go to MainActivity and finish this
     */
    protected void switchToMainActivity(){
        Intent l_intent = new Intent( this, MainActivity.class );
        startActivity( l_intent );
        this.finish();
    }

    /**
     * Save current values in SharedPreferences
     */
    protected void savePreferences(){
        SharedPreferences l_preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor l_pref_editor = l_preferences.edit();

        l_pref_editor.putString( "pref_server", m_txt_server.getText().toString() );
        l_pref_editor.putString( "pref_username", m_txt_username.getText().toString() );
        l_pref_editor.putString( "pref_password", m_txt_password.getText().toString() );

        l_pref_editor.apply();
    }

    /**
     * Update UI from current SharedPreferences values
     */
    protected void readPreferences(){
        SharedPreferences l_preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        m_txt_server.setText( l_preferences.getString( "pref_server", "") );
        m_txt_username.setText( l_preferences.getString( "pref_username", "") );
        m_txt_password.setText( l_preferences.getString( "pref_password", "") );
    }

    protected void _testConnection(){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        String url = l_DBScripts.testDBConnection();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;
                String l_txt = getString( l_ok ? R.string.pref_test_ok : R.string.pref_test_nok);
                Toast.makeText( getApplicationContext(), l_txt, Toast.LENGTH_LONG ).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add(stringRequest);
    }

}
