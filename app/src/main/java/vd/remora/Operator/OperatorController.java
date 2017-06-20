package vd.remora.Operator;

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

public class OperatorController {

    private OperatorListenerInterface m_listener = null;
    private OperatorErrorListener m_error_listener = null;
    private ArrayList<String> m_operators = new ArrayList<>();
    private ArrayList<String> m_steps = new ArrayList<>();

    public void setListener( OperatorListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( OperatorErrorListener a_listerner ){
        m_error_listener = a_listerner;
    }

    public void fetchOnDB( Context a_context ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.createAllCardsURL();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseToLists(response, m_operators, m_steps);
                notifyListener();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        m_error_listener.onError( error.toString() );
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( a_context );
        requestQueue.add(stringRequest);
    }


    public void notifyListener(){
        m_listener.setOperators(m_operators);
        m_listener.setSteps(m_steps);
    }


    /**
     * Fiil provided string array list from response string values
     * @param response List under JSON format
     * @param a_operators Output list of operators
     * @param a_steps Output list of steps
     */
    protected void responseToLists(String response, ArrayList<String> a_operators, ArrayList<String> a_steps){
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
