package vd.remora.Operator;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import vd.remora.R;

public class OperatorAdapter extends ArrayAdapter<Operator> {

    private int m_selected_item;
    private OperatorController m_operator_controller = null;

    public OperatorAdapter(Context context, List<Operator> a_operators, OperatorController a_operator_controller ) {
        super(context, 0, a_operators);
        m_operator_controller = a_operator_controller;
    }

    public void setSelectedItem( int a_selected_item ){
        m_selected_item = a_selected_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.operator_row,parent, false);
        }

        OperatorViewHolder viewHolder = (OperatorViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new OperatorViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.operator_name);
            viewHolder.btn_delete= (Button) convertView.findViewById(R.id.operator_btn_delete);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Operator>
        Operator operator = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText( operator.getName() );
        viewHolder.btn_delete.setOnClickListener( new DeleteClickListener( operator.getName() ) );

        boolean l_selected = m_selected_item == position;
        viewHolder.btn_delete.setVisibility( l_selected ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private class OperatorViewHolder{
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
            builder.setTitle( getContext().getString(R.string.ope_delete_title) );

            // Set up the input
            final TextView l_txt = new TextView( getContext() );
            l_txt.setGravity(Gravity.CENTER);
            l_txt.setText( getContext().getString( R.string.ope_delete_instr, m_name) );
            builder.setView( l_txt );

            // Set up the buttons
            builder.setPositiveButton( getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_operator_controller.deleteOperator( getContext(), m_name );
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
