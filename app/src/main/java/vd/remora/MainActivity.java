package vd.remora;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HashMap<Integer, Fragment> m_fragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // On first instance creation
        // Create only once fragments
        if( savedInstanceState == null ){
            m_fragments.put(R.id.nav_add_prod, new AddProductionFragment());

            // Select default Fragment
            MenuItem l_default_item = navigationView.getMenu().getItem(0);
            l_default_item.setChecked(true);
            this.onNavigationItemSelected( l_default_item );
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Choose right Fragment and actionbar title
        Fragment newFragment = m_fragments.get(id);
        String l_title = getSupportActionBar().getTitle().toString();

        if (id == R.id.nav_add_prod) {
            l_title = getString( R.string.nav_add_prod );
        }
        /*else if (id == R.id.nav_operators) {
            l_title = getString( R.string.nav_operators );
            newFragment = new OperatorsFragment();
        }
        else if( id == R.id.nav_prod_steps ){
            l_title = getString(R.string.nav_prod_step);
            newFragment = new ProductionStepFragment();
        }
        else if (id == R.id.nav_patients) {
            l_title = getString( R.string.nav_patients );
            newFragment = new PatientFragment();
        }*/
        else if( id == R.id.nav_settings ){
            Intent l_intent = new Intent( this, PreferenceActivity.class );
            startActivity( l_intent );
        }

        // change Fragment
        if( newFragment != null ) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.content_main, newFragment);
            //transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }

        // Change actionbar title
        if( getSupportActionBar() != null ){
            getSupportActionBar().setTitle( l_title );;
        }

        // Close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}