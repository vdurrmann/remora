package vd.remora;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class SplashScreenActivity extends Activity {

    private ProgressDialog loading;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );

        this.initialize();
    }

    @Override
    protected void onResume(){
        super.onResume();

        this.initialize();
    }

    protected void initialize(){
        loading = ProgressDialog.show( this, "Please wait...", "Initializing application...", false, false );

        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        String url = l_DBScripts.createAllCardsURL();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Split response into operator and step lists
                ArrayList<String> l_operators = new ArrayList<>();
                ArrayList<String> l_steps = new ArrayList<>();
                fillCards(response, l_operators, l_steps);

                loading.dismiss();
                switchToMainActivity( l_operators, l_steps );
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( SplashScreenActivity.this, getString(R.string.pref_error), Toast.LENGTH_LONG).show();

                loading.dismiss();
                switchToPreferenceActivity();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Fiil provided string array list from response string values
     * @param response List under JSON format
     * @param a_operators Output list of operators
     * @param a_steps Output list of steps
     */
    protected void fillCards(String response, ArrayList<String> a_operators, ArrayList<String> a_steps){
        a_operators.clear();
        a_steps.clear();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++){
                JSONObject obj = result.getJSONObject(i);

                String l_type = obj.getString(DBScripts.KEY_CARD_TYPE);
                if( l_type.compareTo("Operateur") == 0 ) {
                    a_operators.add(obj.getString(DBScripts.KEY_CARD_NAME));
                }
                else if( l_type.compareTo("Etape") == 0 ){
                    a_steps.add(obj.getString(DBScripts.KEY_CARD_NAME));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch to MainActivity. It finish this activity
     * @param a_operators Operators'sname list
     * @param a_steps Steps'sname list
     */
    protected void switchToMainActivity( ArrayList<String> a_operators, ArrayList<String> a_steps ){
        Intent l_intent = new Intent( this, MainActivity.class );
        l_intent.putStringArrayListExtra( "Operators", a_operators );
        l_intent.putStringArrayListExtra( "Steps", a_steps );
        startActivity(l_intent);

        this.finish();
    }

    /**
     * Switch to Preference activity and allow to resume to current activity
     */
    protected void switchToPreferenceActivity(){
        Intent l_intent = new Intent( this, PreferenceActivity.class );
        startActivity( l_intent );
    }

}
