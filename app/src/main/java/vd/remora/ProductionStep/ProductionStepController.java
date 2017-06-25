package vd.remora.ProductionStep;

import android.content.Context;
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

public class ProductionStepController {

    private ProductionStepListenerInterface m_listener = null;
    private DataBaseErrorListener m_error_listener = null;
    private ArrayList<String> m_steps = new ArrayList<>();

    public void setListener( ProductionStepListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( DataBaseErrorListener a_listerner ){
        m_error_listener = a_listerner;
    }

    public void fetchOnDB( Context a_context ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.allStepsURL();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                _allStepsResponseToList( response, m_steps );
                m_listener.setSteps( m_steps );
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

    public void insertStep( final Context a_context, String a_step_name ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.insertStepURL( a_step_name );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;
                if( l_ok ) {
                    fetchOnDB( a_context );
                }
                m_listener.onStepCreated( l_ok );
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


    private void _allStepsResponseToList(String response, ArrayList<String> a_steps){
        a_steps.clear();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++){
                JSONObject obj = result.getJSONObject(i);
                a_steps.add(obj.getString(DBScripts.KEY_STEP_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void _notifyErrorListener( String a_error ){
        m_error_listener.onError(a_error);
    }

}
