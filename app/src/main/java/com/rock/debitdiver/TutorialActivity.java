package com.rock.debitdiver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rock.debitdiver.Register.RegisterEmail;
import com.rock.debitdiver.Register.RegisterProfile;
import com.rock.debitdiver.Tutorials.BarelyMove;
import com.rock.debitdiver.Tutorials.FollowMe;
import com.rock.debitdiver.Tutorials.GuideUserDebtCalc;
import com.rock.debitdiver.Tutorials.GuideUserLogin;
import com.rock.debitdiver.Tutorials.IntroductionDiver;
import com.rock.debitdiver.Tutorials.SinkingDebit;

import me.itangqi.waveloadingview.WaveLoadingView;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {
    ViewPager tutorialViewPager;
    CircleIndicator indicator;
    WaveLoadingView loadingIndicator;
    TutorialAdapter tutorialAdapter;
    Button btSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        tutorialViewPager = findViewById(R.id.tutorialViewPager);
        indicator = findViewById(R.id.indicator);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btSkip = (Button)findViewById(R.id.btSkip);

        loadingIndicator.setProgressValue(80);
        loadingIndicator.setAmplitudeRatio(60);
        loadingIndicator.setAnimDuration(3000);

        tutorialAdapter = new TutorialAdapter(getSupportFragmentManager());
        tutorialViewPager.setAdapter(tutorialAdapter);
        indicator.setViewPager(tutorialViewPager);
        tutorialViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadingIndicator.setProgressValue(position % 2 == 0 ? 80 :  70);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class TutorialAdapter extends FragmentPagerAdapter
    {
        public TutorialAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position)
            {
                case 0:
                    fragment =  IntroductionDiver.newInstance();
                    break;
                case 1:
                    fragment = SinkingDebit.newInstance();
                    break;
                case 2:
                    fragment = BarelyMove.newInstance();
                    break;
                case 3:
                    fragment = FollowMe.newInstance();
                    break;
                case 4:
                    fragment = GuideUserLogin.newInstance();
                    break;
                case 5:
                    fragment = GuideUserDebtCalc.newInstance();
                    break;
                default:
                    fragment = IntroductionDiver.newInstance();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
