package vd.remora.ProductionStep;

import java.util.ArrayList;

public interface ProductionStepListenerInterface {

    void setSteps( ArrayList<String> a_steps );

    void onStepCreated( boolean l_ok );

    void onStepDeleted( boolean l_ok );

}
