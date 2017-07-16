package vd.remora;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Filter.Filter;
import vd.remora.Filter.FilterAdapter;
import vd.remora.Filter.FilterController;
import vd.remora.Filter.FilterCreateView;
import vd.remora.ProductionStep.ProductionStep;
import vd.remora.ProductionStep.ProductionStepAdapter;
import vd.remora.ProductionStep.ProductionStepController;
import vd.remora.ProductionStep.ProductionStepListenerInterface;

public class FragmentFilter extends Fragment
        implements DataBaseErrorListener,
        ProductionStepListenerInterface
{

    private ProgressDialog m_loading;
    private FilterController m_filters_controller = null;
    private ProductionStepController m_step_controller = null;

    private ListView m_list_view;
    private ArrayList<String> m_production_steps = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        m_filters_controller = new FilterController();

        m_step_controller = new ProductionStepController();
        m_step_controller.setListener( this );
        m_step_controller.setErrorListener( this );

        m_list_view = (ListView) view.findViewById(R.id.list_filter);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_list_view.clearChoices();

        List<Filter> l_filters = new ArrayList<>();
        FilterAdapter l_adapter = new FilterAdapter( getContext(), l_filters );
        m_list_view.setAdapter( l_adapter );

        FloatingActionButton l_btn = (FloatingActionButton)view.findViewById( R.id.filter_btn_add );
        l_btn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                _createFilter();
            }
        });

        m_step_controller.fetchOnDB(getContext());
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
        Snackbar.make( getView(), response, Snackbar.LENGTH_LONG ).show();
        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }

    @Override
    public void setSteps(ArrayList<String> a_steps) {
        m_production_steps = a_steps;
        m_loading.dismiss();
    }
    @Override public void onStepCreated(boolean l_ok) {}
    @Override public void onStepDeleted(boolean l_ok) {}
    @Override public void onStepOrderFound(String a_order) {}
}
