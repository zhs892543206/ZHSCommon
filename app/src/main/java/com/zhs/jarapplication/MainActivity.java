package com.zhs.jarapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhs.commonhelper.interf.ActInterface;
import com.zhs.commonhelper.widget.dialog.ToastUtil;
import com.zhs.commonhelper.widget.fastsearch.MyFastIndexLLy;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.ConfirmCallback;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ActInterface{
    /**
     * 存储该用户需要管辖的部门
     */
    private String[] deptIds = {"1", "2", "3", "4", "5"
            , "6", "7", "8", "9", "10", "11"
            , "12", "13", "14", "15", "16", "17"//, "神经内科", "烧伤科", "眼科", "妇产科16:30"
    };

    /**
     * 存储该用户需要管辖的部门
     */
    private String[] deptStrs = {"骨科", "脑外科", "神级外科", "心胸外科", "消化内科"
            , "心血管内科 6A-11", "血液内科", "儿科", "耳鼻喉科", "口腔科", "皮肤科"
            , "中医科", "单", "Sbcd", "#心胸外科", "精神 病科", " 泌尿外科"//, "神经内科", "烧伤科", "眼科", "妇产科16:30"
    };//# 各种符号也分中英文，搜索输入不同结果也不同
    private List<SearchModel> deptList = new ArrayList<>();
    private MyFastIndexLLy fastIndexLLy;
    private String optType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initHeader();
        initView(savedInstanceState);
        showView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void initData() {
        for(int i = 0 ;i< deptIds.length; i++){
            deptList.add(new SearchModel(deptIds[i], deptStrs[i], "1"));
        }
    }

    @Override
    public void initHeader() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fastIndexLLy = findViewById(R.id.myfastindexly);

        //deptList长度为0会奔溃的，所以长度0这个界面我都不让他进来了.或者不执行这句
        fastIndexLLy.initView(deptList, true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FloatingActionButton和Snackbar结合外出布局必须是CoordinatorLayout，参数1需要传这个view，其他情况参数1可以用activity.findViewById(android.R.id.content)
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "ces", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

    }

    @Override
    public void showView() {
        fastIndexLLy.setRecyclerClick(new ConfirmCallback() {

            @Override
            public void onClickConfirm(int pos) {
                if(pos>=0) {
                    ToastUtil.showToast(fastIndexLLy.searchContactList.get(pos).getCity());
                }
            }
        });
        fastIndexLLy.setRightTxt("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(optType!=null && optType.length()>0){
            fastIndexLLy.setLeftTxt(optType, null);
        }
    }
}
