package vd.remora.Filter;

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

public class FilterController {

    private ArrayList<Filter> m_filters = new ArrayList<>();
    private FilterListenerInterface m_listener = null;
    private DataBaseErrorListener m_error_listener = null;


    public void fetchOnDB( Context a_context ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.selectAllFilter();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                _allFiltersResponseToList( response, m_filters );
                m_listener.setFilters( m_filters );
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

    public void createFilter(final Context a_context, Filter a_filter ){
        DBScripts l_DBScripts = new DBScripts( PreferenceManager.getDefaultSharedPreferences(a_context) );
        String url = l_DBScripts.createFilter( a_filter );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;
                if( l_ok ) {
                    // TODO fetchOnDB( a_context );
                }
                m_listener.onFilterCreated(l_ok);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        m_error_listener.onError(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( a_context );
        requestQueue.add(stringRequest);
    }

    public void setListener( FilterListenerInterface a_listener ){
        m_listener = a_listener;
    }

    public void setErrorListener( DataBaseErrorListener a_listener ){
        m_error_listener = a_listener;
    }

    private void _allFiltersResponseToList(String response, ArrayList<Filter> a_filters){
        a_filters.clear();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DBScripts.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++){
                JSONObject obj = result.getJSONObject(i);
                Filter l_filter = new Filter();
                l_filter.setName( obj.getString(DBScripts.KEY_FILTER_NAME) );
                l_filter.setDateStart( obj.getString(DBScripts.KEY_FILTER_DATE_START) );
                l_filter.setDateEnd( obj.getString(DBScripts.KEY_FILTER_DATE_END) );

                a_filters.add( l_filter );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
