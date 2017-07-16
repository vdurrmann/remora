package vd.remora.Filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.ProductionStep.ProductionStep;
import vd.remora.R;

public class FilterCreateSelectStepsAdapter extends ArrayAdapter<ProductionStep> {

    public FilterCreateSelectStepsAdapter(Context context, List<ProductionStep> a_steps ) {
        super(context, 0, a_steps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.filter_create_steps_row,parent, false);
        }

        SelectStepViewHolder viewHolder = (SelectStepViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SelectStepViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.filterCreateStepsRow_text);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.filterCreateStepsRow_checkbox);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Operator>
        ProductionStep l_step = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText( l_step.getName() );

        return convertView;
    }

    public void addAll(ArrayList<String> a_steps) {
        List<ProductionStep> l_steps = new ArrayList<>();
        for (String name : a_steps ){
            l_steps.add( new ProductionStep(name) );
        }
        this.addAll(l_steps);
    }

    private class SelectStepViewHolder{
        CheckBox checkbox;
        TextView name;
    }

}
