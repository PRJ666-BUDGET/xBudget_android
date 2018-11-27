package com.prj666_183a06.xbudget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prj666_183a06.xbudget.database.Plans;
import com.prj666_183a06.xbudget.receiptocr.CameraActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    FragmentManager fragmentManager = null;
    Fragment fragment = null;
    NavigationView navigationView;

    private DatabaseReference planRef = FirebaseDatabase.getInstance().getReference("plans");
    public static double gIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        planRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                gIncome = 0;
                for (DataSnapshot planData : dataSnapshot.getChildren()) {
                    Plans planValue = planData.getValue(Plans.class);
                    if(planValue.getPlan_type().equals("income")){
                        gIncome += planValue.getPlan_amount();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed!!!");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                        if (current instanceof HomeFragment) {
                            navigationView.setCheckedItem(R.id.nav_home);
                        }
                        else if (current instanceof  PlansFragment) {
                            navigationView.setCheckedItem(R.id.nav_budget);
                        }
                        else if (current instanceof  ExpenseActivity) {
                            navigationView.setCheckedItem(R.id.nav_expense);
                        }
                        else if (current instanceof  ReportFragment) {
                            navigationView.setCheckedItem(R.id.nav_report);
                        }
                    }
                }
        );
    }

    double getgIncome() {
        return gIncome;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment current = getCurrentFragment();

            if (current instanceof HomeFragment) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to close application?").setPositiveButton("Yes", confirmCloseDialogClickListener)
                        .setNegativeButton("No", confirmCloseDialogClickListener).show();
            }
            else if (checkNavigationMenuItem() != 0) {
                navigationView.setCheckedItem(R.id.nav_home);
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
            else {
                super.onBackPressed();
            }
        }
    }

    DialogInterface.OnClickListener confirmCloseDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finishAndRemoveTask();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // REMOVE Settings
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
//        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_budget:
                fragment = new PlansFragment();
                break;
            case R.id.nav_expense:
                fragment = new ExpenseActivity();
                break;
            case R.id.nav_report:
                fragment = new ReportFragment();
                break;
            case R.id.nav_camera:
                Intent openCamera = new Intent(this, CameraActivity.class);
                startActivity(openCamera);
                break;
        }

        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, fragment)
//                    .addToBackStack(BACK_STACK_ROOT_TAG)
//                    .commit();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }
}
