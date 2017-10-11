package vd.remora.History;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable{

    private String m_date;
    private String m_folder;
    private String m_operator_name;
    private String m_production_step;
    private String m_patient_name;
    private String m_patient_firstname;

    public History( String a_date, String a_folder, String a_operator, String a_step, String a_patient_name, String a_patient_firstname ){
        m_date = a_date;
        m_folder = a_folder;
        m_operator_name = a_operator;
        m_production_step = a_step;
        m_patient_name = a_patient_name;
        m_patient_firstname = a_patient_firstname;
    }

    private History(Parcel in) {
        m_date = in.readString();
        m_folder = in.readString();
        m_patient_name = in.readString();
        m_patient_firstname = in.readString();
        m_production_step = in.readString();
        m_operator_name = in.readString();
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

    public String patientName(){
        return m_patient_name;
    }

    public String patientFirstName(){
        return m_patient_firstname;
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( date() );
        dest.writeString( folder() );
        dest.writeString( patientName() );
        dest.writeString( patientFirstName() );
        dest.writeString( productionStep() );
        dest.writeString( operator() );
    }

    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
