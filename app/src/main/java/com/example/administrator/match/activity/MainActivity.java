package com.example.administrator.match.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.match.R;
import com.example.administrator.match.fragment.BillingManageActivity;
import com.example.administrator.match.fragment.Fragment_environment;
import com.example.administrator.match.fragment.Fragment_road;
import com.example.administrator.match.fragment.SetCarAccountRechargeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frame;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private List<Fragment>list;
    private ActionBarDrawerToggle toggle;
    private TextView title;
    private TextView netSetting;
    private Toolbar toolbar;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fragmentManager=getSupportFragmentManager();
        findviews();

        initData();

        setListener();
    }

    private void findviews() {
        drawer=findViewById(R.id.drawer);
        frame = (FrameLayout) findViewById(R.id.frame);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        title=findViewById(R.id.title);
        netSetting=findViewById(R.id.tv_netSetting);
        toolbar=findViewById(R.id.toolbar);
    }

    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                transaction=fragmentManager.beginTransaction();
                Toast.makeText(MainActivity.this, ""+menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId()){
                    case R.id.account:
                        transaction.replace(R.id.frame,list.get(0));
                        break;
                    case R.id.car_account:
                        transaction.replace(R.id.frame,list.get(1));
                        break;
                    case R.id.environment:
                        transaction.replace(R.id.frame,list.get(2));
                        break;
                    case R.id.road_setting:
                        transaction.replace(R.id.frame,list.get(3));
                }
                transaction.commit();
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void initData() {
        title.setText("");
        netSetting.setVisibility(View.GONE);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,0,0);
        actionBarDrawerToggle.syncState();

        list=new ArrayList<>();
        list.add(new BillingManageActivity());
        list.add(new SetCarAccountRechargeFragment());
        list.add(new Fragment_environment());
        list.add(new Fragment_road());

    }

}
