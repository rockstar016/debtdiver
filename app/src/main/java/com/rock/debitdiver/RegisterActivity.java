package com.rock.debitdiver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.rock.debitdiver.Register.RegisterEmail;
import com.rock.debitdiver.Register.RegisterProfile;
import com.rock.debitdiver.Utils.OnDisableViewPager;

import me.relex.circleindicator.CircleIndicator;

public class RegisterActivity extends AppCompatActivity {

    OnDisableViewPager viewPager;
    CircleIndicator indicator;
    RegisterAdapter registerAdapter;
    public String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        viewPager = findViewById(R.id.vwPager);
        viewPager.setPagingEnabled(false);
        indicator = findViewById(R.id.indicator);
        registerAdapter = new RegisterAdapter(getSupportFragmentManager());
        viewPager.setAdapter(registerAdapter);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    public void showProfilePage()
    {
        viewPager.setCurrentItem(1);
    }

    public void showEmailPage()
    {
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            if(viewPager.getCurrentItem() == 0)
                finish();
            else
                viewPager.setCurrentItem(0);
        }
        return true;
    }

    class RegisterAdapter extends FragmentPagerAdapter
    {
        public RegisterAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
            {
                return RegisterEmail.newInstance();
            }
            else
            {
                return RegisterProfile.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
