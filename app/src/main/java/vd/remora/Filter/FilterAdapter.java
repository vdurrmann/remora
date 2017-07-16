package vd.remora.Filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import vd.remora.R;

public class FilterAdapter extends ArrayAdapter<Filter> {

    public FilterAdapter(Context context, List<Filter> a_patients) {
        super(context, 0, a_patients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO layout_rox
        /*if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.filter_create,parent, false);
        }*/

        FilterViewHolder viewHolder = (FilterViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FilterViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.filter_row_name);
            /*viewHolder.date_start = (TextView) convertView.findViewById(R.id.filter_row_date_start);
            viewHolder.data_end = (TextView) convertView.findViewById(R.id.filter_row_date_end);
            viewHolder.steps = (Spinner) convertView.findViewById(R.id.filter_row_steps);*/
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Patient>
        Filter l_filter = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText( l_filter.name() );
        viewHolder.date_start.setText( l_filter.dateStart() );
        viewHolder.data_end.setText( l_filter.dateEnd() );
        //TODO viewHolder.steps.set( l_patient.date() );

        return convertView;
    }

    private class FilterViewHolder{
        public TextView name;
        public TextView date_start;
        public TextView data_end;
        public Spinner steps;
    }
}
