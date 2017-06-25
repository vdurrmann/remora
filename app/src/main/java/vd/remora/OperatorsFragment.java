package vd.remora;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Operator.Operator;
import vd.remora.Operator.OperatorAdapter;
import vd.remora.Operator.OperatorController;
import vd.remora.Operator.OperatorListenerInterface;

/**
 * Fragment to list Operators
 * Fetch operator list in Bundle under "operators" key
 */
public class OperatorsFragment extends Fragment
        implements OperatorListenerInterface, DataBaseErrorListener {

    ListView m_list_view;
    private ProgressDialog m_loading;
    private OperatorController m_operator_controller = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator, container, false);

        // Operator controller
        m_operator_controller = new OperatorController();
        m_operator_controller.setErrorListener( this );
        m_operator_controller.setListener( this );

        // Fill operator list
        m_list_view = (ListView) view.findViewById(R.id.list_operators);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_list_view.clearChoices();

        List<Operator> l_operators = new ArrayList<>();
        OperatorAdapter l_adapter = new OperatorAdapter( getContext(), l_operators, m_operator_controller );
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

        m_loading = ProgressDialog.show( getActivity(),
                getString(R.string.load_wait),
                getString(R.string.load_load_data),
                false, false );


        m_operator_controller.fetchOnDB( getContext() );

        return view;
    }

    void addNewOperator(){
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle(getString(R.string.ope_add_title));

        // Set up the input
        final EditText input = new EditText( getContext() );
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_operator_controller.insertOperator( getContext(), input.getText().toString() );
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
    public void onOperatorCreated(boolean a_created) {
        if( getView() == null ){
            return;
        }
        String l_txt = a_created ? "Operateur créée" : "Un problème est survenu";
        Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
    }

    @Override
    public void onOperatorDeleted(boolean a_deleted){
        if( getView() == null ){
            return;
        }
        String l_txt = a_deleted ? "Operateur supprimé" : "Un problème est survenu";
        Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
    }

    @Override
    public void onError(String response) {
        Snackbar.make( getView(), response, Snackbar.LENGTH_LONG ).show();

        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }
}
