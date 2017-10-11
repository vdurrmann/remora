package vd.remora;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vd.remora.History.History;
import vd.remora.History.HistoryController;
import vd.remora.History.HistoryListenerInterface;
import vd.remora.Operator.OperatorController;
import vd.remora.Operator.OperatorListenerInterface;
import vd.remora.ProductionStep.ProductionStepListenerInterface;
import vd.remora.Patient.Patient;
import vd.remora.Patient.PatientController;
import vd.remora.Patient.PatientListenerInterface;
import vd.remora.ProductionStep.ProductionStepController;

public class AddProductionFragment extends Fragment
        implements OperatorListenerInterface,
        PatientListenerInterface,
        ProductionStepListenerInterface,
        HistoryListenerInterface,
        DataBaseErrorListener
{

    private ProgressDialog m_loading;
    // Widgets
    Button m_btn_validate;
    Spinner m_spinner_operator;
    Spinner m_spinner_steps;
    EditText m_txt_folder;
    TextView m_txt_patient;
    TableLayout m_table_history;

    //Data
    private PatientController m_patient_controller = null;
    private OperatorController m_operator_controller = null;
    private ProductionStepController m_steps_controller = null;
    private HistoryController m_history_controller = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prod, container, false);

        // Controllers
        m_patient_controller = new PatientController();
        m_patient_controller.setListener( this );
        m_patient_controller.setErrorListener( this );

        m_operator_controller = new OperatorController();
        m_operator_controller.setListener( this );
        m_operator_controller.setErrorListener( this );

        m_steps_controller = new ProductionStepController();
        m_steps_controller.setListener( this );
        m_steps_controller.setErrorListener( this );

        m_history_controller = new HistoryController();
        m_history_controller.setListener( this );
        m_history_controller.setErrorListener( this );

        // UI
        m_spinner_operator = (Spinner) view.findViewById( R.id.spinner_operator);
        m_spinner_steps = (Spinner) view.findViewById( R.id.spinner_step );

        m_txt_patient = (TextView) view.findViewById( R.id.txt_patient_name );

        m_txt_folder = (EditText) view.findViewById( R.id.folder );
        m_txt_folder.addTextChangedListener( folderTextWatcher );

        m_btn_validate = (Button) view.findViewById( R.id.btn_validate );
        m_btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Update production step
                String l_operator = m_spinner_operator.getSelectedItem().toString();
                String l_step = m_spinner_steps.getSelectedItem().toString();
                String l_folder = m_txt_folder.getText().toString();

                m_patient_controller.updatePatientProductionStep(
                        getContext(),
                        l_folder,
                        l_operator,
                        l_step
                );
            }
        });

        m_table_history = (TableLayout) view.findViewById( R.id.valid_step_history_array );

        // Fill UI
        if( savedInstanceState == null ) {
            m_loading = ProgressDialog.show( getActivity(), "Please wait...", "Updating data...", false, false );
            m_operator_controller.fetchOnDB(getContext());
            m_steps_controller.fetchOnDB(getContext());
            m_history_controller.fetchoOnDB(getContext(),5);
        }
        else{
            this.updateList( savedInstanceState.getStringArrayList("operators"), m_spinner_operator );
            m_spinner_operator.setSelection( savedInstanceState.getInt("operator_selected") );

            this.updateList( savedInstanceState.getStringArrayList("steps"), m_spinner_steps);
            m_spinner_steps.setSelection( savedInstanceState.getInt("step_selected") );

            this.setHistory(savedInstanceState.<History>getParcelableArrayList("history"));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState( Bundle outState ){
        super.onSaveInstanceState(outState);

        // Save operators
        ArrayList<String> l_operators = new ArrayList<>();
        int l_nb_operators = m_spinner_operator.getAdapter().getCount();
        for(int i = 0; i < l_nb_operators; ++i ){
            l_operators.add(m_spinner_operator.getItemAtPosition(i).toString());
        }
        outState.putStringArrayList( "operators", l_operators);
        outState.putInt("operator_selected", m_spinner_operator.getSelectedItemPosition());

        // Save steps
        ArrayList<String> l_steps = new ArrayList<>();
        int l_nb_steps = m_spinner_steps.getAdapter().getCount();
        for(int i = 0; i < l_nb_steps; ++i ){
            l_steps.add( m_spinner_steps.getItemAtPosition(i).toString() );
        }
        outState.putStringArrayList( "steps", l_steps);
        outState.putInt("step_selected", m_spinner_steps.getSelectedItemPosition());

        outState.putParcelableArrayList("history", m_history_controller.histories());
    }

    protected void updateList( ArrayList<String> a_list, Spinner a_spinner ){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, a_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a_spinner.setAdapter(adapter);
    }

    private void _setValidateEnable( boolean a_enable ){
        m_btn_validate.setEnabled(a_enable);
    }

    // Text watcher on patient folder entries
    private TextWatcher folderTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if( s.toString().length() > 0 ) {
                m_patient_controller.findPatient( getContext(), m_txt_folder.getText().toString() );
            }
            else{
                onPatientFound("");
            }
        }
    };

    private void _updatePatientName( String a_name ){
        m_txt_patient.setText( a_name );
    }

    @Override
    public void setOperators(ArrayList<String> a_operators) {
        this.updateList( a_operators, m_spinner_operator );
        m_loading.dismiss();
    }

    @Override public void onOperatorCreated(boolean a_created) {}
    @Override public void onOperatorDeleted(boolean a_deleted) {}

    @Override
    public void setSteps(ArrayList<String> a_steps) {
        this.updateList( a_steps, m_spinner_steps );
        m_loading.dismiss();
    }

    @Override public void onStepCreated(boolean l_ok) {}
    @Override public void onStepDeleted(boolean l_ok) {}
    @Override public void onStepOrderFound(String a_order) {}

    @Override public void setPatients(ArrayList<Patient> a_patients) {}

    @Override
    public void onPatientFound(String a_patient_name) {
        this._updatePatientName( a_patient_name );
        this._setValidateEnable( a_patient_name.compareTo("") != 0 );
    }

    @Override
    public void onProductionStepUpdated( boolean a_ok ){
        String l_txt = a_ok ? getString(R.string.prod_updated_ok) : getString(R.string.an_error_occured);
        Toast.makeText( getContext(), l_txt, Toast.LENGTH_LONG ).show();

        if( a_ok ){
            // Update history table
            m_history_controller.fetchoOnDB(getContext(), 5);

            // Reset UI
            m_txt_folder.setText("");
        }
    }

    @Override
    public void onError(String response) {
        Toast.makeText( getContext(), response, Toast.LENGTH_LONG ).show();

        Intent l_intent = new Intent( getActivity(), PreferenceActivity.class );
        startActivity( l_intent );
        this.getActivity().finish();
    }

    private TextView _createTextView( String a_txt ){
        TextView l_date = new TextView(getContext());
        l_date.setText( a_txt );
        return l_date;
    }

    @Override
    public void setHistory(ArrayList<History> a_histories) {
        m_table_history.removeAllViews();

        for(int i = 0; i < a_histories.size(); ++i ){
            History l_history = a_histories.get(i);

            String l_patient = "";
            l_patient += l_history.folder();
            l_patient += " - ";
            l_patient += l_history.patientName();
            l_patient += " ";
            l_patient += l_history.patientFirstName();

            TableRow l_row = new TableRow(getContext());
            l_row.addView( _createTextView(l_patient) );
            l_row.addView( _createTextView(l_history.productionStep()) );
            l_row.addView( _createTextView(l_history.operator()) );

            m_table_history.setColumnStretchable(0, true);
            m_table_history.setColumnStretchable(1, true);
            m_table_history.setColumnStretchable(2, true);
            m_table_history.setColumnStretchable(3, true);
            m_table_history.addView(l_row);
        }
    }
}
