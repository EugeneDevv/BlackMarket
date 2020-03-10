package com.panaceasoft.riftblackmarket.ui.notification.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivityNotificationListBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;

public class NotificationListActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNotificationListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_list);

        // Init all UI
        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));

    }
    //endregion


    //region Private Methods

    private void initUI(ActivityNotificationListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.notification__title));

        // setup Fragment
        setupFragment(new NotificationListFragment());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //endregion


}