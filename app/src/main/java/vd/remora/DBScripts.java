package vd.remora;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.InputStream;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

public class DBScripts {

    private static final String PHP_TEST_CONNECTION = "db_test_connect.php";

    private static final String PHP_INSERT_PROD = "db_rfid_insert_prod.php";
    private static final String PHP_SELECT_HISTORY = "db_rfid_select_history.php";

    private static final String PHP_ALL_CARDS = "db_rfid_all_cards.php";
    private static final String PHP_FIND_PATIENT = "db_rfid_find_patient.php";
    private static final String PHP_SELECT_PATIENT = "db_rfid_select_patient.php";
    private static final String PHP_SELECT_PATIENT_PROD = "db_rfid_select_patient_prod.php";

    private static final String PHP_INSERT_OPERATOR = "db_rfid_insert_operator.php";
    private static final String PHP_DELETE_OPERATOR = "db_rfid_delete_operator.php";

    private static final String PHP_ALL_STEPS = "db_rfid_all_steps.php";
    private static final String PHP_FIND_STEP_ORDER = "db_rfid_find_step_order.php";
    private static final String PHP_INSERT_STEPS = "db_rfid_insert_steps.php";
    private static final String PHP_DELETE_STEPS = "db_rfid_delete_steps.php";

    // JSON type found in response
    public static final String JSON_ARRAY = "result";
    public static final String KEY_CARD_TYPE = "type";
    public static final String KEY_CARD_NAME = "name";
    public static final String KEY_PATIENT_NAME = "name";
    public static final String KEY_PATIENT_FIRSTNAME = "surname";
    public static final String KEY_PATIENT_FOLDER = "folder";
    public static final String KEY_PATIENT_STEP = "step";
    public static final String KEY_PATIENT_DATE = "date";

    public static final String KEY_STEP_NAME = "name";
    public static final String KEY_STEP_ORDER = "order";

    public static final String KEY_HISTORY_DATE = "date";
    public static final String KEY_HISTORY_FOLDER = "folder";
    public static final String KEY_HISTORY_OPERATOR = "operator";
    public static final String KEY_HISTORY_STEP = "step";


    private String m_server;
    private String m_username;
    private String m_password;
    private String m_database;

    public DBScripts(SharedPreferences a_preferences){
        m_server = a_preferences.getString( "pref_server", "" );
        m_username = a_preferences.getString( "pref_username", "" );
        m_password = a_preferences.getString( "pref_password", "" );
        m_database = "db_rfid"; //a_preferences.getString( "pref_database", "" );
    }

    public String testDBConnection(){
        return _addDBConnectionData( PHP_TEST_CONNECTION );
    }

    /**
     * Create URL to get all cards list
     */
    public String createAllCardsURL(){
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
    public String insertProductionURL(String a_operator, String a_step, String a_folder){
        // Get now data
        GregorianCalendar l_now = new GregorianCalendar();
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String l_date = l_format.format( l_now.getTimeInMillis() );

        // Create URL
        String l_url = _addDBConnectionData( PHP_INSERT_PROD );
        l_url += "&date=" + l_date;
        l_url += "&folder=" + a_folder;
        l_url += "&operator=" + a_operator;
        l_url += "&step=" + a_step;
        return l_url;
    }

    public String selectHistory( int a_nb_history ){
        String l_url = _addDBConnectionData( PHP_SELECT_HISTORY );
        l_url += "&limit=" + Integer.toString(a_nb_history);
        return l_url;
    }

    public String createInsertOperatorURL(String a_name){
        GregorianCalendar l_now = new GregorianCalendar();
        java.text.SimpleDateFormat l_format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String l_date = l_format.format( l_now.getTimeInMillis() );

        // Create URL
        String l_url = _addDBConnectionData( PHP_INSERT_OPERATOR );
        l_url += "&name=" + a_name;
        l_url += "&id=" + l_date;
        return l_url;
    }

    public String deleteOperatorURL( String a_operator ){
        String l_url = _addDBConnectionData( PHP_DELETE_OPERATOR );
        l_url += "&name=" + a_operator;
        return l_url;
    }

    public String allStepsURL(){
        String l_url = _addDBConnectionData( PHP_ALL_STEPS );
        return l_url;
    }

    public String findStepOrderURL( String a_step_name ){
        String l_url = _addDBConnectionData( PHP_FIND_STEP_ORDER );
        l_url+="&step="+a_step_name;
        return l_url;
    }

    public String insertStepURL( String a_step_name ){
        String l_url = _addDBConnectionData( PHP_INSERT_STEPS );
        l_url += "&name=" + a_step_name;
        return l_url;
    }

    public String deleteStepURL( String a_step_name ){
        String l_url = _addDBConnectionData( PHP_DELETE_STEPS );
        l_url += "&name=" + a_step_name;
        return l_url;
    }



    public String findPatientURL(String a_folder){
        String l_url = _addDBConnectionData( PHP_FIND_PATIENT );
        l_url += "&folder=" + a_folder;
        return l_url;
    }

    public String selectPatientProdURL(String a_step_order){
        String l_url = _addDBConnectionData( PHP_SELECT_PATIENT_PROD );
        l_url+="&step_order="+a_step_order;
        return l_url;
    }

    public String createSelectPatientURL(){
        String l_url = _addDBConnectionData( PHP_SELECT_PATIENT );
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
