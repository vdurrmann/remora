package vd.remora.ProductionStep;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vd.remora.Operator.OperatorAdapter;
import vd.remora.R;

public class ProductionStepAdapter extends ArrayAdapter<ProductionStep> {

    int m_selected_id;

    public ProductionStepAdapter(Context context, List<ProductionStep> a_steps, ProductionStepController a_step_controller ) {
        super(context, 0, a_steps);
    }

    public void setSelectedId( int a_id ){
        m_selected_id = a_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.production_step_row,parent, false);
        }

        ProductionStepViewHolder viewHolder = (ProductionStepViewHolder)convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ProductionStepViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.prod_name);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Operator>
        ProductionStep l_step = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText( l_step.getName() );

        // Update background color
        if( position == m_selected_id ){
            convertView.setBackgroundColor(Color.LTGRAY);
        }
        else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    public void addAll(ArrayList<String> a_steps) {
        List<ProductionStep> l_steps = new ArrayList<>();
        for (String name : a_steps ){
            l_steps.add( new ProductionStep(name) );
        }
        this.addAll(l_steps);
    }

    private class ProductionStepViewHolder{
        TextView name;
    }

}
