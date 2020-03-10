package com.panaceasoft.riftblackmarket.ui.item.detail;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentItemBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.utils.ViewAnimationUtil;
import com.panaceasoft.riftblackmarket.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.ItemViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.SpecsViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.rating.RatingViewModel;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.common.Status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private TouchCountViewModel touchCountViewModel;
    private FavouriteViewModel favouriteViewModel;
    private SpecsViewModel specsViewModel;
    private RatingViewModel ratingViewModel;
    private PSDialogMsg psDialogMsg;
    private ImageView imageView;
    private Item item;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemBinding> binding;
    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        imageView = binding.get().coverUserImageView;

        item = binding.get().getItem();

        return binding.get().getRoot();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().userCardView.setOnClickListener(v -> navigationController.navigateToUserDetail(getActivity(), itemViewModel.otherUserId, itemViewModel.otherUserName));

        binding.get().menuImageView.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(getActivity(), binding.get().menuImageView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {

                if(item.getTitle().toString().equals(getString(R.string.menu__report_item))){

                    itemViewModel.setReportItemStatusObj(itemViewModel.itemId , loginUserId);
                }else {//share

                    Bitmap bitmap = getBitmapFromView(getCurrentImageView());
                    shareImageUri(saveImageExternal(bitmap));
                }
                return false;
            });

            popupMenu.show();

        });

        binding.get().countPhotoConstraint.setOnClickListener(v -> navigationController.navigateToGalleryActivity(ItemFragment.this.getActivity(),Constants.IMAGE_TYPE_PRODUCT,itemViewModel.itemId));

        binding.get().coverUserImageView.setOnClickListener(v -> navigationController.navigateToGalleryActivity(ItemFragment.this.getActivity(),Constants.IMAGE_TYPE_PRODUCT,itemViewModel.itemId));

        binding.get().editButton.setOnClickListener(v -> navigationController.navigateToItemEntryActivity(getActivity(),itemViewModel.itemId, itemViewModel.locationId, itemViewModel.locationName));

        binding.get().ratingBarInformation.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                navigationController.navigateToRatingList(ItemFragment.this.getActivity(), binding.get().getItem());
            }
            return true;
        });

        binding.get().backImageView.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        binding.get().statisticDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().statisticTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().statisticDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().meetTheSellerDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().userImageView);
                ViewAnimationUtil.expand(binding.get().facebookImageView);
                ViewAnimationUtil.expand(binding.get().mailImageView);
                ViewAnimationUtil.expand(binding.get().userNameTextView);
                ViewAnimationUtil.expand(binding.get().ratingCountTextView);
                ViewAnimationUtil.expand(binding.get().ratingBarInformation);
                ViewAnimationUtil.expand(binding.get().ratingInfoTextView);
                ViewAnimationUtil.expand(binding.get().joinedTextView);
                ViewAnimationUtil.expand(binding.get().verifiedTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().userImageView);
                ViewAnimationUtil.collapse(binding.get().facebookImageView);
                ViewAnimationUtil.collapse(binding.get().mailImageView);
                ViewAnimationUtil.collapse(binding.get().userNameTextView);
                ViewAnimationUtil.collapse(binding.get().ratingCountTextView);
                ViewAnimationUtil.collapse(binding.get().ratingBarInformation);
                ViewAnimationUtil.collapse(binding.get().ratingInfoTextView);
                ViewAnimationUtil.collapse(binding.get().joinedTextView);
                ViewAnimationUtil.collapse(binding.get().verifiedTextView);
            }
        });

        binding.get().meetTheSellerTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().meetTheSellerDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().userImageView);
                ViewAnimationUtil.expand(binding.get().facebookImageView);
                ViewAnimationUtil.expand(binding.get().mailImageView);
                ViewAnimationUtil.expand(binding.get().userNameTextView);
                ViewAnimationUtil.expand(binding.get().ratingCountTextView);
                ViewAnimationUtil.expand(binding.get().ratingBarInformation);
                ViewAnimationUtil.expand(binding.get().ratingInfoTextView);
                ViewAnimationUtil.expand(binding.get().joinedTextView);
                ViewAnimationUtil.expand(binding.get().verifiedTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().userImageView);
                ViewAnimationUtil.collapse(binding.get().facebookImageView);
                ViewAnimationUtil.collapse(binding.get().mailImageView);
                ViewAnimationUtil.collapse(binding.get().userNameTextView);
                ViewAnimationUtil.collapse(binding.get().ratingCountTextView);
                ViewAnimationUtil.collapse(binding.get().ratingBarInformation);
                ViewAnimationUtil.collapse(binding.get().ratingInfoTextView);
                ViewAnimationUtil.collapse(binding.get().joinedTextView);
                ViewAnimationUtil.collapse(binding.get().verifiedTextView);
            }
        });

        binding.get().gettingThisDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().meetTextView);
                ViewAnimationUtil.expand(binding.get().addressTextView);
                ViewAnimationUtil.expand(binding.get().imageView25);
            } else {
                ViewAnimationUtil.collapse(binding.get().meetTextView);
                ViewAnimationUtil.collapse(binding.get().addressTextView);
                ViewAnimationUtil.collapse(binding.get().imageView25);
            }
        });

        binding.get().gettingThisTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().gettingThisDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().meetTextView);
                ViewAnimationUtil.expand(binding.get().addressTextView);
                ViewAnimationUtil.expand(binding.get().imageView25);
            } else {
                ViewAnimationUtil.collapse(binding.get().meetTextView);
                ViewAnimationUtil.collapse(binding.get().addressTextView);
                ViewAnimationUtil.collapse(binding.get().imageView25);
            }
        });

        binding.get().favouriteImageView.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    favFunction(item, likeButton);
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    unFavFunction(item, likeButton);
                }
            }
        });

        binding.get().chatButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Clicked chat button", Toast.LENGTH_SHORT).show();

            if (loginUserId.equals("")) {
                navigationController.navigateToUserLoginActivity(getActivity());
            }
            else if (itemViewModel.currentItem.user.userId.isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_user_not_exit),getString(R.string.app__ok));
                psDialogMsg.show();
            }
            else {
                navigationController.navigateToChatActivity(getActivity(),
                        itemViewModel.currentItem.id,
                        itemViewModel.currentItem.user.userId,
                        itemViewModel.currentItem.user.userName,
                        itemViewModel.currentItem.defaultPhoto.imgPath,
                        itemViewModel.currentItem.title,
                        itemViewModel.currentItem.itemCurrency.currencySymbol,
                        itemViewModel.currentItem.price,
                        itemViewModel.currentItem.itemCondition.name,
                        Constants.CHAT_FROM_SELLER,
                        itemViewModel.currentItem.user.userProfilePhoto,
                        0
                );
            }


        });

        binding.get().ratingBarInformation.setOnClickListener(v -> navigationController.navigateToRatingList(ItemFragment.this.getActivity(), item));
    }


    @Override
    protected void initViewModels() {
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        ratingViewModel = ViewModelProviders.of(this, viewModelFactory).get(RatingViewModel.class);
        specsViewModel = ViewModelProviders.of(this, viewModelFactory).get(SpecsViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        getIntentData();

        getItemDetail();

        getFavData();

        getTouchCount();

        getReportItemStatus();

    }

    private void getReportItemStatus() {

        itemViewModel.getReportItemStatusData().observe(this, result -> {

            if(result != null)
            {
                switch (result.status)
                {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Success Report this Item", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Fail Report this item", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void setTouchCount() {

        if (connectivity.isConnected()) {
            touchCountViewModel.setTouchCountPostDataObj(loginUserId, itemViewModel.itemId);
        }
    }

    private void getTouchCount() {
        setTouchCount();
        //get touch count post method
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void getFavData() {
        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void getIntentData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemViewModel.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    itemViewModel.historyFlag = getActivity().getIntent().getExtras().getString(Constants.HISTORY_FLAG);

                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    private void shareImageUri(Uri uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                Objects.requireNonNull(getContext()).startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmapFromView(ImageView view) {
        Drawable drawable = view.getDrawable();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private ImageView getCurrentImageView() {
        return imageView;
    }

    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void getItemDetail() {

        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

        LiveData<Resource<Item>> itemDetail = itemViewModel.getItemDetailData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);

                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);
                                bindingRatingData(listResource.data);
                                bindingCountData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingFavouriteCount(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBusinessMode(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);

                                // Update the data
                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);

                                bindingRatingData(listResource.data);
                                bindingCountData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingFavouriteCount(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBusinessMode(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);
                                itemViewModel.locationId = listResource.data.itemLocation.id;
                                itemViewModel.locationName = listResource.data.itemLocation.name;
                                itemViewModel.otherUserId = listResource.data.user.userId;
                                itemViewModel.otherUserName = listResource.data.user.userName;
//                                checkText(listResource.data);

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }

                } else {

                    itemViewModel.setLoadingState(false);

                }
            });
        }

        //get rating post method
        ratingViewModel.getRatingPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                        navigationController.navigateToRatingList(ItemFragment.this.getActivity(), item);
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }
                }
            }
        });


        //load product specs

