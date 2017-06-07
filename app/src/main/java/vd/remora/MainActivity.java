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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Data
    protected ArrayList<String> m_operators = new ArrayList<>();
    protected ArrayList<String> m_steps = new ArrayList<>();

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

        // Get data from intent
        m_operators = getIntent().getStringArrayListExtra( "Operators" );
        m_steps = getIntent().getStringArrayListExtra( "Steps" );

        // Select default Fragment
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_main, new AddProductionFragment());
        tx.commit();
        getSupportActionBar().setTitle( getString(R.string.nav_add_prod) );;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Choose right Fragment and actionbar title
        Fragment newFragment = null;
        String l_title = "";

        if (id == R.id.nav_add_prod) {
            newFragment = new AddProductionFragment();
            l_title = getString( R.string.nav_add_prod );

        } else if (id == R.id.nav_operators) {
            newFragment = new OperatorsFragment();
            l_title = getString( R.string.nav_operators );
        } else if( id == R.id.nav_settings ){
            Intent l_intent = new Intent( this, PreferenceActivity.class );
            startActivity( l_intent );
        }

        // change Fragment
        if( newFragment != null ) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.content_main, newFragment);
            transaction.addToBackStack(null);
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

    public ArrayList<String> getOperators(){
        return m_operators;
    }

    public ArrayList<String> getSteps(){
        return m_steps;
    }

}