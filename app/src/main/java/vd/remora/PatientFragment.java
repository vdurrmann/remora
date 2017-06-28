package vd.remora;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Patient.Patient;
import vd.remora.Patient.PatientAdapter;
import vd.remora.Patient.PatientController;
import vd.remora.Patient.PatientListenerInterface;
import vd.remora.ProductionStep.ProductionStepController;
import vd.remora.ProductionStep.ProductionStepListenerInterface;

public class PatientFragment extends Fragment
        implements PatientListenerInterface,
        DataBaseErrorListener,
        ProductionStepListenerInterface{

    ProgressDialog m_loading;

    Spinner m_spinner_steps;
    ListView m_list_view;

    private ProductionStepController m_steps_controller = null;

    private PatientController m_patient_controller = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        // Controller
        m_patient_controller = new PatientController();
        m_patient_controller.setListener( this );
        m_patient_controller.setErrorListener( this );

        m_steps_controller = new ProductionStepController();
        m_steps_controller.setListener( this );
        m_steps_controller.setErrorListener( this );

        // Fill operator list
        m_list_view = (ListView) view.findViewById(R.id.list_patients);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_NONE);
        m_list_view.clearChoices();

        List<Patient> l_patients = new ArrayList<>();
        PatientAdapter l_adapter = new PatientAdapter( getContext(), l_patients );
        m_list_view.setAdapter( l_adapter );

        m_spinner_steps = (Spinner)view.findViewById(R.id.pat_spinner_steps);
        m_spinner_steps.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String l_step = m_spinner_steps.getSelectedItem().toString();
                m_steps_controller.findStepOrder( getContext(), l_step );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                m_patient_controller.fetchOnDB( getContext() );
            }
        });

        m_loading = ProgressDialog.show( getActivity(),
                getString(R.string.load_wait),
                getString(R.string.load_load_patients),
                false, false );

        m_patient_controller.fetchOnDB( getContext() );
        m_steps_controller.fetchOnDB( getContext() );

        return view;
    }

    @Override
    public void setPatients(ArrayList<Patient> a_patients) {
        PatientAdapter l_adapter = (PatientAdapter)m_list_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( a_patients );
        l_adapter.notifyDataSetChanged();

        m_loading.dismiss();
        m_list_view.smoothScrollToPosition(0);
    }

    @Override public void onPatientFound(String a_patient_name) {}
    @Override public void onProductionStepUpdated( boolean a_ok ){}

    @Override
    public void onError(String response) {
        Snackbar.make( getView(), response, Snackbar.LENGTH_LONG ).show();

        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }

    @Override
    public void setSteps(ArrayList<String> a_steps) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, a_steps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner_steps.setAdapter(adapter);
    }
    @Override public void onStepCreated(boolean l_ok) {}
    @Override public void onStepDeleted(boolean l_ok) {}

    @Override
    public void onStepOrderFound(String a_order) {
        m_patient_controller.findPatientProd( getContext(), a_order );
    }
}
