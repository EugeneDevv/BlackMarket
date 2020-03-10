package com.panaceasoft.riftblackmarket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.panaceasoft.riftblackmarket.databinding.ActivityMainBinding;
import com.panaceasoft.riftblackmarket.ui.common.NavigationController;
import com.panaceasoft.riftblackmarket.ui.common.PSAppCompactActivity;
import com.panaceasoft.riftblackmarket.utils.AppLanguage;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.MyContextWrapper;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.common.NotificationViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.ItemViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;
import com.panaceasoft.riftblackmarket.viewobject.User;
import com.panaceasoft.riftblackmarket.viewobject.holder.ItemParameterHolder;

import javax.inject.Inject;

/**
 * MainActivity of Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 *
 * @author Panacea-soft
 * @version 1.0
 * @since 11/15/17.
 */

public class MainActivity extends PSAppCompactActivity {


    //region Variables

    @Inject
    SharedPreferences pref;

    @Inject
    AppLanguage appLanguage;
    private Boolean notiSetting = false;
    private String token = "";
    private UserViewModel userViewModel;
    private ItemViewModel itemViewModel;
    private NotificationViewModel notificationViewModel;
    private User user;
    private PSDialogMsg psDialogMsg;
    private boolean isLogout = false;
    Drawable yourdrawable = null;
    ActionBarDrawerToggle drawerToggle;
    private String token1;
    public String selectedLocationId, selectedLocationName, selected_lat, selected_lng;
    private String loginUserId;
    private String locationId;
    private String locationName;
    public String notiItemId, notibuyerId, notiSellerId, notiMsg, notisenderName, notiSenderUrl, userId;
    String receiverId = Constants.EMPTY_STRING;
    String receiverName = Constants.EMPTY_STRING;
    String receiverUrl = Constants.EMPTY_STRING;
    int requestCode = 0;
    String flag = Constants.EMPTY_STRING;

    Dialog dialog;
    TextView titleTextView, msgTextView, subTitleTextView;
    Button openItemButton;
    ImageButton closeButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    public ActivityMainBinding binding;

    //endregion


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_PSTheme);

        super.onCreate(savedInstanceState);

        // onNewIntent(getIntent());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUIAndActions();

        initModels();

        initData();

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            String message = getBaseContext().getString(R.string.message__want_to_quit);
            String okStr = getBaseContext().getString(R.string.message__ok_close);
            String cancelStr = getBaseContext().getString(R.string.message__cancel_close);

            psDialogMsg.showConfirmDialog(message, okStr, cancelStr);

            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(view -> {

                psDialogMsg.cancel();
                finish();
                System.exit(0);
            });

            psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

        }
        return true;
    }

    //endregion


    //region Private Methods

    /**
     * Initialize Models
     */
    private void initModels() {

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationViewModel.class);
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
    }


    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private void showAlertMessage(String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.ps_dialog, null);

        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null);

        TextView message = view.findViewById(R.id.messageTextView);

        message.setText(msg);

        builder.create();

        builder.show();

    }


    /**
     * This function will initialize UI and Event Listeners
     */
    private void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this, false);

        initToolbar(binding.toolbar, Constants.EMPTY_STRING);

        initDrawerLayout();

        initNavigationView();

        navigationController.navigateToCityList(this);
        showBottomNavigation();

        setSelectMenu(R.id.nav_home);

        getIntentData();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_menu:
                    //layout_scrollFlags
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.VISIBLE);
                    navigationController.navigateToHome(MainActivity.this, false, selectedLocationId, selectedLocationName);
                    setToolbarText(binding.toolbar, Constants.EMPTY_STRING);

                    break;
                case R.id.message_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.GONE);
                    if (user == null) {
                        setToolbarText(binding.toolbar, getString(R.string.login__login));

                        chooseProfileFragment();

                    } else {
                        setToolbarText(binding.toolbar, getString(R.string.menu__message));

                        navigationController.navigateToMessage(MainActivity.this);
                    }

                    break;

                case R.id.interest_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.GONE);
                    navigationController.navigateToInterest(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__interest));

                    break;

                case R.id.search_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.GONE);
                    navigationController.navigateToFilter(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;

                case R.id.me_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.GONE);
                    if (user == null) {
                        setToolbarText(binding.toolbar, getString(R.string.login__login));
                        chooseProfileFragment();

                    } else {

                        setToolbarText(binding.toolbar, getString(R.string.profile__title));
                        navigationController.navigateToUserProfile(this);

                    }
                    break;

                default:

