package com.kayzr.kayzrstaff;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kayzr.kayzrstaff.domain.KayzrApp;
import com.kayzr.kayzrstaff.domain.Tournament;
import com.kayzr.kayzrstaff.domain.User;
import com.kayzr.kayzrstaff.fragments.AvailibilitiesFragment;
import com.kayzr.kayzrstaff.fragments.HomeFragment;
import com.kayzr.kayzrstaff.fragments.RosterFragment;
import com.kayzr.kayzrstaff.fragments.SettingsFragment;
import com.kayzr.kayzrstaff.fragments.TeamInfoFragment;
import com.kayzr.kayzrstaff.network.Calls;
import com.kayzr.kayzrstaff.network.Config;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view)NavigationView navigationView;

    public static KayzrApp app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        displaySelectedScreen(R.id.nav_home);

        app = (KayzrApp) getApplicationContext();
/*        if(app.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/
        getThisWeekTournaments();
        getNextWeekTournaments();
        getUsersList();

        navigationView.setNavigationItemSelectedListener(this);
    }


    public void getThisWeekTournaments() {

        Calls caller = Config.getRetrofit().create(Calls.class);
        Call<List<Tournament>> call = caller.getThisWeekTournaments();
        call.enqueue(new Callback<List<Tournament>>() {
            @Override
            public void onResponse(Call<List<Tournament>> call, Response<List<Tournament>> response) {
                app.setThisWeek(response.body());
                Log.d("Backend Call", " call successful (ThisWeekTournaments)");
            }

            @Override
            public void onFailure(Call<List<Tournament>> call, Throwable t) {
                Log.e("Backend CAll", "call failed (ThisWeekTournaments) " + t.getMessage());
            }
        });

    }

    public void getNextWeekTournaments() {

        Calls caller = Config.getRetrofit().create(Calls.class);
        Call<List<Tournament>> call = caller.getNextWeekTournaments();
        call.enqueue(new Callback<List<Tournament>>() {
            @Override
            public void onResponse(Call<List<Tournament>> call, Response<List<Tournament>> response) {
                app.setNextWeek(response.body());
                Log.d("Backend Call", " call successful (NextWeekTournaments)");
            }

            @Override
            public void onFailure(Call<List<Tournament>> call, Throwable t) {
                Log.e("Backend CAll", "call failed (NextWeekTournaments) " + t.getMessage());
            }
        });

    }

    public void getUsersList() {

        Calls caller = Config.getRetrofit().create(Calls.class);
        Call<List<User>> call = caller.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                app.setKayzrTeam(response.body());
                Log.d("Backend Call", " call successful (getTeam)");

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Backend CAll", "call failed (getTeam) " + t.getMessage());
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Fragment fragment = null;
        int id = item.getItemId();

        // Noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragment = new SettingsFragment();
            toolbar.setTitle("Settings");
        }

        // Replacing the fragment.
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void displaySelectedScreen(int itemId) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;

        if (itemId == R.id.nav_home) {
            fragment = new HomeFragment();
            toolbar.setTitle("KayzrStaff");
        } else if (itemId == R.id.nav_this_week) {
            fragment = new RosterFragment();
            toolbar.setTitle("This week");
        } else if (itemId == R.id.nav_availabilities) {
            fragment = new AvailibilitiesFragment();
            toolbar.setTitle("Availibilities");
        } else if (itemId == R.id.nav_team_info) {
            fragment = new TeamInfoFragment();
            toolbar.setTitle("Team Info");
        }

        // Replace the fragment.
        if (fragment != null) {
            // Make sure options show
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);

    }

}
