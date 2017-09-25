package vd.remora.Filter;

import java.util.ArrayList;

public interface FilterListenerInterface {

    void setFilters( ArrayList<Filter> a_steps );

    void onFilterCreated( boolean l_ok );

    void onFilterDeleted( boolean l_ok );

}
