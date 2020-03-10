package com.panaceasoft.riftblackmarket.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.panaceasoft.riftblackmarket.db.common.Converters;
import com.panaceasoft.riftblackmarket.viewobject.AboutUs;
import com.panaceasoft.riftblackmarket.viewobject.Blog;
import com.panaceasoft.riftblackmarket.viewobject.ChatHistory;
import com.panaceasoft.riftblackmarket.viewobject.ChatHistoryMap;
import com.panaceasoft.riftblackmarket.viewobject.City;
import com.panaceasoft.riftblackmarket.viewobject.CityMap;
import com.panaceasoft.riftblackmarket.viewobject.Comment;
import com.panaceasoft.riftblackmarket.viewobject.CommentDetail;
import com.panaceasoft.riftblackmarket.viewobject.DeletedObject;
import com.panaceasoft.riftblackmarket.viewobject.Image;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.ItemCategory;
import com.panaceasoft.riftblackmarket.viewobject.ItemCollection;
import com.panaceasoft.riftblackmarket.viewobject.ItemCollectionHeader;
import com.panaceasoft.riftblackmarket.viewobject.ItemCondition;
import com.panaceasoft.riftblackmarket.viewobject.ItemCurrency;
import com.panaceasoft.riftblackmarket.viewobject.ItemDealOption;
import com.panaceasoft.riftblackmarket.viewobject.ItemFavourite;
import com.panaceasoft.riftblackmarket.viewobject.ItemFromFollower;
import com.panaceasoft.riftblackmarket.viewobject.ItemHistory;
import com.panaceasoft.riftblackmarket.viewobject.ItemLocation;
import com.panaceasoft.riftblackmarket.viewobject.ItemMap;
import com.panaceasoft.riftblackmarket.viewobject.ItemPriceType;
import com.panaceasoft.riftblackmarket.viewobject.ItemSpecs;
import com.panaceasoft.riftblackmarket.viewobject.ItemSubCategory;
import com.panaceasoft.riftblackmarket.viewobject.ItemType;
import com.panaceasoft.riftblackmarket.viewobject.Noti;
import com.panaceasoft.riftblackmarket.viewobject.PSAppInfo;
import com.panaceasoft.riftblackmarket.viewobject.PSAppSetting;
import com.panaceasoft.riftblackmarket.viewobject.PSAppVersion;
import com.panaceasoft.riftblackmarket.viewobject.Rating;
import com.panaceasoft.riftblackmarket.viewobject.User;
import com.panaceasoft.riftblackmarket.viewobject.UserLogin;
import com.panaceasoft.riftblackmarket.viewobject.UserMap;
import com.panaceasoft.riftblackmarket.viewobject.messageHolder.Message;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        ItemFavourite.class,
        Comment.class,
        CommentDetail.class,
        Noti.class,
        ItemHistory.class,
        Blog.class,
        Rating.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class,
        City.class,
        CityMap.class,
        Item.class,
        ItemMap.class,
        ItemCategory.class,
        ItemCollectionHeader.class,
        ItemCollection.class,
        ItemSubCategory.class,
        ItemSpecs.class,
        ItemCurrency.class,
        ItemPriceType.class,
        ItemType.class,
        ItemLocation.class,
        ItemDealOption.class,
        ItemCondition.class,
        ItemFromFollower.class,
        Message.class,
        ChatHistory.class,
        ChatHistoryMap.class,
        PSAppSetting.class,
        UserMap.class


}, version = 1, exportSchema = false)

@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public UserMapDao userMapDao();

    abstract public HistoryDao historyDao();

    abstract public SpecsDao specsDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public ItemDealOptionDao itemDealOptionDao();

    abstract public ItemConditionDao itemConditionDao();

    abstract public ItemLocationDao itemLocationDao();

    abstract public ItemCurrencyDao itemCurrencyDao();

    abstract public ItemPriceTypeDao itemPriceTypeDao();

    abstract public ItemTypeDao itemTypeDao();

    abstract public RatingDao ratingDao();

    abstract public CommentDao commentDao();

    abstract public CommentDetailDao commentDetailDao();

    abstract public NotificationDao notificationDao();

    abstract public BlogDao blogDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();

    abstract public CityDao cityDao();

    abstract public CityMapDao cityMapDao();

    abstract public ItemDao itemDao();

    abstract public ItemMapDao itemMapDao();

    abstract public ItemCategoryDao itemCategoryDao();

    abstract public ItemCollectionHeaderDao itemCollectionHeaderDao();

    abstract public ItemSubCategoryDao itemSubCategoryDao();

    abstract public ChatHistoryDao chatHistoryDao();

    abstract public MessageDao messageDao();



//    /**
//     * Migrate from:
//     * version 1 - using Room
//     * to
//     * version 2 - using Room where the {@link } has an extra field: addedDateStr
//     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}