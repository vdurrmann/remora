package vd.remora.Filter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.GregorianCalendar;

import vd.remora.R;

public class FilterCreateView extends RelativeLayout {

    private EditText m_txt_name;
    private Button m_btn_date_start;
    private Button m_btn_date_end;

    private GregorianCalendar m_date_start;
    private GregorianCalendar m_date_end;

    public FilterCreateView(Context context) {
        super(context);
        this._init();
    }

    public FilterCreateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._init();
    }

    public FilterCreateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._init();
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
