package com.rock.debitdiver;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.MainPage.DebtCalculatorFragment;
import com.rock.debitdiver.MainPage.DebtListFragment;
import com.rock.debitdiver.MainPage.MainBaseFragment;
import com.rock.debitdiver.MainPage.PaymentDetailFragment;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.Utils.MPreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;
import static com.rock.debitdiver.MainActivity.FRAGMENTS.CALCFRAGMENT;
import static com.rock.debitdiver.MainActivity.FRAGMENTS.DEBTFRAGMENT;
import static com.rock.debitdiver.MainActivity.FRAGMENTS.PAYMENTFRAGMENT;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static double totalDebt = 0;
    public static double totalpaid = 0;
    public enum FRAGMENTS{
        CALCFRAGMENT, DEBTFRAGMENT, PAYMENTFRAGMENT
    }
    FRAGMENTS currentFragment;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitializeDebtList();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerLayout =
                navigationView.getHeaderView(0);
        TextView userName = headerLayout.findViewById(R.id.userName);
        TextView userEmail = headerLayout.findViewById(R.id.userEmail);
        userName.setText(MPreferenceManager.readStringInformation(this, MPreferenceManager.NAME));
        userEmail.setText(MPreferenceManager.readStringInformation(this, MPreferenceManager.EMAIL));
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void InitializeDebtList() {
        getMainApp().debtLists.clear();
        totalDebt = 0;
        totalpaid = 0;
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("TOKEN", MPreferenceManager.readStringInformation(this, MPreferenceManager.TOKEN));
        ApiCallWrapper service = new ApiCallWrapper(this, POST, true, "", ServerURLs.GET_DEBTLIST_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        JSONArray array = returnValue.getJSONArray("msg");
                        for(int i = 0; i < array.length(); i ++){
                            DebtInfo item = new DebtInfo(array.getJSONObject(i));
                            getMainApp().debtLists.add(item);
                        }
                        for(DebtInfo item:getMainApp().debtLists){
                            totalDebt += Double.parseDouble(item.getAmount());
                            totalpaid += Double.parseDouble(item.getCurrent_paid());
                        }
                        addFragment(CALCFRAGMENT);
                    }
                    else{
                        Toast.makeText(MainActivity.this, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        service.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Logout();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId())
        {
            case R.id.navCalculate:
                addFragment(CALCFRAGMENT);
                break;
            case R.id.navDebit:
                addFragment(DEBTFRAGMENT);
                break;
            case R.id.navPayment:
                addFragment(PAYMENTFRAGMENT);
                break;
            case R.id.navExit:
                Logout();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        MPreferenceManager.saveStringInformation(this, MPreferenceManager.TOKEN, "");
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    public void setCurrentFragment(FRAGMENTS currentFragment) {
        this.currentFragment = currentFragment;
        switch(currentFragment) {
            case CALCFRAGMENT:
                getSupportActionBar().setTitle("Calculation");
                break;
            case DEBTFRAGMENT:
                getSupportActionBar().setTitle("Debt Paid");
                break;
            case PAYMENTFRAGMENT:
                getSupportActionBar().setTitle("Payment History");
                break;
        }
    }

    /**
     *
     */
    public void addFragment(FRAGMENTS donationfragment) {
        if(currentFragment != donationfragment){
            setCurrentFragment(donationfragment);
            MainBaseFragment f = null;
            switch (donationfragment){
                case CALCFRAGMENT:
                    f = DebtCalculatorFragment.newInstance();
                    break;
                case DEBTFRAGMENT:
                    f = DebtListFragment.newInstance();
                    break;
                case PAYMENTFRAGMENT:
                    f = PaymentDetailFragment.newInstance();
                    break;
            }
            attachFragment(f);
        }
    }

    /**
     *
     */
    private void attachFragment(MainBaseFragment f) {
        if (f != null) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, f).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.print, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.print){
            if(currentFragment == DEBTFRAGMENT){
                Toast.makeText(this, getSupportActionBar().getTitle(), Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}
