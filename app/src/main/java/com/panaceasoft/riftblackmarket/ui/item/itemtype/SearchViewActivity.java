package com.panaceasoft.riftblackmarket.ui.item.itemtype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ActivitySearchViewBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.ui.item.itemcondition.ItemConditionFragment;
import com.panaceasoft.riftblackmarket.ui.item.itemcurrency.ItemCurrencyTypeFragment;
import com.panaceasoft.riftblackmarket.ui.item.itemdealoption.ItemDealOptionTypeFragment;
import com.panaceasoft.riftblackmarket.ui.item.itemlocation.ItemLocationFragment;
import com.panaceasoft.riftblackmarket.ui.item.itempricetype.ItemPriceTypeFragment;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;

public class SearchViewActivity extends PSAppCompactActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchViewBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_search_view);

        initUI(databinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }

    protected void initUI(ActivitySearchViewBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.ITEM_TYPE_FLAG);

        switch (fragName) {
            case Constants.ITEM_TYPE:
                setupFragment(new ItemTypeFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_type_title));
                break;
            case Constants.ITEM_PRICE_TYPE:
                setupFragment(new ItemPriceTypeFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_price_type_title));
                break;
            case Constants.ITEM_CURRENCY_TYPE:
                setupFragment(new ItemCurrencyTypeFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_currency_title));
                break;
            case Constants.ITEM_DEAL_OPTION_TYPE:
                setupFragment(new ItemDealOptionTypeFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_deal_option_title));
                break;
            case Constants.ITEM_LOCATION_TYPE:
                setupFragment(new ItemLocationFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_location_title));
                break;
            case Constants.ITEM_CONDITION_TYPE:
                setupFragment(new ItemConditionFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_condition_title));
                break;
        }

    }
}