package vd.remora.ProductionStep;

import android.content.Context;
import android.content.DialogInterface;
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

    private int m_selected_item;
    private ProductionStepController m_steps_controller = null;

    public ProductionStepAdapter(Context context, List<ProductionStep> a_steps, ProductionStepController a_step_controller ) {
        super(context, 0, a_steps);
        m_steps_controller = a_step_controller;
    }

    public void setSelectedItem( int a_selected_item ){
        m_selected_item = a_selected_item;
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
            viewHolder.btn_delete = (Button)convertView.findViewById(R.id.prod_btn_delete);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Operator>
        ProductionStep l_step = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText( l_step.getName() );
        viewHolder.btn_delete.setOnClickListener( new DeleteClickListener( l_step.getName() ) );

        boolean l_selected = m_selected_item == position;
        viewHolder.btn_delete.setVisibility( l_selected ? View.VISIBLE : View.GONE);

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
        Button btn_delete;
    }


    private class DeleteClickListener implements View.OnClickListener {
        private String m_name;

        private DeleteClickListener( String a_name ){
            m_name = a_name;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
            builder.setTitle( getContext().getString(R.string.step_delete_title) );

            // Set up the input
            final TextView l_txt = new TextView( getContext() );
            l_txt.setGravity(Gravity.CENTER);
            l_txt.setText( getContext().getString( R.string.step_delete_insctr, m_name) );
            builder.setView( l_txt );

            // Set up the buttons
            builder.setPositiveButton( getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_steps_controller.deleteStep( getContext(), m_name );
                }
            });
            builder.setNegativeButton( getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

}
