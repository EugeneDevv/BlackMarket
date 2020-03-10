package com.panaceasoft.riftblackmarket.ui.item.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivityMapBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;
import com.panaceasoft.riftblackmarket.utils.Utils;

public class MapActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map);

        // Init all UI
        initUI(binding);
    }
    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString("Language", Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.psLog("Inside Result MainActivity");
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //region Private Methods

    private void initUI(ActivityMapBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.MAP_FLAG);

        switch (fragName) {
            case Constants.MAP:
                setupFragment(new MapFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.map_filter__map_title));
                break;
            case Constants.MAP_PICK:
                setupFragment(new PickMapFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.map_filter__map_title));
                break;
        }
    }
}
