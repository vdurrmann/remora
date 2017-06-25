package vd.remora.Patient;

public class Patient {

    String m_folder;
    String m_name;
    String m_firstname;
    String m_production_step;
    String m_date;

    public Patient( String a_folder,
                    String a_name,
                    String a_firstname,
                    String a_step,
                    String a_date)
    {
        m_folder = a_folder;
        m_name = a_name;
        m_firstname = a_firstname;
        m_production_step = a_step;
        m_date = a_date;
    }

    void setFolder( String a_folder ){
        m_folder = a_folder;
    }

    String folder(){
        return m_folder;
    }

    void setName( String a_name ){
        m_name = a_name;
    }

    String name(){
        return m_name;
    }

    void setFirstName( String a_firstname ){
        m_firstname = a_firstname;
    }

    String firstName(){
        return m_firstname;
    }

    void setStep( String a_step ){
        m_production_step = a_step;
    }

    String productionStep(){
        return m_production_step;
    }

    void setDate( String a_date ){
        m_date = a_date;
    }

    String date(){
        return m_date;
    }


    static String formatFolder( String a_folder ){
        try{
            Integer.parseInt(a_folder);//test it is an integer
            int length = a_folder.length();
            switch(length){
                case 1 : a_folder = "000"+a_folder;
                    break;
                case 2 : a_folder = "00"+a_folder;
                    break;
                case 3 : a_folder = "0"+a_folder;
                    break;
                default :break;
            }
        }
        catch(NumberFormatException e){}
        return a_folder;
    }

}
