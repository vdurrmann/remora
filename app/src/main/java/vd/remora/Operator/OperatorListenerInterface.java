package vd.remora.Operator;

import java.util.ArrayList;

public interface OperatorListenerInterface {

    void setOperators(ArrayList<String> a_operators );

    void onOperatorCreated( boolean a_created );
    void onOperatorDeleted( boolean a_deleted );

}
