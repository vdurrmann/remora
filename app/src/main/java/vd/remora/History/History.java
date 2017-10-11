package vd.remora.History;

public class History {

    private String m_date;
    private String m_folder;
    private String m_operator_name;
    private String m_production_step;

    public History( String a_date, String a_folder, String a_operator, String a_step ){
        m_date = a_date;
        m_folder = a_folder;
        m_operator_name = a_operator;
        m_production_step = a_step;
    }

    public String date(){
        return m_date;
    }

    public String folder(){
        return m_folder;
    }

    public String operator(){
        return m_operator_name;
    }

    public String productionStep(){
        return m_production_step;
    }


}