//                    navigationController.navigateToShopProfile(this);
//                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
            }

            return true;
        });

        binding.addItemButton.setOnClickListener(v -> {
            if (loginUserId.isEmpty()) {

                psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToUserLoginActivity(this);
                });

            } else {

                try {
                    locationId = pref.getString(Constants.SELECTED_LOCATION_ID, Constants.EMPTY_STRING);
                    locationName = pref.getString(Constants.SELECTED_LOCATION_NAME, Constants.EMPTY_STRING);

                } catch (Exception e) {
                    Utils.psErrorLog("", e);
                }


                navigationController.navigateToItemEntryActivity(this, Constants.ADD_NEW_ITEM, locationId, locationName);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        Bundle extra = intent.getExtras();
//        if(extra != null) {
//            notiItemId = extra.getString(Constants.NOTI_ITEM_ID);
//            notiMsg = extra.getString(Constants.NOTI_MSG);
//            notibuyerId = extra.getString(Constants.NOTI_BUYER_ID);
//            notiSellerId = extra.getString(Constants.NOTI_SELLER_ID);
//            notisenderName = extra.getString(Constants.NOTI_SENDER_NAME);
//            notiSenderUrl = extra.getString(Constants.NOTI_SENDER_URL);
//        }
//        userId = pref.getString(Constants.USER_ID,Constants.EMPTY_STRING);
//
//        checkUserId();
//
//        if(notiItemId != null) {
//            navigationController.navigateToChatActivity(MainActivity.this, notiItemId, receiverId, receiverName, "", "", "",
//                    "", "", flag, notiSenderUrl, requestCode);
//        }
//
//        //open from admin noti
//        if((notiItemId == null || notiItemId.isEmpty()) && (notiMsg != null ) ) {
//            showAlertMessage(notiMsg);
//        }
    }

    private void checkUserId() {
        if (!userId.isEmpty()) {
            if (userId.equals(notibuyerId)) {
                receiverId = notiSellerId;
                receiverName = notisenderName;
                receiverUrl = notiSenderUrl;
                requestCode = Constants.REQUEST_CODE__SELLER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_SELLER;
            }
            if (userId.equals(notiSellerId)) {
                receiverId = notibuyerId;
                receiverName = notisenderName;
                receiverUrl = notiSenderUrl;
                requestCode = Constants.REQUEST_CODE__BUYER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_BUYER;
            }

        }
    }

    private void getIntentData() {
        selectedLocationId = getIntent().getStringExtra(Constants.SELECTED_LOCATION_ID);
        selectedLocationName = getIntent().getStringExtra(Constants.SELECTED_LOCATION_NAME);
        selected_lat = getIntent().getStringExtra(Constants.LAT);
        selected_lng = getIntent().getStringExtra(Constants.LNG);

        pref.edit().putString(Constants.SELECTED_LOCATION_ID, selectedLocationId).apply();
        pref.edit().putString(Constants.SELECTED_LOCATION_NAME, selectedLocationName).apply();
        pref.edit().putString(Constants.LAT, selected_lat).apply();
        pref.edit().putString(Constants.LNG, selected_lng).apply();

        notiItemId = getIntent().getStringExtra(Constants.NOTI_ITEM_ID);
        notiMsg = getIntent().getStringExtra(Constants.NOTI_MSG);
        notibuyerId = getIntent().getStringExtra(Constants.NOTI_BUYER_ID);
        notiSellerId = getIntent().getStringExtra(Constants.NOTI_SELLER_ID);
        notisenderName = getIntent().getStringExtra(Constants.NOTI_SENDER_NAME);
        notiSenderUrl = getIntent().getStringExtra(Constants.NOTI_SENDER_URL);

        userId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);

        checkUserId();

        if (notiItemId != null) {
            navigationController.navigateToChatActivity(MainActivity.this, notiItemId, receiverId, receiverName, "", "", "",
                    "", "", flag, notiSenderUrl, requestCode);
        }

        //open from admin noti
        if ((notiItemId == null || notiItemId.isEmpty()) && (notiMsg != null)) {
            showAlertMessage(notiMsg);
        }


    }

    private void initDrawerLayout() {

        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app__drawer_open, R.string.app__drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_grey_24);

        drawerToggle.setToolbarNavigationClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.drawerLayout.addDrawerListener(drawerToggle);
        binding.drawerLayout.post(drawerToggle::syncState);

    }

    private void initNavigationView() {

        if (binding.navView != null) {

            // Updating Custom Fonts
            Menu m = binding.navView.getMenu();
            try {
                if (m != null) {

                    for (int i = 0; i < m.size(); i++) {
                        MenuItem mi = m.getItem(i);

                        //for applying a font to subMenu ...
                        SubMenu subMenu = mi.getSubMenu();
                        if (subMenu != null && subMenu.size() > 0) {
                            for (int j = 0; j < subMenu.size(); j++) {
                                MenuItem subMenuItem = subMenu.getItem(j);

                                subMenuItem.setTitle(subMenuItem.getTitle());
                                // update font

                                subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));

                            }
                        }

                        mi.setTitle(mi.getTitle());
                        // update font

                        mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
                    }
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in Setting Custom Font", e);
            }

            binding.navView.setNavigationItemSelectedListener(menuItem -> {
                navigationMenuChanged(menuItem);
                return true;
            });

        }

        if (binding.bottomNavigationView != null) {

            // Updating Custom Fonts
            Menu m = binding.bottomNavigationView.getMenu();
            try {

                for (int i = 0; i < m.size(); i++) {
                    MenuItem mi = m.getItem(i);

                    //for applying a font to subMenu ...
                    SubMenu subMenu = mi.getSubMenu();
                    if (subMenu != null && subMenu.size() > 0) {
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);

                            subMenuItem.setTitle(subMenuItem.getTitle());
                            // update font

                            subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));

                        }
                    }

                    mi.setTitle(mi.getTitle());
                    // update font

                    mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in Setting Custom Font", e);
            }

            binding.navView.setNavigationItemSelectedListener(menuItem -> {
                navigationMenuChanged(menuItem);
                return true;
            });

        }

    }


    private void hideBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.GONE);
        binding.addItemButton.setVisibility(View.GONE);

        Utils.removeToolbarScrollFlag(binding.toolbar);

    }

    private void showBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        binding.addItemButton.setVisibility(View.VISIBLE);

        Utils.addToolbarScrollFlag(binding.toolbar);

    }

    private void navigationMenuChanged(MenuItem menuItem) {
        openFragment(menuItem.getItemId());

        if (menuItem.getItemId() != R.id.nav_logout_login) {
            menuItem.setChecked(true);
            binding.drawerLayout.closeDrawers();
        }
    }

    public void setSelectMenu(int id) {
        binding.navView.setCheckedItem(id);
    }

    private int menuId = 0;

    /**
     * Open Fragment
     *
     * @param menuId To know which fragment to open.
     */
    private void openFragment(int menuId) {

        this.menuId = menuId;
        switch (menuId) {
            case R.id.nav_home:
            case R.id.nav_home_login:

                setToolbarText(binding.toolbar, Constants.EMPTY_STRING);
                navigationController.navigateToHome(this, false, selectedLocationId, selectedLocationName);
                showBottomNavigation();
                break;

            case R.id.nav_category:
            case R.id.nav_category_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__category));
                navigationController.navigateToCategory(this);
                hideBottomNavigation();
                break;

            case R.id.nav_latest:
            case R.id.nav_latest_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__latest_item));
                navigationController.navigateToHomeLatestFiltering(MainActivity.this, new ItemParameterHolder().getRecentItem());
                hideBottomNavigation();
                break;

