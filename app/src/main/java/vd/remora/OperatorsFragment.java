package vd.remora;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import vd.remora.Operator.Operator;
import vd.remora.Operator.OperatorAdapter;
import vd.remora.Operator.OperatorListenerInterface;

/**
 * Fragment to list Operators
 * Fetch operator list in Bundle under "operators" key
 */
public class OperatorsFragment extends Fragment implements OperatorListenerInterface {

    ListView m_list_view;
    private ProgressDialog m_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator, container, false);

        // Fill operator list
        m_list_view = (ListView) view.findViewById(R.id.list_operators);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_list_view.clearChoices();

        List<Operator> l_operators = new ArrayList<>();
        OperatorAdapter l_adapter = new OperatorAdapter( getContext(), l_operators );
        m_list_view.setAdapter( l_adapter );

        m_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperatorAdapter l_adapter = (OperatorAdapter)m_list_view.getAdapter();
                l_adapter.setSelectedItem( position );
                l_adapter.notifyDataSetChanged();
            }
        });

        //
        FloatingActionButton l_btn = (FloatingActionButton)view.findViewById( R.id.btn_add_operator );
        l_btn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewOperator();
            }
        });

        m_loading = ProgressDialog.show( getActivity(), "Please wait...", "Updating data...", false, false );

        return view;
    }

    void addNewOperator(){
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText( getContext() );
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertOperator( input.getText().toString() );
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void insertOperator(final String a_name ){
        SharedPreferences l_pref = PreferenceManager.getDefaultSharedPreferences( getActivity().getApplicationContext() );
        DBScripts l_DBScripts = new DBScripts( l_pref );
        String url = l_DBScripts.createInsertOperatorURL( a_name );
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean l_ok = response.compareTo( "1" ) == 0;

                if( l_ok ) {
                    // Add operator to list
                    addOperatorToList(a_name);
                }
                // Notify user
                String l_txt = l_ok ? "Operateur créée" : "Un problème est survenu";
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

    protected void addOperatorToList( String a_operator ){
        OperatorAdapter l_adapter = (OperatorAdapter) m_list_view.getAdapter();
        if( l_adapter != null ){
            l_adapter.add( new Operator(a_operator) );
        }
    }

    protected List<Operator> createOperatorList(ArrayList<String> a_operators_name ){
        List<Operator> l_operators = new ArrayList<>();
        for (String name : a_operators_name ){
            l_operators.add( new Operator( name ) );
        }
        return l_operators;
    }

    @Override
    public void setOperators(ArrayList<String> a_operators) {
        OperatorAdapter l_adapter = (OperatorAdapter)m_list_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( createOperatorList(a_operators) );
        l_adapter.notifyDataSetChanged();

        m_loading.dismiss();
    }

    @Override
    public void setSteps(ArrayList<String> a_steps) {}
}
