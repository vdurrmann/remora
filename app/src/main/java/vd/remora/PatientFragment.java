package vd.remora;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Patient.Patient;
import vd.remora.Patient.PatientAdapter;
import vd.remora.Patient.PatientListenerInterface;

public class PatientFragment extends Fragment implements PatientListenerInterface{

    ProgressDialog m_loading;
    ListView m_list_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        // Fill operator list
        m_list_view = (ListView) view.findViewById(R.id.list_patients);
        m_list_view.setChoiceMode(ListView.CHOICE_MODE_NONE);
        m_list_view.clearChoices();

        List<Patient> l_patients = new ArrayList<>();
        PatientAdapter l_adapter = new PatientAdapter( getContext(), l_patients );
        m_list_view.setAdapter( l_adapter );

        m_loading = ProgressDialog.show( getActivity(),
                getString(R.string.load_wait),
                getString(R.string.load_load_patients),
                false, false );

        return view;
    }

    @Override
    public void setPatients(ArrayList<Patient> a_patients) {
        PatientAdapter l_adapter = (PatientAdapter)m_list_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( a_patients );
        l_adapter.notifyDataSetChanged();

        m_loading.dismiss();
    }
}