//            case R.id.nav_best_thing:
//            case R.id.nav_best_thing_login:
//                setToolbarText(binding.toolbar, getString(R.string.menu__featured_item));
////                navigationController.navigateToFeatureProduct(MainActivity.this, new ItemParameterHolder().getFeaturedItem());
//                hideBottomNavigation();
//                break;

            case R.id.nav_popular:
            case R.id.nav_popular_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__trending_item));
                navigationController.navigateToHomePopularFiltering(MainActivity.this, new ItemParameterHolder().getPopularItem());
                hideBottomNavigation();
                break;

            case R.id.nav_profile:
            case R.id.nav_profile_login:

                if (user == null) {

                    setToolbarText(binding.toolbar, getString(R.string.login__login));

                    chooseProfileFragment();

                } else {
                    setToolbarText(binding.toolbar, getString(R.string.profile__title));

                    navigationController.navigateToUserProfile(this);

                }

                Utils.psLog("nav_profile");

                hideBottomNavigation();

                break;

            case R.id.nav_favourite_news_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__favourite_items));
                navigationController.navigateToFavourite(this);
                Utils.psLog("nav_favourite_news");

                hideBottomNavigation();
                break;

            case R.id.nav_user_history_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__user_history));
                navigationController.navigateToHistory(this);
                Utils.psLog("nav_history");

                hideBottomNavigation();
                break;

            case R.id.nav_logout_login:

                psDialogMsg.showConfirmDialog(getString(R.string.edit_setting__logout_question), getString(R.string.app__ok), getString(R.string.app__cancel));

                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(view -> {

//                    pref.edit().putString(Constants.PROFILE_FRAGMENT_TYPE, Constants.USER_LOGIN_TYPE).apply();

                    psDialogMsg.cancel();

                    hideBottomNavigation();

                    userViewModel.deleteUserLogin(user).observe(this, status -> {
                        if (status != null) {
                            this.menuId = 0;

                            setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                            isLogout = true;

                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                        }
                    });

                    Utils.psLog("nav_logout_login");
                });

                psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

                break;

            case R.id.nav_setting:
            case R.id.nav_setting_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__setting));
                navigationController.navigateToSetting(this);
                Utils.psLog("nav_setting");

                hideBottomNavigation();
                break;

            case R.id.nav_language:
            case R.id.nav_language_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__language));
                navigationController.navigateToLanguageSetting(this);
                Utils.psLog("nav_language");
                hideBottomNavigation();

                break;
            case R.id.nav_rate_this_app:
            case R.id.nav_rate_this_app_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__rate));
                navigationController.navigateToPlayStore(this);
                hideBottomNavigation();

                break;

