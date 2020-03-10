package com.panaceasoft.riftblackmarket.ui.user.userlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivityUserListBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;
import com.panaceasoft.riftblackmarket.viewobject.holder.UserParameterHolder;

public class UserListActivity extends PSAppCompactActivity {


    UserParameterHolder userHolder = new UserParameterHolder();

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);
        
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

    private void initUI(ActivityUserListBinding binding) {

        userHolder = (UserParameterHolder) getIntent().getSerializableExtra(Constants.USER_PARAM_HOLDER_KEY);

        // Toolbar
        if(userHolder.return_types.equals("follower")) {
            initToolbar(binding.toolbar, getString(R.string.user_follower_list_toolbar_name));
        }else {
            initToolbar(binding.toolbar, getString(R.string.user_following_list_toolbar_name));
        }

        // setup Fragment
        setupFragment(new UserListFragment());

    }

}
