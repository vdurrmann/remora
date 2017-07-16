package vd.remora.Filter;

import android.content.Context;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import vd.remora.DBScripts;

public class FilterController {

    private ArrayList<Filter> m_filters = new ArrayList<>();


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
                // TODO m_listener.onStepCreated( l_ok );
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO _notifyErrorListener( error.toString() );
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue( a_context );
        requestQueue.add(stringRequest);
    }

}
