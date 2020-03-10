package com.panaceasoft.riftblackmarket.ui.item.search.searchlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivitySettingBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;

public class SearchListActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        initUI(activityFilteringBinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI(ActivitySettingBinding binding) {

        String title = getIntent().getStringExtra(Constants.ITEM_NAME);

        if (title != null) {
            initToolbar(binding.toolbar, title);
        } else {
            initToolbar(binding.toolbar, getString(R.string.item_list_title));
        }

        setupFragment(new SearchListFragment());

        // setup Fragment

        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

}
