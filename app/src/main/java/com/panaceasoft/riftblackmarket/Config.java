package com.panaceasoft.riftblackmarket;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class Config {


    //AppVersion
    public static String APP_VERSION = "1.0";

    /* APP Setting */
    public static boolean IS_DEVELOPMENT = true; // set false, your app is production

    /* API Related */
    public static final String APP_API_URL = "http://www.panacea-soft.com/buysell-admin/index.php/";

    public static final String APP_IMAGES_URL = "http://www.panacea-soft.com/buysell-admin/uploads/";

    public static final String APP_IMAGES_THUMB_URL = "http://www.panacea-soft.com/buysell-admin/uploads/thumbnail/";

//    public static final String APP_API_URL = "http://192.168.100.198/buysell-admin/index.php/";
//
//    public static final String APP_IMAGES_URL = "http://192.168.100.198/buysell-admin/uploads/";
//
//    public static final String APP_IMAGES_THUMB_URL = "http://192.168.100.198/buysell-admin/uploads/thumbnail/";

    public static final String API_KEY = "teampsisthebest"; // If you change here, you need to update in server.

    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_AR = "ar";
    public static final String LANGUAGE_ES = "es";
    public static final String DEFAULT_LANGUAGE = LANGUAGE_EN;

    /* Loading Limit Count Setting */
    public static final int API_SERVICE_CACHE_LIMIT = 5; // Minutes Cache

    public static int RATING_COUNT = 30;

    public static int ITEM_COUNT = 30;

    public static int LIST_CATEGORY_COUNT = 30;

    public static int LIST_NEW_FEED_COUNT = 30;

    public static int NOTI_LIST_COUNT = 30;

    public static int COMMENT_COUNT = 30;

    public static int LIST_NEW_FEED_COUNT_PAGER = 10;//cannot equal 15

    public static int HISTORY_COUNT = 30;

    public static int CHAT_HISTORY_COUNT = 30;

    public static int LOGIN_USER_ITEM_COUNT = 6;

    public static String MAP_MILES = "8";


    //region playstore

    public static String PLAYSTORE_MARKET_URL_FIX = "market://details?id=";
    public static String PLAYSTORE_HTTP_URL_FIX = "http://play.google.com/store/apps/details?id=";

    //endregion

    //Image Cache and Loading
    public static int IMAGE_CACHE_LIMIT = 250; // Mb
    public static boolean PRE_LOAD_FULL_IMAGE = true;


    /* Admob Setting */
   public static final Boolean SHOW_ADMOB = false;

   public static final String LIMIT_FROM_DB_COUNT = "6";

    //Firebase
    public static final String SUCCESSFULLY_DELTED = "deleted";
    public static final String SUCCESSFULLY_SAVED = "saved";

    //FirebaseTables

    public static final String CHAT = "Chat";
    public static final String MESSAGE = "Message";
    public static final String USER_PRESENCE = "User_Presence";
    public static final String CHAT_WITH = "Current_Chat_With";

    public static final String ACTIVE = "Online (Active)";
    public static final String INACTIVE = "Online (Inactive)";
    public static final String OFFLINE = "OFFLINE";

}
