package com.panaceasoft.riftblackmarket.ui.common;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.ui.blog.detail.BlogDetailActivity;
import com.panaceasoft.riftblackmarket.ui.blog.list.BlogListActivity;
import com.panaceasoft.riftblackmarket.ui.category.list.CategoryListActivity;
import com.panaceasoft.riftblackmarket.ui.category.list.CategoryListFragment;
import com.panaceasoft.riftblackmarket.ui.chat.chat.ChatActivity;
import com.panaceasoft.riftblackmarket.ui.chat.chatimage.ChatImageFullScreenActivity;
import com.panaceasoft.riftblackmarket.ui.chathistory.MessageFragment;
import com.panaceasoft.riftblackmarket.ui.city.menu.CityMenuFragment;
import com.panaceasoft.riftblackmarket.ui.city.selectedcity.SelectedCityActivity;
import com.panaceasoft.riftblackmarket.ui.city.selectedcity.SelectedCityFragment;
import com.panaceasoft.riftblackmarket.ui.comment.detail.CommentDetailActivity;
import com.panaceasoft.riftblackmarket.ui.customcamera.CameraActivity;
import com.panaceasoft.riftblackmarket.ui.customcamera.setting.CameraSettingActivity;
import com.panaceasoft.riftblackmarket.ui.dashboard.DashBoardSearchFragment;
import com.panaceasoft.riftblackmarket.ui.dashboard.DashboardSearchByCategoryActivity;
import com.panaceasoft.riftblackmarket.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.riftblackmarket.ui.gallery.GalleryActivity;
import com.panaceasoft.riftblackmarket.ui.gallery.detail.GalleryDetailActivity;
import com.panaceasoft.riftblackmarket.ui.item.detail.ItemActivity;
import com.panaceasoft.riftblackmarket.ui.item.entry.ItemEntryActivity;
import com.panaceasoft.riftblackmarket.ui.item.favourite.FavouriteListActivity;
import com.panaceasoft.riftblackmarket.ui.item.favourite.FavouriteListFragment;
import com.panaceasoft.riftblackmarket.ui.item.history.HistoryFragment;
import com.panaceasoft.riftblackmarket.ui.item.itemfromfollower.ItemFromFollowerListActivity;
import com.panaceasoft.riftblackmarket.ui.item.itemtype.SearchViewActivity;
import com.panaceasoft.riftblackmarket.ui.item.loginUserItem.LoginUserItemListActivity;
import com.panaceasoft.riftblackmarket.ui.item.map.MapActivity;
import com.panaceasoft.riftblackmarket.ui.item.map.mapFilter.MapFilteringActivity;
import com.panaceasoft.riftblackmarket.ui.item.rating.RatingListActivity;
import com.panaceasoft.riftblackmarket.ui.item.search.searchlist.SearchListActivity;
import com.panaceasoft.riftblackmarket.ui.item.search.searchlist.SearchListFragment;
import com.panaceasoft.riftblackmarket.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.panaceasoft.riftblackmarket.ui.language.LanguageFragment;
import com.panaceasoft.riftblackmarket.ui.location.LocationActivity;
import com.panaceasoft.riftblackmarket.ui.notification.detail.NotificationActivity;
import com.panaceasoft.riftblackmarket.ui.notification.list.NotificationListActivity;
import com.panaceasoft.riftblackmarket.ui.notification.setting.NotificationSettingActivity;
import com.panaceasoft.riftblackmarket.ui.setting.SettingActivity;
import com.panaceasoft.riftblackmarket.ui.setting.SettingFragment;
import com.panaceasoft.riftblackmarket.ui.setting.appinfo.AppInfoActivity;
import com.panaceasoft.riftblackmarket.ui.subcategory.SubCategoryActivity;
import com.panaceasoft.riftblackmarket.ui.user.PasswordChangeActivity;
import com.panaceasoft.riftblackmarket.ui.user.ProfileEditActivity;
import com.panaceasoft.riftblackmarket.ui.user.ProfileFragment;
import com.panaceasoft.riftblackmarket.ui.user.UserForgotPasswordActivity;
import com.panaceasoft.riftblackmarket.ui.user.UserForgotPasswordFragment;
import com.panaceasoft.riftblackmarket.ui.user.UserLoginActivity;
import com.panaceasoft.riftblackmarket.ui.user.UserLoginFragment;
import com.panaceasoft.riftblackmarket.ui.user.UserRegisterActivity;
import com.panaceasoft.riftblackmarket.ui.user.UserRegisterFragment;
import com.panaceasoft.riftblackmarket.ui.user.userlist.UserListActivity;
import com.panaceasoft.riftblackmarket.ui.user.userlist.detail.UserDetailActivity;
import com.panaceasoft.riftblackmarket.ui.user.verifyemail.VerifyEmailFragment;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.Comment;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.Noti;
import com.panaceasoft.riftblackmarket.viewobject.holder.ItemParameterHolder;
import com.panaceasoft.riftblackmarket.viewobject.holder.UserParameterHolder;