//            case R.id.nav_chat_login:
////                navigationController.navigateToChatActivity(this, "itm_a65be83fb825406c14760f8848ff7bfa");
//                break;
        }

    }

    private void chooseProfileFragment() {
        String fragmentType = pref.getString(Constants.USER_OLD_ID, Constants.EMPTY_STRING);

        if (fragmentType.isEmpty()) {
            if (user == null) {
                navigationController.navigateToUserLogin(this);
            } else {
                navigationController.navigateToUserProfile(this);
            }
        } else {
            navigationController.navigateToVerifyEmail(this);
        }

    }


    /**
     * Initialize Data
     */
    private void initData() {

        try {
            notiSetting = pref.getBoolean(Constants.NOTI_SETTING, false);
            token = pref.getString(Constants.NOTI_TOKEN, "");

        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }

        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    user = data.get(0).user;

                    pref.edit().putString(Constants.USER_ID, user.userId).apply();
                    pref.edit().putString(Constants.USER_NAME, user.userName).apply();
                    pref.edit().putString(Constants.USER_EMAIL, user.userEmail).apply();
                    pref.edit().putString(Constants.USER_PASSWORD, user.userPassword).apply();

                } else {
                    user = null;

                    pref.edit().remove(Constants.USER_ID).apply();
                    pref.edit().remove(Constants.USER_NAME).apply();
                    pref.edit().remove(Constants.USER_EMAIL).apply();
                    pref.edit().remove(Constants.USER_PASSWORD).apply();
                }

            } else {

                user = null;
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();
                pref.edit().remove(Constants.USER_PASSWORD).apply();

            }
            updateMenu();

            if (isLogout) {
//                setToolbarText(binding.toolbar, getString(R.string.app__app_name));
//                showBottomNavigation();
                navigationController.navigateToHome(MainActivity.this, false, selectedLocationId, selectedLocationName);
                showBottomNavigation();
                isLogout = false;
            }

        });


        registerNotificationToken(); // Just send "" because don't have token to sent. It will get token itself.
    }

    /**
     * This function will change the menu based on the user is logged in or not.
     */
    private void updateMenu() {

        if (user == null) {

            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, false);

            setSelectMenu(R.id.nav_home);

        } else {
            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, false);

            if (menuId == R.id.nav_profile) {
                setSelectMenu(R.id.nav_profile_login);
            } else if (menuId == R.id.nav_profile_login) {
                setSelectMenu(R.id.nav_profile_login);
            } else {
                setSelectMenu(R.id.nav_home_login);
            }

        }


    }

    private void registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notiSetting) {

            if (this.token.equals("")) {

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {

                                return;
                            }

                            // Get new Instance ID token
                            if (task.getResult() != null) {
                                token1 = task.getResult().getToken();
                            }

                            notificationViewModel.registerNotification(getBaseContext(), Constants.PLATFORM, token1);
                        });


            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.");
        }
    }

    private void getCustomLayoutDialog(String message, String itemId) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_noti_alert);

        titleTextView = dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.notification__custom_alert);

        subTitleTextView = dialog.findViewById(R.id.subTitleTextView);
        subTitleTextView.setText(R.string.notification__custom_alert);

        msgTextView = dialog.findViewById(R.id.messageTextView);
        msgTextView.setText(message);

        openItemButton = dialog.findViewById(R.id.openItemButton);
        if (!itemId.isEmpty()) {
            openItemButton.setText(R.string.notification__custom_open);
        } else {
            openItemButton.setText(R.string.app__ok);
        }

        closeButton = dialog.findViewById(R.id.closeButton);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));
        }

        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    //endregion

    public void updateToolbarIconColor(int color) {
        if (yourdrawable != null) {
            yourdrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void updateMenuIconWhite() {
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_white_24);
    }

    public void updateMenuIconGrey() {
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_grey_24);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        yourdrawable = menu.getItem(0).getIcon();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_notification) {

            navigationController.navigateToNotificationList(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

}
