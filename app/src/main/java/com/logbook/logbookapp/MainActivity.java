package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(AppConstant.VEHICLE_TAB),false);
        //tabLayout.addTab(tabLayout.newTab().setText(AppConstant.SERVICE_SHOP_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(AppConstant.ABOUT_TAB));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                switch(tab.getText().toString()) {
                    case AppConstant.VEHICLE_TAB:
                        startVehiclesActivity();
                        break;
                    case AppConstant.ABOUT_TAB:
                        showAbount();
                        break;
                    case AppConstant.OTHER_TAB:
                        break;
                    default:
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getText().toString()) {
                    case AppConstant.VEHICLE_TAB:
                        break;
                    case AppConstant.ABOUT_TAB:
                        hideAbount();
                        break;
                    case AppConstant.OTHER_TAB:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void showAbount(){
        TextView aboutText = (TextView) findViewById(R.id.mainTextView);
        aboutText.setVisibility(View.VISIBLE);
    }

    public void hideAbount(){
        TextView aboutText = (TextView) findViewById(R.id.mainTextView);
        aboutText.setVisibility(View.INVISIBLE);
    }

    public void startVehiclesActivity(){
        Intent intent = new Intent(this,VehiclesActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
