package vd.remora.History;


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

public class HistoryController {

    private ArrayList<History> m_vec_history = new ArrayList<>();

    private HistoryListenerInterface m_listener = null;
    private DataBaseErrorListener m_error_listener = null;

    public void setListener( HistoryListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( DataBaseErrorListener a_listerner ){
        m_error_listener = a_listerner;
    }

    public void fetchoOnDB(Context a_context, int a_nb_history ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.selectHistory(a_nb_history);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                _ResponseToHistoryList( response, m_vec_history );
                m_listener.setHistory( m_vec_history );
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

    private void _ResponseToHistoryList(String response, ArrayList<History> a_histories){
        a_histories.clear();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++){
                JSONObject obj = result.getJSONObject(i);
                String l_date = obj.getString(DBScripts.KEY_HISTORY_DATE);
                String l_folder = obj.getString(DBScripts.KEY_HISTORY_FOLDER);
                String l_operator = obj.getString(DBScripts.KEY_HISTORY_OPERATOR);
                String l_step = obj.getString(DBScripts.KEY_HISTORY_STEP);

                History l_history = new History(l_date, l_folder, l_operator, l_step);
                a_histories.add( l_history );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void _notifyErrorListener( String a_error ){
        m_error_listener.onError(a_error);
    }

}
