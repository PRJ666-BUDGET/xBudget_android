package com.prj666_183a06.xbudget;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.prj666_183a06.xbudget.Database.PlanRepository;
import com.prj666_183a06.xbudget.Local.PlanDataSource;
import com.prj666_183a06.xbudget.Local.PlanDatabase;
import com.prj666_183a06.xbudget.Model.Plan;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Adapter
    List<Plan> planList = new ArrayList<>();
    ArrayAdapter adapter;

    //Database
    private CompositeDisposable compositeDisposable;
    private PlanRepository planRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        
        // Database
        // TODO: 2018-10-08 Add expanse database?
        PlanDatabase planDatabase = PlanDatabase.getInstance(this); // Create database
        planRepository = PlanRepository.getInstance(PlanDataSource.getIntance(planDatabase.planDAO()));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);
    }

//    private void loadData() {
//        //Use RxJava
//        Disposable disposbale = planRepository.getAllPlans()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<List<Plan>>() {
//                               @Override
//                               public void accept(List<Plan> plans) throws Exception {
//                                   onGetAllPlanSuccess(plans);
//                               }
//                           },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                Toast.makeText(MainActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//        compositeDisposable.add(disposbale);
//    }

    private void onGetAllPlanSuccess(List<Plan> plans) {
        planList.clear();
        planList.addAll(plans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.demo_clear) {
//            deleteAllPlans();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void deleteAllPlans() {
//
//
//        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(ObservableEmitter<Object> e) throws Exception {
//                planRepository.deleteAllPlan();
//                e.onComplete();
//
//        })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer() {
//
//                               @Override
//                               public void accept(Object o) throws Exception {
//
//                               }
//                           }, new Consumer<Throwable>() {
//                               @Override
//                               public void accept(Throwable throwable) throws Exception {
//                                   Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                               }
//                           },
//                        new Action() {
//                            @Override
//                            public void run() throws Exception {
//                                loadData(); // Refresh data
//                            }
//                        }
//                );
//
//        compositeDisposable.add(disposable);
//    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                fragment = new HomeActivity();
                break;
            case R.id.nav_budget:
                fragment = new PlansActivity();
                break;
            case R.id.nav_expense:
                fragment = new ExpenseActivity();
                break;
            case R.id.nav_report:
                fragment = new ReportActivity();
                break;
            case R.id.nav_settings:
                fragment = new SettingsActivity();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
