package vd.remora.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vd.remora.R;


public class PatientAdapter extends ArrayAdapter<Patient> {

    public PatientAdapter(Context context, List<Patient> a_patients) {
        super(context, 0, a_patients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.patient_row,parent, false);
        }

        PatientAdapter.PatientViewHolder viewHolder = (PatientAdapter.PatientViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new PatientAdapter.PatientViewHolder();
            viewHolder.folder = (TextView) convertView.findViewById(R.id.patient_folder);
            viewHolder.name = (TextView) convertView.findViewById(R.id.patient_name);
            viewHolder.firstname = (TextView) convertView.findViewById(R.id.patient_firstname);
            viewHolder.step = (TextView) convertView.findViewById(R.id.patient_step);
            viewHolder.date = (TextView) convertView.findViewById(R.id.patient_date);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Patient>
        Patient l_patient = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.folder.setText( l_patient.folder() );
        viewHolder.name.setText( l_patient.name() );
        viewHolder.firstname.setText( l_patient.firstName() );
        viewHolder.step.setText( l_patient.productionStep() );
        viewHolder.date.setText( l_patient.date() );

        return convertView;
    }

    private class PatientViewHolder{
        public TextView folder;
        public TextView name;
        public TextView firstname;
        public TextView step;
        public TextView date;
    }

}
