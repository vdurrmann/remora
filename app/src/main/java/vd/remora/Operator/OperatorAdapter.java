package vd.remora.Operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import vd.remora.R;

public class OperatorAdapter extends ArrayAdapter<Operator> {

    int m_selected_item;

    public OperatorAdapter(Context context, List<Operator> a_operators) {
        super(context, 0, a_operators);
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

        boolean l_selected = m_selected_item == position;
        viewHolder.btn_delete.setVisibility( l_selected ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private class OperatorViewHolder{
        public TextView name;
        public Button btn_delete;
    }

}
