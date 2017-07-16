package vd.remora.Filter;

import java.util.Vector;

public class Filter {

    private String m_name;
    private String m_date_start;
    private String m_date_end;
    private Vector<String> m_vec_steps;

    public Filter(){
        m_vec_steps = new Vector<>();
    }

    public void setName( String a_name ){
        m_name = a_name;
    }

    public String name(){
        return m_name;
    }

    public void setDateStart( String a_date ){
        m_date_start = a_date;
    }

    public String dateStart(){
        return m_date_start;
    }

    public void setDateEnd( String a_date ){
        m_date_end = a_date;
    }

    public String dateEnd(){
        return m_date_end;
    }

    public void setSteps( Vector<String> a_steps ){
        m_vec_steps = a_steps;
    }

    public Vector<String> steps(){
        return m_vec_steps;
    }

}