//        LiveData<List<ItemSpecs>> itemSpecs = specsViewModel.getSpecsListData();
//        if(itemSpecs != null) {
//            itemSpecs.observe(this, listResource -> {
//                if (listResource != null && listResource.size() > 0) {
//
//                    ItemFragment.this.replaceItemSpecsData(listResource);
//                    specsViewModel.isSpecsData = true;
//
//                } else {
//                    specsViewModel.isSpecsData = false;
//                    binding.get().fiveCardView.setVisibility(View.GONE);
//
//                }
//                showOrHideSpecs();
//            });
//        }
//    }

//    private void bindingMapData(Item item) {
//        itemViewModel.latValue = item.lat;
//        itemViewModel.lngValue = item.lng;

//        //load product specs
//
//        LiveData<List<ItemSpecs>> itemSpecs = specsViewModel.getSpecsListData();
//        if(itemSpecs != null) {
//            itemSpecs.observe(this, listResource -> {
//                if (listResource != null && listResource.size() > 0) {
//
//                    ItemFragment.this.replaceItemSpecsData(listResource);
//                    specsViewModel.isSpecsData = true;
//
//                } else {
//                    specsViewModel.isSpecsData = false;
//                    binding.get().fiveCardView.setVisibility(View.GONE);
//
//                }
//                showOrHideSpecs();
//            });
//        }

    }

    private void replaceItemData(Item item) {
        itemViewModel.currentItem = item;
        binding.get().setItem(item);

    }

    private void bindingCountData(Item item) {
        binding.get().favouriteCountTextView.setText(getString(R.string.item_detail__fav_count, item.favouriteCount));
        binding.get().viewCountTextView.setText(getString(R.string.item_detail__view_count, item.touchCount));
    }

    private void bindingCategoryNameAndSubCategoryName(Item item) {
        String categoryName = item.category.name;
        String subCategoryName = item.subCategory.name;

        if(categoryName.equals("")){
            binding.get().categoryAndSubCategoryTextView.setText(subCategoryName);
        }
        else if(subCategoryName.equals("")){
            binding.get().categoryAndSubCategoryTextView.setText(categoryName);
        }
        else {
            String name = categoryName + " / " + subCategoryName;
            binding.get().categoryAndSubCategoryTextView.setText(name);
        }

    }

    private void bindingPriceWithCurrencySymbol(Item item) {
        String currencySymbol = item.itemCurrency.currencySymbol;
        String price = item.price;
        String currencyPrice = currencySymbol +""+ price;
        binding.get().priceTextView.setText(currencyPrice);
    }

    private void bindingRatingData(Item item) {

        if (item.user.overallRating.isEmpty()) {
            binding.get().ratingCountTextView.setText(getString(R.string.item_detail__rating));
        } else {
            binding.get().ratingCountTextView.setText(item.user.overallRating);
        }

        if(!item.user.overallRating.isEmpty()) {
            binding.get().ratingBarInformation.setRating(item.user.ratingDetails.totalRatingValue);
        }

        binding.get().ratingInfoTextView.setText(String.format("%s%s%s","(",item.user.ratingCount,")"));

    }

    private void bindingVerifiedData(Item item){
        if(item.user.verifyTypes.equals("1")){
            binding.get().facebookImageView.setVisibility(View.GONE);
            binding.get().mailImageView.setVisibility(View.VISIBLE);
        }if(item.user.verifyTypes.equals("2")){
            binding.get().facebookImageView.setVisibility(View.VISIBLE);
            binding.get().mailImageView.setVisibility(View.GONE);
        }
    }
    private void bindingFavouriteCount(Item item) {
        binding.get().likesTextView.setText(String.format("%s%s%s", item.favouriteCount, " ",getString(R.string.item_detail__like)));

    }

    private void bindingFavoriteData(Item item) {
        if (item.isFavourited.equals(Constants.ONE)) {
            binding.get().favouriteImageView.setLiked(true);
        } else {
            binding.get().favouriteImageView.setLiked(false);
        }
    }

    private void bindingSoldData(Item item) {
        if (item.isSoldOut.equals(Constants.ONE)) {
            binding.get().soldTextView.setText(getString(R.string.item_detail__sold));
        } else {
            binding.get().soldTextView.setVisibility(View.GONE);
        }
    }

    private void bindingPhotoCount(Item item){
        if(item.photoCount.equals("1")){
            binding.get().photoCountTextView.setText(String.format("%s%s%s", item.photoCount," ", getString(R.string.item_detail__photo)));
        }
        else{
            binding.get().photoCountTextView.setText(String.format("%s%s%s",item.photoCount," ",getString(R.string.item_detail__photos)));
        }
    }
    private void bindingBusinessMode(Item item) {
        if (item.businessMode.equals("0")) {
            binding.get().orderTextView.setText(getString(R.string.item_detail__order_not_more_than_one));
        }
        if (item.businessMode.equals("1")) {
            binding.get().orderTextView.setText(getString(R.string.item_detail__order_more_than_one));
        }
    }

    private void bindindAddedDateUserName(Item item){
        binding.get().activeHourTextView.setText( item.addedDateStr);
        binding.get().userNameActiveHourTextView.setText(item.user.userName);
    }
    private void bindingBottomConstraintLayout(Item item){
        if(item.isOwner.equals(Constants.ONE)){
            binding.get().itemOwnerConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.GONE);
        }
        else{
            binding.get().itemSupplierConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().itemOwnerConstraintLayout.setVisibility(View.GONE);
        }
    }
    private void unFavFunction(Item item, LikeButton likeButton) {
        if (loginUserId.equals("")) {
            navigationController.navigateToUserLoginActivity(getActivity());
            likeButton.setLiked(false);
        } else {
            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }
        }
    }

    private void favFunction(Item item, LikeButton likeButton) {
        if (loginUserId.equals("")) {
            likeButton.setLiked(false);
            navigationController.navigateToUserLoginActivity(getActivity());
        } else {
            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }
        }
    }

    private void showOrHide(Item item) {

        if(item.addedDateStr.equals("")){
            binding.get().activeHourTextView.setVisibility(View.GONE);
            binding.get().imageView16.setVisibility(View.GONE);
        }else{
            binding.get().activeHourTextView.setVisibility(View.VISIBLE);
            binding.get().imageView16.setVisibility(View.VISIBLE);
        }
        if (item.price.equals("")) {
            binding.get().priceTextView.setVisibility(View.GONE);
            binding.get().imageView17.setVisibility(View.GONE);
        } else {
            binding.get().priceTextView.setVisibility(View.VISIBLE);
            binding.get().imageView17.setVisibility(View.VISIBLE);
        }
        if (item.favouriteCount.equals("")) {
            binding.get().likesTextView.setVisibility(View.GONE);
            binding.get().imageView22.setVisibility(View.GONE);
        } else {
            binding.get().likesTextView.setVisibility(View.VISIBLE);
            binding.get().imageView22.setVisibility(View.VISIBLE);
        }
        if(item.itemCondition.name.equals("")){
            binding.get().newTextView.setVisibility(View.GONE);
            binding.get().imageView18.setVisibility(View.GONE);
        }else{
            binding.get().newTextView.setVisibility(View.VISIBLE);
            binding.get().imageView18.setVisibility(View.VISIBLE);
        }

        if (item.category.name.equals("") && item.subCategory.name.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setVisibility(View.GONE);
            binding.get().imageView23.setVisibility(View.GONE);
        } else {
            binding.get().categoryAndSubCategoryTextView.setVisibility(View.VISIBLE);
            binding.get().imageView23.setVisibility(View.VISIBLE);
        }
        if (item.highlightInfo.equals("")) {
            binding.get().highlightInfoTextView.setVisibility(View.GONE);
            binding.get().imageView26.setVisibility(View.GONE);
        } else {
            binding.get().highlightInfoTextView.setVisibility(View.VISIBLE);
            binding.get().imageView26.setVisibility(View.VISIBLE);
        }

        if (item.brand.equals("")) {
            binding.get().brandTextView.setVisibility(View.GONE);
            binding.get().imageView24.setVisibility(View.GONE);
        } else {
            binding.get().brandTextView.setVisibility(View.VISIBLE);
            binding.get().imageView24.setVisibility(View.VISIBLE);
        }

        if (item.itemType.name.equals("")) {
            binding.get().saleBuyTextView.setVisibility(View.GONE);
            binding.get().imageView27.setVisibility(View.GONE);
        } else {
            binding.get().saleBuyTextView.setVisibility(View.VISIBLE);
            binding.get().imageView27.setVisibility(View.VISIBLE);
        }

        if (item.businessMode.equals("1") || item.businessMode.equals("0")) {
            binding.get().orderTextView.setVisibility(View.VISIBLE);
            binding.get().imageView29.setVisibility(View.VISIBLE);
        } else {
            binding.get().orderTextView.setVisibility(View.GONE);
            binding.get().imageView29.setVisibility(View.GONE);
        }

        if (item.description.equals("")) {
            binding.get().informationTextView.setVisibility(View.GONE);
            binding.get().imageView30.setVisibility(View.GONE);
        } else {
            binding.get().informationTextView.setVisibility(View.VISIBLE);
            binding.get().imageView30.setVisibility(View.VISIBLE);
        }

        if(item.addedUserId.equals(loginUserId)) {
            binding.get().editButton.setVisibility(View.VISIBLE);
            binding.get().chatButton.setVisibility(View.GONE);
        }else {
            binding.get().editButton.setVisibility(View.GONE);
            binding.get().chatButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
        if (loginUserId != null) {
            itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
        }
        psDialogMsg.cancel();
//        binding.get().rating.setRating(0);
    }


}
