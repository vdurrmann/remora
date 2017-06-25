package vd.remora.Operator;

public class Operator {
    String m_name = "";

    public Operator( String a_name ){
        m_name = a_name;
    }

    void setName( String a_name ){
        m_name = a_name;
    }

    String getName(){
        return m_name;
    }

}
