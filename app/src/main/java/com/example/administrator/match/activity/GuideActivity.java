package com.example.administrator.match.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.match.R;
import com.example.administrator.match.adapter.GuideFragmentAdapter;
import com.example.administrator.match.fragment.GuideFragment1;
import com.example.administrator.match.fragment.GuideFragment2;
import com.example.administrator.match.fragment.GuideFragment3;
import com.example.administrator.match.until.CacheUntil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private View view1,view2,view3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager=findViewById(R.id.guideViewPager);
        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);
        view3=findViewById(R.id.view3);
        List<Fragment>list=new ArrayList<>();
        list.add(new GuideFragment1());
        list.add(new GuideFragment2());
        list.add(new GuideFragment3());


        viewPager.setAdapter(new GuideFragmentAdapter(getSupportFragmentManager(),list));

        viewPager.setOnPageChangeListener(OnPageChangeListener);


        boolean first= CacheUntil.getBoolean(this,"first",false);
        Log.e("e-----------------",first+"");
        if(first){
            Intent intent=new Intent(GuideActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else{
            CacheUntil.putBoolean(this,"first",true);
 //         new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent=new Intent(GuideActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//            },3000);
        }
    }
    ViewPager.OnPageChangeListener OnPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

                switch (i){
                    case 0:
                        view1.setBackgroundResource(R.drawable.guide_view_checked);
                        view2.setBackgroundResource(R.drawable.guide_view_nochecked);
                        view3.setBackgroundResource(R.drawable.guide_view_nochecked);
                        break;
                    case 1:
                        view1.setBackgroundResource(R.drawable.guide_view_nochecked);
                        view2.setBackgroundResource(R.drawable.guide_view_checked);
                        view3.setBackgroundResource(R.drawable.guide_view_nochecked);
                        break;
                    case 2:
                        view1.setBackgroundResource(R.drawable.guide_view_nochecked);
                        view2.setBackgroundResource(R.drawable.guide_view_nochecked);
                        view3.setBackgroundResource(R.drawable.guide_view_checked);
                        break;
                }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };
}
