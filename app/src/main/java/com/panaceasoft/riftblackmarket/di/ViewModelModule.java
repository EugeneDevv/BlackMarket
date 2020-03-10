package com.panaceasoft.riftblackmarket.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.riftblackmarket.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.apploading.PSAPPLoadingViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.blog.BlogViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.chat.ChatViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.chathistory.ChatHistoryViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.city.CityViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.city.FeaturedCitiesViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.city.PopularCitiesViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.city.RecentCitiesViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.comment.CommentDetailListViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.comment.CommentListViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.common.NotificationViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.common.PSNewsViewModelFactory;
import com.panaceasoft.riftblackmarket.viewmodel.contactus.ContactUsViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.image.ImageViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.HistoryViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.PopularItemViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.RecentItemViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.SpecsViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemcondition.ItemConditionViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemcurrency.ItemCurrencyViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemdealoption.ItemDealOptionViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemfromfollower.ItemFromFollowerViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemlocation.ItemLocationViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itempricetype.ItemPriceTypeViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemsubcategory.ItemSubCategoryViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.itemtype.ItemTypeViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.notification.NotificationsViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.psappinfo.PSAppInfoViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.rating.RatingViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Panacea-Soft on 11/16/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PSNewsViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel.class)
    abstract ViewModel bindAboutUsViewModel(AboutUsViewModel aboutUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemLocationViewModel.class)
    abstract ViewModel bindItemLocationViewModel(ItemLocationViewModel itemLocationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemDealOptionViewModel.class)
    abstract ViewModel bindItemDealOptionViewModel(ItemDealOptionViewModel itemDealOptionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemConditionViewModel.class)
    abstract ViewModel bindItemConditionViewModel(ItemConditionViewModel itemConditionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel.class)
    abstract ViewModel bindImageViewModel(ImageViewModel imageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemTypeViewModel.class)
    abstract ViewModel bindItemTypeViewModel(ItemTypeViewModel itemTypeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel.class)
    abstract ViewModel bindRatingViewModel(RatingViewModel ratingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel notificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemFromFollowerViewModel.class)
    abstract ViewModel bindItemFromFollowerViewModel(ItemFromFollowerViewModel itemFromFollowerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemPriceTypeViewModel.class)
    abstract ViewModel bindItemPriceTypeViewModel(ItemPriceTypeViewModel itemPriceTypeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCurrencyViewModel.class)
    abstract ViewModel bindItemCurrencyViewModel(ItemCurrencyViewModel itemCurrencyViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    abstract ViewModel bindContactUsViewModel(ContactUsViewModel contactUsViewModel);

  /*  @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);*/

    @Binds
    @IntoMap
    @ViewModelKey(CommentListViewModel.class)
    abstract ViewModel bindCommentViewModel(CommentListViewModel commentListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CommentDetailListViewModel.class)
    abstract ViewModel bindCommentDetailViewModel(CommentDetailListViewModel commentDetailListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel.class)
    abstract ViewModel bindFavouriteViewModel(FavouriteViewModel favouriteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel.class)
    abstract ViewModel bindTouchCountViewModel(TouchCountViewModel touchCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SpecsViewModel.class)
    abstract ViewModel bindProductSpecsViewModel(SpecsViewModel specsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    abstract ViewModel bindHistoryProductViewModel(HistoryViewModel historyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCategoryViewModel.class)
    abstract ViewModel bindCityCategoryViewModel(ItemCategoryViewModel itemCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    abstract ViewModel bindNotificationListViewModel(NotificationsViewModel notificationListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingCategoryListViewModel.class)
    abstract ViewModel bindHomeTrendingCategoryListViewModel(HomeTrendingCategoryListViewModel transactionListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(BlogViewModel blogViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PSAppInfoViewModel.class)
    abstract ViewModel bindPSAppInfoViewModel(PSAppInfoViewModel psAppInfoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel.class)
    abstract ViewModel bindClearAllDataViewModel(ClearAllDataViewModel clearAllDataViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel.class)
    abstract ViewModel bindCityViewModel(CityViewModel cityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(com.panaceasoft.riftblackmarket.viewmodel.item.ItemViewModel.class)
    abstract ViewModel bindItemViewModel(com.panaceasoft.riftblackmarket.viewmodel.item.ItemViewModel itemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularItemViewModel.class)
    abstract ViewModel bindPopularItemViewModel(PopularItemViewModel popularItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecentItemViewModel.class)
    abstract ViewModel bindRecentItemViewModel(RecentItemViewModel recentItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PSAPPLoadingViewModel.class)
    abstract ViewModel bindPSAPPLoadingViewModel(PSAPPLoadingViewModel psappLoadingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularCitiesViewModel.class)
    abstract ViewModel bindPopularCitiesViewModel(PopularCitiesViewModel popularCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedCitiesViewModel.class)
    abstract ViewModel bindFeaturedCitiesViewModel(FeaturedCitiesViewModel featuredCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecentCitiesViewModel.class)
    abstract ViewModel bindRecentCitiesViewModel(RecentCitiesViewModel recentCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemSubCategoryViewModel.class)
    abstract ViewModel bindItemSubCategoryViewModel(ItemSubCategoryViewModel itemSubCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel bindChatViewModel(ChatViewModel chatViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatHistoryViewModel.class)
    abstract ViewModel bindSellerViewModel(ChatHistoryViewModel chatHistoryViewModel);

}


