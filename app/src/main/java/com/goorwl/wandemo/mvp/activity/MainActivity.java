package com.goorwl.wandemo.mvp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.fragment.FragmentFour;
import com.goorwl.wandemo.mvp.fragment.FragmentHome;
import com.goorwl.wandemo.mvp.fragment.FragmentOne;
import com.goorwl.wandemo.mvp.fragment.FragmentThree;
import com.goorwl.wandemo.mvp.fragment.FragmentTwo;
import com.goorwl.wandemo.mvp.imple.MainActivityImple;
import com.goorwl.wandemo.mvp.presenter.MainActivityPresenter;
import com.goorwl.wandemo.utils.FragmentEnmu;

public class MainActivity extends BaseActivity implements MainActivityImple {

    private static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private Fragment              mFHome;
    private Fragment              mFOne;
    private Fragment              mFTwo;
    private Fragment              mFThree;
    private Fragment              mFFour;
    private Toolbar               mToolbar;
    private Context               mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void initView() {
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        //        tabLayout.addTab(tabLayout.newTab().setText("测试1").setIcon());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navi_home));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navi_one));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navi_two));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navi_three));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navi_four));

        mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));

        switchFragment(FragmentEnmu.ZERO);
        mToolbar.setTitle(R.string.navi_home);

        mToolbar.inflateMenu(R.menu.menu_home);

        // 设置actionbar 点击事件
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    Toast.makeText(mContext, "搜索界面", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_shezhi:
                    Toast.makeText(mContext, "设置界面", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        // 监听底部
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        switchFragment(FragmentEnmu.ONE);
                        mToolbar.setTitle(R.string.navi_one);
                        break;
                    case 2:
                        switchFragment(FragmentEnmu.TWO);
                        mToolbar.setTitle(R.string.navi_two);
                        break;
                    case 3:
                        switchFragment(FragmentEnmu.THREE);
                        mToolbar.setTitle(R.string.navi_three);
                        break;
                    case 4:
                        switchFragment(FragmentEnmu.FOUR);
                        mToolbar.setTitle(R.string.navi_four);
                        break;
                    default:
                        switchFragment(FragmentEnmu.ZERO);
                        mToolbar.setTitle(R.string.navi_home);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // GET INSTANCE OF PRESENTER
        mPresenter = new MainActivityPresenter(this, this);
    }

    private void switchFragment(FragmentEnmu enmu) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (enmu) {
            case ZERO:
                if (mFHome == null) {
                    mFHome = FragmentHome.getInstance();
                    fragmentTransaction.add(R.id.main_content, mFHome);
                } else {
                    fragmentTransaction.show(mFHome);
                }
                break;
            case ONE:
                if (mFOne == null) {
                    mFOne = FragmentOne.getInstance();
                    fragmentTransaction.add(R.id.main_content, mFOne);
                } else {
                    fragmentTransaction.show(mFOne);
                }
                break;
            case TWO:
                if (mFTwo == null) {
                    mFTwo = FragmentTwo.getInstance();
                    fragmentTransaction.add(R.id.main_content, mFTwo);
                } else {
                    fragmentTransaction.show(mFTwo);
                }
                break;
            case THREE:
                if (mFThree == null) {
                    mFThree = FragmentThree.getInstance();
                    fragmentTransaction.add(R.id.main_content, mFThree);
                } else {
                    fragmentTransaction.show(mFThree);
                }
                break;
            case FOUR:
                if (mFFour == null) {
                    mFFour = FragmentFour.getInstance();
                    fragmentTransaction.add(R.id.main_content, mFFour);
                } else {
                    fragmentTransaction.show(mFFour);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mFHome != null) {
            fragmentTransaction.hide(mFHome);
        }
        if (mFOne != null) {
            fragmentTransaction.hide(mFOne);
        }
        if (mFTwo != null) {
            fragmentTransaction.hide(mFTwo);
        }
        if (mFThree != null) {
            fragmentTransaction.hide(mFThree);
        }
        if (mFFour != null) {
            fragmentTransaction.hide(mFFour);
        }
    }

    long firstClick;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
