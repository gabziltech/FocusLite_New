package com.example.focuslite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focuslite.Database.DataHelp;
import com.example.focuslite.Database.MyOpenHelper;
import com.example.focuslite.MainFragments.FragmentCSV;
import com.example.focuslite.MainFragments.FragmentHome;
import com.example.focuslite.MainFragments.FragmentHistory;
import com.example.focuslite.MainFragments.FragmentProfile;
import com.example.focuslite.MainFragments.FragmentVideos;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment=null;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    LinearLayout Profile;
    TextView EmailId;
    ActionBarDrawerToggle toggle;
    MyOpenHelper db;
    DataHelp dh;
    boolean second, flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new MyOpenHelper(getApplicationContext());
        dh = new DataHelp(getApplicationContext());

        fragment = new FragmentHome();
        setFragment(fragment);

        View mHeaderView = navigationView.getHeaderView(0);
        Profile=(LinearLayout) mHeaderView.findViewById(R.id.profile);
        EmailId=(TextView) mHeaderView.findViewById(R.id.email_id);

        String EmailID = db.getEmailId();
        EmailId.setText(EmailID);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new FragmentProfile();
                setFragment(fragment);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                item.setVisible(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 1) {
                if (!second) {
                    Toast.makeText(getApplication(), "Press back again to close application", Toast.LENGTH_SHORT).show();
                    second = true;
                } else {
                    second = false;
                    this.finish();
                }
            } else {
                if (flag == false) {
                    if (count != 1)
                        getSupportFragmentManager().popBackStack();
                } else
                    flag = false;
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_connection) {
            fragment=new FragmentHome();
        } else if (id == R.id.nav_history) {
            fragment=new FragmentHistory();
        } else if (id == R.id.nav_videos) {
            fragment=new FragmentVideos();
        } else if (id == R.id.nav_generate) {
            fragment=new FragmentCSV();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "javed@gabzil.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "About App");
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            String url = "https://play.google.com/store/apps/details?id=com.example.focuslite";
            Intent messageSendIntent = new Intent();
            messageSendIntent.setAction(Intent.ACTION_SEND);
            messageSendIntent.putExtra(Intent.EXTRA_TEXT, url);
            messageSendIntent.setType("text/plain");
            startActivity(messageSendIntent);
        } else if (id == R.id.nav_logout) {
            dh.DeleteSession();
            Intent i = new Intent(MainActivity.this,Login.class);
            startActivity(i);
        }

        setFragment(fragment);
        return true;
    }

    public void setFragment(Fragment frag) {
        if (fragment != null) {
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack("" + frag.getClass().getSimpleName());
                ft.commit();

            } catch (Exception e) {
                Toast.makeText(this, "Some problem occured, please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("MainActivity", "Error in creating content_main");
        }
        drawer.closeDrawer(GravityCompat.START);
    }
}
