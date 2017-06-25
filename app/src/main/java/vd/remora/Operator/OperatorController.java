package vd.remora.Operator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;

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

public class OperatorController {

    private OperatorListenerInterface m_listener = null;
    private DataBaseErrorListener m_error_listener = null;
    private ArrayList<String> m_operators = new ArrayList<>();
    private ArrayList<String> m_steps = new ArrayList<>();

    public void setListener( OperatorListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( DataBaseErrorListener a_listerner ){
        m_error_listener = a_listerner;
    }

    public void fetchOnDB( Context a_context ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.createAllCardsURL();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseToLists(response, m_operators, m_steps);
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

    public void insertOperator(final Context a_context, String a_name ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.createInsertOperatorURL( a_name );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;
                if( l_ok ) {
                    fetchOnDB( a_context );
                }
                m_listener.onOperatorCreated( l_ok );
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

    public void deleteOperator( final Context a_context, String a_operator ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.deleteOperatorURL( a_operator );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;
                if( l_ok ) {
                    fetchOnDB( a_context );
                }
                m_listener.onOperatorCreated( l_ok );
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


    private void _notifyListener(){
        if( m_listener == null ){
            return;
        }

        m_listener.setOperators(m_operators);
        m_listener.setSteps(m_steps);
    }

    private void _notifyErrorListener( String a_error ){
        m_error_listener.onError( a_error );
    }


    /**
     * Fiil provided string array list from response string values
     * @param response List under JSON format
     * @param a_operators Output list of operators
     * @param a_steps Output list of steps
     */
    private void responseToLists(String response, ArrayList<String> a_operators, ArrayList<String> a_steps){
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

}
