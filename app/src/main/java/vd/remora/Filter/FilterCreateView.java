package vd.remora.Filter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import vd.remora.ProductionStep.ProductionStep;
import vd.remora.R;

public class FilterCreateView extends RelativeLayout {

    // Own widgets
    private EditText m_txt_name;
    private Button m_btn_date_start;
    private Button m_btn_date_end;
    private ListView m_steps_view;

    // Attribute to manage data choice
    private GregorianCalendar m_date_start;
    private GregorianCalendar m_date_end;

    public FilterCreateView(Context context) {
        super(context);
        this._init();
    }

    /** Set available production steps for step spinner **/
    public void setSteps( ArrayList<String> a_steps ){
        FilterCreateSelectStepsAdapter l_adapter = (FilterCreateSelectStepsAdapter) m_steps_view.getAdapter();
        l_adapter.clear();
        l_adapter.addAll( a_steps );
        l_adapter.notifyDataSetChanged();
    }

    public String name(){
        return m_txt_name.getText().toString();
    }

    /** Return yyyy-mm-dd date */
    public String dateStart(){
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return l_format.format( m_date_start.getTimeInMillis() );
    }

    public String dateEnd(){
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return l_format.format( m_date_end.getTimeInMillis() );
    }


    protected void _init(){
        inflate(getContext(), R.layout.filter_create, this);

        m_txt_name = (EditText)findViewById( R.id.filter_create_name );

        m_date_start = new GregorianCalendar();
        m_date_end = new GregorianCalendar();
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String l_date_now = l_format.format( m_date_start.getTimeInMillis() );

        m_btn_date_start = (Button)findViewById( R.id.filter_create_btn_start_date);
        m_btn_date_start.setText( l_date_now );
        m_btn_date_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog l_picker = new DatePickerDialog(getContext());
                    l_picker.getDatePicker().setMaxDate( m_date_end.getTimeInMillis() );
                    l_picker.setOnDateSetListener( new DatePickerListener(m_btn_date_start, m_date_start) );
                    l_picker.show();
                }
            }
        });

        m_btn_date_end = (Button)findViewById( R.id.filter_create_btn_end_date );
        m_btn_date_end.setText( l_date_now );
        m_btn_date_end.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog l_picker = new DatePickerDialog(getContext());
                    l_picker.getDatePicker().setMinDate( m_date_start.getTimeInMillis() );
                    l_picker.setOnDateSetListener(new DatePickerListener(m_btn_date_end, m_date_end) );
                    l_picker.show();
                }
            }
        });

        m_steps_view = (ListView) findViewById( R.id.filter_create_step_list );
        List<ProductionStep> l_steps = new ArrayList<>();
        FilterCreateSelectStepsAdapter l_adapter = new FilterCreateSelectStepsAdapter( getContext(), l_steps );
        m_steps_view.setAdapter( l_adapter );
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        private Button m_btn_date;
        private GregorianCalendar m_date;

        public DatePickerListener( Button a_btn_date, GregorianCalendar a_date ){
            m_btn_date = a_btn_date;
            m_date = a_date;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String l_date = Integer.toString(year)
                    + "-" + Integer.toString(month)
                    + "-" + Integer.toString(dayOfMonth);
            m_btn_date.setText( l_date );
            m_date.set(year, month, dayOfMonth);
        }
    }
}
