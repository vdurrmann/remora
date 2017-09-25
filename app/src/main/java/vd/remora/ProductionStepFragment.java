package vd.remora;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.ProductionStep.ProductionStep;
import vd.remora.ProductionStep.ProductionStepAdapter;
import vd.remora.ProductionStep.ProductionStepController;
import vd.remora.ProductionStep.ProductionStepListenerInterface;

public class ProductionStepFragment extends Fragment
        implements ProductionStepListenerInterface, DataBaseErrorListener{

    private ListView m_list_view;
    private ProductionStep m_selected_step = null;

    private ProgressDialog m_loading;

    private ProductionStepController m_steps_controller = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        View view = inflater.inflate( R.layout.fragment_production_step, container, false );

        m_steps_controller = new ProductionStepController();
        m_steps_controller.setListener( this );
        m_steps_controller.setErrorListener( this );

        // Fill operator list
        m_list_view = (ListView) view.findViewById(R.id.list_steps);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_list_view.clearChoices();

        List<ProductionStep> l_steps = new ArrayList<>();
        ProductionStepAdapter l_adapter = new ProductionStepAdapter( getContext(), l_steps, m_steps_controller );
        m_list_view.setAdapter( l_adapter );

        m_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductionStepAdapter l_adapter = (ProductionStepAdapter) m_list_view.getAdapter();
                l_adapter.setSelectedId(position);
                l_adapter.notifyDataSetChanged();
                m_selected_step = l_adapter.getItem(position);
                // Force to update option menu
                getActivity().invalidateOptionsMenu();
            }
        });

        // item is unselected on entry
        m_selected_step = null;

        Button l_btn = (Button)view.findViewById( R.id.btn_add_step );
        l_btn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewStep();
            }
        });

        m_loading = ProgressDialog.show( getActivity(),
                getString(R.string.load_wait),
                getString(R.string.load_load_data),
                false, false );

        m_steps_controller.fetchOnDB( getContext() );

        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate( R.menu.menu_step, menu );
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu ){
        super.onPrepareOptionsMenu(menu);

        MenuItem l_delete_item = menu.findItem(R.id.menu_step_delete);
        boolean l_is_selected = m_selected_step != null && m_selected_step.getName().equals("");
        l_delete_item.setVisible( l_is_selected );
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int l_id = item.getItemId();

        if( l_id == R.id.menu_step_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
            builder.setTitle("Supprimer une étape");
            builder.setMessage("Voulez-vous vraiment supprimer " + m_selected_step.getName() + " ?");
            builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_steps_controller.deleteStep( getContext(), m_selected_step.getName() );
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }

        return false;
    }

    void addNewStep(){
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle(getString(R.string.step_add_title));

        // Set up the input
        final EditText input = new EditText( getContext() );
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_steps_controller.insertStep( getContext(), input.getText().toString() );
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

    @Override
    public void setSteps(ArrayList<String> a_steps) {
        ProductionStepAdapter l_adapter = (ProductionStepAdapter) m_list_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( a_steps );
        l_adapter.notifyDataSetChanged();

        m_loading.dismiss();
    }

    @Override
    public void onStepCreated( boolean l_ok ){
        if( getView() == null ){
            return;
        }
        String l_txt = l_ok ? getString(R.string.step_created_ok) : getString(R.string.an_error_occured);
        Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
    }

    @Override
    public void onStepDeleted(boolean l_ok) {
        if( getView() == null ){
            return;
        }
        String l_txt = l_ok ? getString(R.string.step_deleted_ok) : getString(R.string.an_error_occured);
        Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
    }

    @Override public void onStepOrderFound(String a_order) {}

    @Override
    public void onError(String response) {
        View l_view = getView();
        if( l_view == null ){ return; }
        Snackbar.make( l_view, response, Snackbar.LENGTH_LONG ).show();

        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }
}
