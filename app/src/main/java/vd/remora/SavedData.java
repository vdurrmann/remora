package vd.remora;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SavedData {
    protected ArrayList<String> m_operators;
    protected ArrayList<String> m_steps;

    public void setOperators( ArrayList<String> a_operators ){
        m_operators = a_operators;
    }

    public ArrayList<String> operators(){
        return m_operators;
    }

    public void setSteps( ArrayList<String> a_steps ){
        m_steps = a_steps;
    }

    public ArrayList<String> steps(){
        return m_steps;
    }
}
