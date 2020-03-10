package com.panaceasoft.riftblackmarket.ui.item.itemfromfollower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivityItemFromFollowerListBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;

public class ItemFromFollowerListActivity extends PSAppCompactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityItemFromFollowerListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_item_from_follower_list);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //region Private Methods

    private void initUI(ActivityItemFromFollowerListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.item_from_follower_toolbar));

        // setup Fragment
        setupFragment(new ItemFromFollowerListFragment());

    }

    //endregion
}
