package vd.remora;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.InputStream;
import java.util.GregorianCalendar;

class DBScripts {

    private static final String PHP_INSERT_PROD = "db_rfid_insert_prod.php";
    private static final String PHP_ALL_CARDS = "db_rfid_all_cards.php";
    private static final String PHP_FIND_PATIENT = "db_rfid_find_patient.php";

    // JSON type found in response
    static final String JSON_ARRAY = "result";
    static final String KEY_CARD_TYPE = "type";
    static final String KEY_CARD_NAME = "name";
    static final String KEY_PATIENT_NAME = "name";
    static final String KEY_PATIENT_SURNAME = "surname";


    private String m_server;
    private String m_username;
    private String m_password;
    private String m_database;

    DBScripts(Context a_context){
        SharedPreferences l_preferences = PreferenceManager.getDefaultSharedPreferences( a_context.getApplicationContext() );
        m_server = l_preferences.getString( "pref_server", "" );
        m_username = l_preferences.getString( "pref_username", "" );
        m_password = l_preferences.getString( "pref_password", "" );
        m_database = l_preferences.getString( "pref_database", "" );
    }

    /**
     * Create URL to get all cards list
     */
    String createAllCardsURL(){
        String l_url = _addDBConnectionData( PHP_ALL_CARDS );
        return l_url;
    }

    /**
     * Create URL to insert a production into database
     * @param a_operator Operator name
     * @param a_step Step name
     * @param a_folder Patient folder
     * @return URL
     */
    String createInsertProdURL(String a_operator, String a_step, String a_folder){
        // Get now data
        GregorianCalendar l_now = new GregorianCalendar();
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String l_date = l_format.format( l_now.getTimeInMillis() );

        // Create URL
        String l_url = _addDBConnectionData( PHP_INSERT_PROD );
        l_url += "&date=" + l_date;
        l_url += "&operator=" + a_operator;
        l_url += "&step=" + a_step;
        l_url += "&folder=" + a_folder;
        return l_url;
    }

    String createFindPatientURL( String a_folder ){
        String l_url = _addDBConnectionData( PHP_FIND_PATIENT );
        l_url += "&folder=" + a_folder;
        return l_url;
    }

    /**
     * Create URL script with database connection values
     * @param a_script_name Script name to call
     * @return URL
     */
    private String _addDBConnectionData(String a_script_name){
        String l_url = "http://" + m_server + "/android_script/" + a_script_name + "?";

        l_url += "user=" + m_username;
        l_url += "&password=" + m_password;
        l_url += "&server=" + m_server;
        l_url += "&database=" + m_database;
        return l_url;
    }
}
