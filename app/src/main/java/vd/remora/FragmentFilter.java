package vd.remora;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import vd.remora.Filter.Filter;
import vd.remora.Filter.FilterAdapter;
import vd.remora.Filter.FilterController;
import vd.remora.Filter.FilterCreateView;
import vd.remora.Patient.Patient;
import vd.remora.Patient.PatientAdapter;

import static android.R.id.input;

public class FragmentFilter extends Fragment {

    private FilterController m_controller = null;

    private ListView m_list_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        m_controller = new FilterController();

        m_list_view = (ListView) view.findViewById(R.id.list_filter);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_NONE);
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

        return view;
    }

    private void _createFilter(){
        final FilterCreateView l_view = new FilterCreateView(getContext());
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
                m_controller.createFilter( getContext(), l_filter );
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

}
