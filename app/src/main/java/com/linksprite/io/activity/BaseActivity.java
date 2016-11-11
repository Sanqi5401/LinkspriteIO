package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.linksprite.io.R;
import com.linksprite.io.database.Account;

import org.greenrobot.eventbus.Subscribe;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/7/29.
 */
public class BaseActivity extends AppCompatActivity {

    protected Toolbar mscToolbar;
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    protected TextView emailNavHeader;

    protected Account account = null;
    private boolean isDestroy = false;
    protected String titleString;
    protected boolean isConnect = true;
    private boolean isShow = false;
    public static Intent networkStateServiceIntent;

    //
    public void nextActivity(Intent intent) {
        startActivity(intent);
    }

    public void previousActivity(Intent intent) {
        startActivity(intent);
    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        isDestroy = true;
//        stopService(networkStateServiceIntent);
//        EventBus.getDefault().unregister(this);
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(this);
//        EventBus.getDefault().register(this);
//        // Load network details into Globals._currentNetwork
//        if (networkStateServiceIntent == null) {
//            //TODO this line seems redundant unless the service takes a long time to start. profile it
//            NetworkUtils.refreshNetworkMode(this);
//            networkStateServiceIntent = new Intent(this, NetworkStateService.class);
//            startService(networkStateServiceIntent);
//        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mscToolbar = (Toolbar) findViewById(R.id.msc_toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        View headerLayout = navigationView.getHeaderView(0);
        emailNavHeader = (TextView) headerLayout.findViewById(R.id.email_nav_header);
        if (mscToolbar != null) {
            setSupportActionBar(mscToolbar);
            getSupportActionBar().setTitle(titleString);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
            getSupportActionBar().setHomeButtonEnabled(true);

            if (account.getEmail() != null) {
                emailNavHeader.setText(account.getEmail());
                emailNavHeader.setVisibility(View.VISIBLE);
            }

            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            menuItem.setChecked(true);
                            drawerLayout.closeDrawers();
                            Intent intent = null;
                            switch (menuItem.getItemId()) {
//								case R.id.drawer_account:
//									intent = new Intent(BaseActivity.this, HomeActivity.class);
//									break;
                                case R.id.drawer_global_settings:
                                    if (BaseActivity.this.getClass().equals(SettingActivity.class))
                                        return true;
                                    intent = new Intent(BaseActivity.this, SettingActivity.class);

                                    break;
                                case R.id.drawer_home:
                                    if (BaseActivity.this.getClass().equals(HomeActivity.class))
                                        return true;
                                    intent = new Intent(BaseActivity.this, HomeActivity.class);
                                    break;
                                case R.id.drawer_logout:
                                    logout();
                                    return true;
                                case R.id.drawer_users:
                                    if (BaseActivity.this.getClass().equals(UserActivity.class))
                                        return true;
                                    intent = new Intent(BaseActivity.this, UserActivity.class);
                                    break;
                            }
                            if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                            }
                            startActivity(intent);
                            //TODO if you leave the activity and come back later, the item is stil selected.
                            //javadoc: Return true to display the item as the selected item
                            return true;
                        }
                    });
        }
    }

    @Subscribe
    public void onEvent(final Boolean isConnect) {
        this.isConnect = isConnect;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public void showErrDialog(final String err) {
        if(!isDestroyed())
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isShow && !isDestroy) {
                    isShow = true;
                    SweetAlertDialog dialog = new SweetAlertDialog(BaseActivity.this, SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Error");
                    dialog.setContentText(err);
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            isShow = false;
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void logout() {
        Intent myIntent = new Intent(BaseActivity.this, LoginActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
        finish();
    }
}