import javax.inject.Inject;

//import com.panaceasoft.riftblackmarket.ui.followinguser.FollowingUserActivity;
//import com.panaceasoft.riftblackmarket.ui.followinguser.detail.FollowingUserDetailActivity;

//import com.panaceasoft.riftblackmarket.ui.city.selectedCity.SelectedCityFragment;

/**
 * Created by Panacea-Soft on 11/17/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class NavigationController {

    //region Variables

    private final int containerId;
    private RegFragments currentFragment;

    //endregion


    //region Constructor
    @Inject
    public NavigationController() {

        // This setup is for MainActivity
        this.containerId = R.id.content_frame;
    }

    //endregion


    //region default navigation

    public void navigateToUserLogin(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                UserLoginFragment fragment = new UserLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToVerifyEmail(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_EMAIL_VERIFY)) {
            try {
                VerifyEmailFragment fragment = new VerifyEmailFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }



    public void navigateToCategoryFragment(SelectedCityActivity selectedCityActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                selectedCityActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeFragment(SelectedCityActivity selectedCityActivity) {
        if (checkFragmentChange(RegFragments.HOME_HOME)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                selectedCityActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToFilteringFragment(FragmentActivity activity) {
        if (checkFragmentChange(RegFragments.HOME_FILTER)) {
            try {
                DashBoardSearchFragment fragment = new DashBoardSearchFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserProfile(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                ProfileFragment fragment = new ProfileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToFavourite(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FAVOURITE)) {
            try {
                FavouriteListFragment fragment = new FavouriteListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToCityMenu(SelectedCityActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CITY_MENU)) {
            try {
                CityMenuFragment fragment = new CityMenuFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

//    public void navigateToTransaction(MainActivity mainActivity) {
//        if (checkFragmentChange(RegFragments.HOME_TRANSACTION)) {
//            try {
//                TransactionListFragment fragment = new TransactionListFragment();
//                mainActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(containerId, fragment)
//                        .commitAllowingStateLoss();
//            } catch (Exception e) {
//                Utils.psErrorLog("Error! Can't replace fragment.", e);
//            }
//        }
//    }

    public void navigateToHistory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_HISTORY)) {
            try {
                HistoryFragment fragment = new HistoryFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToUserRegister(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_REGISTER)) {
            try {
                UserRegisterFragment fragment = new UserRegisterFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserForgotPassword(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_FOGOT_PASSWORD)) {
            try {
                UserForgotPasswordFragment fragment = new UserForgotPasswordFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToSetting(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_SETTING)) {
            try {
                SettingFragment fragment = new SettingFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToLanguageSetting(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_LANGUAGE_SETTING)) {
            try {
                LanguageFragment fragment = new LanguageFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHome(MainActivity mainActivity, boolean forceReplace , String locationId ,String locationName ) {
        if (checkFragmentChange(RegFragments.HOME_HOME) || forceReplace) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

                Bundle args = new Bundle();
                args.putString(Constants.SELECTED_LOCATION_ID,  locationId);
                args.putString(Constants.SELECTED_LOCATION_NAME, locationName);
                fragment.setArguments(args);

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToMessage(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_MESSAGE)) {
            try {
                MessageFragment fragment = new MessageFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToInterest(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToFilter(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FILTER)) {
            try {
                DashBoardSearchFragment fragment = new DashBoardSearchFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToCityList(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CITY_LIST)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToGalleryActivity(Activity activity, String imgType, String imgParentId) {
        Intent intent = new Intent(activity, GalleryActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!imgParentId.equals("")) {
            intent.putExtra(Constants.IMAGE_PARENT_ID, imgParentId);
        }

        activity.startActivity(intent);

    }

    public void navigateToDetailGalleryActivity(Activity activity, String imgType, String newsId, String imgId) {
        Intent intent = new Intent(activity, GalleryDetailActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!newsId.equals("")) {
            intent.putExtra(Constants.ITEM_ID, newsId);
        }

        if (!imgId.equals("")) {
            intent.putExtra(Constants.IMAGE_ID, imgId);
        }

        activity.startActivity(intent);

    }

    public void navigateToCamera(Activity activity, String flag) {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        switch (flag) {
            case Constants.ONE:
                activity.startActivityForResult(cameraIntent, Constants.REQUEST_CODE__FIRST_CAMERA);
                break;
            case Constants.TWO:
                activity.startActivityForResult(cameraIntent, Constants.REQUEST_CODE__SEC_CAMERA);
                break;
            case Constants.THREE:
                activity.startActivityForResult(cameraIntent, Constants.REQUEST_CODE__THIRD_CAMERA);
                break;
            case Constants.FOUR:
                activity.startActivityForResult(cameraIntent, Constants.REQUEST_CODE__FOURTH_CAMERA);
                break;
            case Constants.FIVE:
                activity.startActivityForResult(cameraIntent, Constants.REQUEST_CODE__FIFTH_CAMERA);
                break;
        }

    }

    public void navigateToCustomCamera(Activity activity, String flag) {
        Intent intent = new Intent(activity, CameraActivity.class);
        switch (flag) {
            case Constants.ONE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FIRST_CUSTOM_CAMERA);
                break;
            case Constants.TWO:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__SEC_CUSTOM_CAMERA);
                break;
            case Constants.THREE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__THIRD_CUSTOM_CAMERA);
                break;
            case Constants.FOUR:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FOURTH_CUSTOM_CAMERA);
                break;
            case Constants.FIVE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FIFTH_CUSTOM_CAMERA);
                break;
        }
    }

    public void navigateToGallery(Activity activity, String flag) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (flag) {
            case Constants.ONE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FIRST_GALLERY);
                break;
            case Constants.TWO:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__SEC_GALLERY);
                break;
            case Constants.THREE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__THIRD_GALLERY);
                break;
            case Constants.FOUR:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FOURTH_GALLERY);
                break;
            case Constants.FIVE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FIFTH_GALLERY);
                break;
        }
    }

    public void navigateToSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__PROFILE_FRAGMENT);
    }

    public void navigateToNotificationSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, NotificationSettingActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCameraSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, CameraSettingActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToAppInfoActivity(Activity activity) {
        Intent intent = new Intent(activity, AppInfoActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToProfileEditActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, UserLoginActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, UserRegisterActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserForgotPasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, UserForgotPasswordActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPasswordChangeActivity(Activity activity) {
        Intent intent = new Intent(activity, PasswordChangeActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToNotificationList(Activity activity) {
        Intent intent = new Intent(activity, NotificationListActivity.class);
        activity.startActivity(intent);
    }


    public void navigateToRatingList(Activity activity, Item item) {
        Intent intent = new Intent(activity, RatingListActivity.class);
        intent.putExtra(Constants.ITEM_USER_ID, item.user.userId);
        intent.putExtra(Constants.ITEM_ID, item.id);
        activity.startActivity(intent);
    }


    public void navigateToNotificationDetail(Activity activity, Noti noti, String token) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        intent.putExtra(Constants.NOTI_ID, noti.id);
        intent.putExtra(Constants.NOTI_TOKEN, token);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT);
    }

    public void navigateToCommentDetailActivity(Activity activity, Comment comment) {
        Intent intent = new Intent(activity, CommentDetailActivity.class);
        intent.putExtra(Constants.COMMENT_ID, comment.id);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE__COMMENT_LIST_FRAGMENT);

    }

    public void navigateToItemListActivity(Activity activity,String userId) {
        Intent intent = new Intent(activity, LoginUserItemListActivity.class);
        intent.putExtra(Constants.USER_ID,userId);
        activity.startActivity(intent);
    }

    public void navigateToUserListActivity(Activity activity, UserParameterHolder userParameterHolder) {
        Intent intent = new Intent(activity, UserListActivity.class);
        intent.putExtra(Constants.USER_PARAM_HOLDER_KEY, userParameterHolder);
        activity.startActivity(intent);
    }

    public void navigateToFavouriteActivity(Activity activity) {
        Intent intent = new Intent(activity, FavouriteListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCategoryActivity(Activity activity) {
        Intent intent = new Intent(activity, CategoryListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToItemEntryActivity(Activity activity, String itemId, String locationId, String locationName) {
        Intent intent = new Intent(activity, ItemEntryActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.SELECTED_LOCATION_ID, locationId);
        intent.putExtra(Constants.SELECTED_LOCATION_NAME, locationName);

        activity.startActivity(intent);
    }

    public void navigateToSubCategoryActivity(Activity activity, String catId, String catName) {
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.CATEGORY_NAME, catName);
        activity.startActivity(intent);
    }

    public void navigateToMapActivity(Activity activity, String LNG, String LAT, String flag) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(Constants.LNG, LNG);
        intent.putExtra(Constants.LAT, LAT);
        intent.putExtra(Constants.MAP_FLAG, flag);
        activity.startActivityForResult(intent, Constants.RESULT_CODE__TO_MAP_VIEW);
    }

    public void navigateBackFromMapView(Activity activity, String lat, String lng) {
        Intent intent = new Intent();
        intent.putExtra(Constants.LAT, lat);
        intent.putExtra(Constants.LNG, lng);

        activity.setResult(Constants.RESULT_CODE__FROM_MAP_VIEW, intent);
    }

    public void navigateToTypeFilterFragment(FragmentActivity mainActivity, String
            catId, String subCatId, ItemParameterHolder itemParameterHolder, String name) {

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);
            intent.putExtra(Constants.CATEGORY_ID, catId);
            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        } else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);
            intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);


            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        }

    }

    public void navigateBackToCommentListFragment(FragmentActivity
                                                          commentDetailActivity, String comment_headerId) {
        Intent intent = new Intent();
        intent.putExtra(Constants.COMMENT_HEADER_ID, comment_headerId);

        commentDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent);
    }

    public void navigateBackToProductDetailFragment(FragmentActivity
                                                            productDetailActivity, String product_id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_ID, product_id);

        productDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent);
    }

    public void navigateBackToHomeFeaturedFragment(FragmentActivity mainActivity, String
            catId, String subCatId) {
        Intent intent = new Intent();

        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);

        mainActivity.setResult(Constants.RESULT_CODE__CATEGORY_FILTER, intent);
    }

    public void navigateBackToHomeFeaturedFragmentFromFiltering(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);

        mainActivity.setResult(Constants.RESULT_CODE__SPECIAL_FILTER, intent);
    }

    public void navigateToCategory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeLatestFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_LATEST_PRODUCTS)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToHomePopularFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_POPULAR_CITIES)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeFilteringActivity(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder, String titleName, String itemLat, String itemLng, String mapMiles) {


        if (itemLat != null) {
            itemParameterHolder.lat = itemLat;
            itemParameterHolder.lng = itemLng;
            itemParameterHolder.mapMiles = mapMiles;
        }

        Intent intent = new Intent(mainActivity, SearchListActivity.class);

        intent.putExtra(Constants.ITEM_NAME, titleName);
        intent.putExtra(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);

        mainActivity.startActivity(intent);
    }

    public void navigateToSearchActivityCategoryFragment(FragmentActivity fragmentActivity, String fragName, String catId, String subCatId) {
        Intent intent = new Intent(fragmentActivity, DashboardSearchByCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_FLAG, fragName);

        if (!catId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.CATEGORY_ID, catId);
        }

        if (!subCatId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
        }

        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_FRAGMENT);
    }

    public void navigateToSearchViewActivity(FragmentActivity fragmentActivity, String fragName, String typeId, String priceTypeId, String conditionId, String dealOptionId, String currencyId, String locationId) {
        Intent intent = new Intent(fragmentActivity, SearchViewActivity.class);
        intent.putExtra(Constants.ITEM_TYPE_FLAG, fragName);

        intent.putExtra(Constants.ITEM_TYPE_ID, typeId);
        intent.putExtra(Constants.ITEM_PRICE_TYPE_ID, priceTypeId);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_ID, conditionId);
        intent.putExtra(Constants.ITEM_OPTION_TYPE_ID, dealOptionId);
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_ID, currencyId);

        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        intent.putExtra(Constants.LOCATION_FLAG, Constants.LOCATION_WITH_CLEAR_ICON);

        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT);
    }

    public void navigateBackToSearchFragment(FragmentActivity fragmentActivity, String catId, String cat_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CATEGORY_NAME, cat_Name);
        intent.putExtra(Constants.CATEGORY_ID, catId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_CATEGORY, intent);
    }

    public void navigateBackToSearchFragmentFromSubCategory(FragmentActivity fragmentActivity, String sub_id, String sub_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.SUBCATEGORY_NAME, sub_Name);
        intent.putExtra(Constants.SUBCATEGORY_ID, sub_id);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY, intent);
    }

    public void navigateBackToItemTypeFragment(FragmentActivity fragmentActivity, String typeId, String typeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_TYPE_NAME, typeName);
        intent.putExtra(Constants.ITEM_TYPE_ID, typeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_TYPE, intent);
    }

    public void navigateBackToItemPriceTypeFragment(FragmentActivity fragmentActivity, String priceTypeId, String priceTypeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_PRICE_TYPE_NAME, priceTypeName);
        intent.putExtra(Constants.ITEM_PRICE_TYPE_ID, priceTypeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_PRICE_TYPE, intent);
    }

    public void navigateBackToItemConditionFragment(FragmentActivity fragmentActivity, String priceTypeId, String priceTypeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_NAME, priceTypeName);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_ID, priceTypeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_CONDITION_TYPE, intent);
    }

    public void navigateBackToItemLocationFragment(FragmentActivity fragmentActivity, String locationId, String locationName,String locationLat, String locationLng) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_NAME, locationName);
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        intent.putExtra(Constants.LAT, locationLat);
        intent.putExtra(Constants.LNG, locationLng);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE, intent);
    }

    public void navigateBackToItemCurrencyTypeFragment(FragmentActivity fragmentActivity, String currencyId, String currencySymbol) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_NAME, currencySymbol);
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_ID, currencyId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_CURRENCY_TYPE, intent);
    }

    public void navigateBackToItemDealOptionTypeFragment(FragmentActivity fragmentActivity, String optionId, String optionName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_OPTION_TYPE_NAME, optionName);
        intent.putExtra(Constants.ITEM_OPTION_TYPE_ID, optionId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_OPTION_TYPE, intent);
    }

    public void navigateBackToItemEntryFromCustomCamera(FragmentActivity fragmentActivity, String filePath) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IMAGE_PATH, filePath);

        fragmentActivity.setResult(Constants.RESULT_CODE__ITEM_ENTRY_WITH_CUSTOM_CAMERA, intent);
    }

    public void navigateBackToProfileFragment(FragmentActivity fragmentActivity) {
        Intent intent = new Intent();

        fragmentActivity.setResult(Constants.RESULT_CODE__LOGOUT_ACTIVATED, intent);
    }

    public void navigateBackToChatHistoryListFragment(FragmentActivity fragmentActivity) {

        fragmentActivity.setResult(Constants.RESULT_CODE__CHAT_FRAGMENT, new Intent());
    }

    public void navigateToItemDetailFromHistoryListOnly(Activity activity, String itemId, String itemName) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ZERO);

        activity.startActivity(intent);
    }

    public void navigateToItemDetailActivity(FragmentActivity fragmentActivity, String itemId, String itemName) {

        Intent intent = new Intent(fragmentActivity, ItemActivity.class);

        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToBlogList(FragmentActivity fragmentActivity) {

        Intent intent = new Intent(fragmentActivity, BlogListActivity.class);
        fragmentActivity.startActivity(intent);
    }

    public void navigateToItemListFromFollower(Activity activity) {

        Intent intent = new Intent(activity, ItemFromFollowerListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToBlogDetailActivity(FragmentActivity fragmentActivity, String blogId) {

        Intent intent = new Intent(fragmentActivity, BlogDetailActivity.class);

        intent.putExtra(Constants.BLOG_ID, blogId);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToMainActivity(Activity activity, String selectedLocationId,String selectedLocationName, String lat,String lng ) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Constants.SELECTED_LOCATION_ID,selectedLocationId);
        intent.putExtra(Constants.SELECTED_LOCATION_NAME,selectedLocationName);
        intent.putExtra(Constants.LAT , lat);
        intent.putExtra(Constants.LNG , lng);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT);
    }

    public void navigateToLocationActivity(Activity activity, String flag) {
        Intent intent = new Intent(activity, LocationActivity.class);
        intent.putExtra(Constants.LOCATION_FLAG,flag);
        activity.startActivityForResult(intent,Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT);
    }

    public void navigateToForceUpdateActivity(FragmentActivity fragmentActivity,String title, String msg) {

        Intent intent = new Intent(fragmentActivity, ForceUpdateActivity.class);

        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_MSG, msg);
        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_TITLE,title);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToPlayStore(FragmentActivity fragmentActivity) {
//        try {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_MARKET_URL)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL)));
//        }
//    }
        Uri uri = Uri.parse(Config.PLAYSTORE_MARKET_URL_FIX + fragmentActivity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            fragmentActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL_FIX + fragmentActivity.getPackageName())));
        }
    }

    public void navigateToMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent(activity, MapFilteringActivity.class);

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE__MAP_FILTERING);
    }

    public void navigateBackToSearchFromMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.setResult(Constants.RESULT_CODE__MAP_FILTERING, intent);

        activity.finish();
    }

    public void navigateToUserDetail(FragmentActivity activity, String otherUserId, String otherUserName) {

        Intent intent = new Intent(activity, UserDetailActivity.class);

        intent.putExtra(Constants.OTHER_USER_ID, otherUserId);
        intent.putExtra(Constants.OTHER_USER_NAME, otherUserName);

        activity.startActivity(intent);
    }

    public void navigateToChatActivity(FragmentActivity activity,
                                       String itemId,
                                       String receivedUserId,
                                       String receiverName,
                                       String itemImagePath,
                                       String itemName,
                                       String itemCurrency,
                                       String itemPrice,
                                       String itemConditionName,
                                       String flag,
                                       String receiveUserImage,
                                       int request_code) {

        Intent intent = new Intent(activity, ChatActivity.class);

        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.RECEIVE_USER_ID, receivedUserId);
        intent.putExtra(Constants.RECEIVE_USER_NAME, receiverName);
        intent.putExtra(Constants.RECEIVE_USER_IMG_URL, receiveUserImage);
        intent.putExtra(Constants.IMAGE_PATH, itemImagePath);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.ITEM_PRICE, itemPrice);
        intent.putExtra(Constants.ITEM_CURRENCY, itemCurrency);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_NAME, itemConditionName);
        intent.putExtra(Constants.CHAT_FLAG, flag);

        activity.startActivityForResult(intent, request_code);
    }

    public void navigateToImageFullScreen(FragmentActivity activity, String path) {
        Intent intent = new Intent(activity, ChatImageFullScreenActivity.class);

        intent.putExtra(Constants.IMAGE, path);

        activity.startActivity(intent);
    }

    public void getImageFromGallery(Activity activity) {

        if (Utils.isStoragePermissionGranted(activity)) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activity.startActivityForResult(Intent.createChooser(intent, "Select Photo"), Constants.RESULT_CODE__IMAGE_CATEGORY);
        }

    }

//    public void navigateToFilterFragemtnFromDashBoard(FragmentActivity activity, ItemParameterHolder itemParameterHolder)
//    {
//        Intent intent = new Intent(activity, SearchListActivity.class);
//
//        intent.putExtra(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
//
//        activity.startActivity(intent);
//    }

    //region Private methods
    private Boolean checkFragmentChange(RegFragments regFragments) {
        if (currentFragment != regFragments) {
            currentFragment = regFragments;
            return true;
        }

        return false;
    }


    /**
     * Remark : This enum is only for MainActivity,
     * For the other fragments, no need to register here
     **/
    private enum RegFragments {
        HOME_FRAGMENT,
        HOME_USER_LOGIN,
        HOME_USER_EMAIL_VERIFY,
        HOME_FB_USER_REGISTER,
        HOME_BASKET,
        HOME_USER_REGISTER,
        HOME_USER_FOGOT_PASSWORD,
        HOME_ABOUTUS,
        HOME_CONTACTUS,
        HOME_NOTI_SETTING,
        HOME_APP_INFO,
        HOME_LANGUAGE_SETTING,
        HOME_LATEST_PRODUCTS,
        HOME_DISCOUNT,
        HOME_FEATURED_PRODUCTS,
        HOME_CATEGORY,
        HOME_MESSAGE,
        HOME_SUBCATEGORY,
        HOME_HOME,
        HOME_TRENDINGPRODUCTS,
        HOME_COMMENTLISTS,
        HOME_SEARCH,
        HOME_NOTIFICATION,
        HOME_PRODUCT_COLLECTION,
        HOME_TRANSACTION,
        HOME_HISTORY,
        HOME_SETTING,
        HOME_FAVOURITE,
        HOME_CITY_LIST,
        HOME_CITY_MENU,
        HOME_FILTER,
        HOME_CITIES,
        HOME_POPULAR_CITIES,
        HOME_RECOMMENDED_CITIES

    }
}
