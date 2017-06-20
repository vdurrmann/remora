package vd.remora;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import vd.remora.Operator.OperatorController;
import vd.remora.Operator.OperatorErrorListener;
import vd.remora.Operator.OperatorListenerInterface;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OperatorErrorListener{

    //Data
    protected OperatorController m_operator_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main );

        // Get data from database
        m_operator_controller = new OperatorController();
        m_operator_controller.setErrorListener( this );
        fetchInDB();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Select default Fragment
        MenuItem l_default_item = navigationView.getMenu().getItem(0);
        l_default_item.setChecked(true);
        this.onNavigationItemSelected( l_default_item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Choose right Fragment and actionbar title
        Fragment newFragment = null;
        String l_title = getSupportActionBar().getTitle().toString();

        if (id == R.id.nav_add_prod) {
            l_title = getString( R.string.nav_add_prod );
            newFragment = new AddProductionFragment();

            m_operator_controller.setListener( (OperatorListenerInterface)newFragment );
            m_operator_controller.fetchOnDB( getApplicationContext() );

        } else if (id == R.id.nav_operators) {
            l_title = getString( R.string.nav_operators );
            newFragment = new OperatorsFragment();

            m_operator_controller.setListener( (OperatorListenerInterface)newFragment );
            m_operator_controller.fetchOnDB( getApplicationContext() );

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

    private void fetchInDB(){
        m_operator_controller.fetchOnDB( getApplicationContext() );
    }

    @Override
    public void onError(String response) {
        Intent l_intent = new Intent( this, PreferenceActivity.class );
        startActivity( l_intent );
        this.finish();
    }
}