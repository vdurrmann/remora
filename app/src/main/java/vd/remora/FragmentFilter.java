package vd.remora;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Filter.Filter;
import vd.remora.Filter.FilterAdapter;
import vd.remora.Filter.FilterController;
import vd.remora.Filter.FilterCreateView;
import vd.remora.Filter.FilterListenerInterface;
import vd.remora.ProductionStep.ProductionStepAdapter;
import vd.remora.ProductionStep.ProductionStepController;
import vd.remora.ProductionStep.ProductionStepListenerInterface;

public class FragmentFilter extends Fragment
        implements DataBaseErrorListener,
        ProductionStepListenerInterface,
        FilterListenerInterface
{

    private ProgressDialog m_loading;
    private FilterController m_filters_controller = null;

    private ListView m_list_view;
    private ArrayList<String> m_production_steps = new ArrayList<>();
    private Filter m_selected_filter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        m_filters_controller = new FilterController();
        m_filters_controller.setListener( this );
        m_filters_controller.setErrorListener( this );

        ProductionStepController l_step_controller = new ProductionStepController();
        l_step_controller.setListener( this );
        l_step_controller.setErrorListener( this );

        m_list_view = (ListView) view.findViewById(R.id.list_filter);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_list_view.clearChoices();

        List<Filter> l_filters = new ArrayList<>();
        FilterAdapter l_adapter = new FilterAdapter( getContext(), l_filters );
        m_list_view.setAdapter( l_adapter );
        m_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterAdapter l_adapter = (FilterAdapter) m_list_view.getAdapter();
                l_adapter.setSelectedId(position);
                l_adapter.notifyDataSetChanged();
                m_selected_filter = l_adapter.getItem(position);
            }
        });

        // item is unselected on entry
        m_selected_filter = null;

        Button l_btn = (Button)view.findViewById( R.id.filter_btn_add );
        l_btn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                _createFilter();
            }
        });

        l_step_controller.fetchOnDB(getContext());
        m_filters_controller.fetchOnDB(getContext());
        m_loading = ProgressDialog.show( getActivity(),
                getString(R.string.load_wait),
                getString(R.string.load_load_data),
                false, false );

        return view;
    }

    private void _createFilter(){
        final FilterCreateView l_view = new FilterCreateView(getContext());
        l_view.setSteps( m_production_steps );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle("Create Filter");
        builder.setView( l_view );

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Filter l_filter = new Filter();
                l_filter.setName( l_view.name() );
                l_filter.setDateStart( l_view.dateStart() );
                l_filter.setDateEnd( l_view.dateEnd() );
                m_filters_controller.createFilter( getContext(), l_filter );
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
    public void onError(String response) {
        View v = getView();
        if( v == null ){ return; }
        Snackbar.make( v, response, Snackbar.LENGTH_LONG ).show();
        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }

    // Production step interface used to
    // get list of steps
    @Override
    public void setSteps(ArrayList<String> a_steps) {
        m_production_steps = a_steps;
        m_loading.dismiss();
    }
    @Override public void onStepCreated(boolean l_ok) {}
    @Override public void onStepDeleted(boolean l_ok) {}
    @Override public void onStepOrderFound(String a_order) {}

    @Override
    public void setFilters(ArrayList<Filter> a_filters) {
        FilterAdapter l_adapter = (FilterAdapter) m_list_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( a_filters );
        l_adapter.notifyDataSetChanged();
        m_loading.dismiss();
    }

    // Filter interface
    @Override
    public void onFilterCreated(boolean l_ok) {
        if( getView() == null ){ return; }
        String l_txt = l_ok ? getString(R.string.filter_created_ok) : getString(R.string.an_error_occured);
        Snackbar.make( getView(), l_txt, Snackbar.LENGTH_LONG ).show();
    }

    @Override
    public void onFilterDeleted(boolean l_ok) {

    }
}
