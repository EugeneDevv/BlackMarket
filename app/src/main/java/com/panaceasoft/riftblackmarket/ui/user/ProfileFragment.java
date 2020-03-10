package com.panaceasoft.riftblackmarket.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentProfileBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.ui.item.adapter.ItemVerticalListAdapter;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.item.ItemViewModel;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.User;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.UserParameterHolder;

import java.util.List;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private UserViewModel userViewModel;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileBinding> binding;
    private AutoClearedValue<ItemVerticalListAdapter> adapter;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateMenuIconWhite();
            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().userOwnItemList.setNestedScrollingEnabled(false);
        binding.get().editTextView.setOnClickListener(view -> navigationController.navigateToProfileEditActivity(getActivity()));
        binding.get().seeAllTextView.setOnClickListener(view -> navigationController.navigateToItemListActivity(getActivity(),loginUserId));
        binding.get().favouriteTextView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().heartImageView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().settingTextView.setOnClickListener(view -> navigationController.navigateToSettingActivity(getActivity()));
        binding.get().followingUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowingUsers());
            }
        });
        binding.get().followUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowerUsers());
            }
        });
    }

    @Override
    protected void initViewModels() {
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent, new ItemVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title);
            }
        },this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().userOwnItemList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        //User
        userViewModel.getUser(loginUserId).observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> listResource) {

                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                ProfileFragment.this.fadeIn(binding.get().getRoot());

                                binding.get().setUser(listResource.data);
                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                                ProfileFragment.this.replaceUserData(listResource.data);


                            }

                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                binding.get().setUser(listResource.data);
                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                                ProfileFragment.this.replaceUserData(listResource.data);
                            }

                            break;
                        case ERROR:
                            // Error State

                            psDialogMsg.showErrorDialog(listResource.message, ProfileFragment.this.getString(R.string.app__ok));
                            psDialogMsg.show();

                            userViewModel.isLoading = false;

                            break;
                        default:
                            // Default
                            userViewModel.isLoading = false;

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }

                // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
                // it won't call us if fragment is stopped or not started.
                if (listResource != null && listResource.data != null) {
                    Utils.psLog("Got Data");


                } else {
                    //noinspection Constant Conditions
                    Utils.psLog("Empty Data");

                }
            }
        });



        //Item
        itemViewModel.holder.userId = loginUserId;

        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

        itemViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });
    }

    @Override
    public void onDispatched() {

    }


    private void replaceUserData(User user) {

        binding.get().editTextView.setText(binding.get().editTextView.getText().toString());
//        binding.get().userNotificatinTextView.setText(binding.get().userNotificatinTextView.getText().toString());
//        binding.get().userHistoryTextView.setText(binding.get().userHistoryTextView.getText().toString());
        binding.get().favouriteTextView.setText(binding.get().favouriteTextView.getText().toString());
        binding.get().settingTextView.setText(binding.get().settingTextView.getText().toString());
        binding.get().historyTextView.setText(binding.get().historyTextView.getText().toString());
        binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());
        binding.get().joinedDateTitleTextView.setText(binding.get().joinedDateTitleTextView.getText().toString());
        binding.get().joinedDateTextView.setText(user.addedDate);
        binding.get().nameTextView.setText(user.userName);
        binding.get().overAllRatingTextView.setText(user.overallRating);
        binding.get().ratingBarInformation.setRating(user.ratingDetails.totalRatingValue);
        binding.get().ratingCountTextView.setText(String.format("%s%s%s","(",user.ratingCount,")"));
        binding.get().followUserTextView.setText(String.format("%s%s%s",user.followerCount," ", getString(R.string.profile__followers)));
        binding.get().followingUserTextView.setText(String.format("%s%s%s",user.followingCount," ", getString(R.string.profile__following)));

        if(user.verifyTypes.equals("1")){
            binding.get().facebookImage.setVisibility(View.GONE);
            binding.get().emailImage.setVisibility(View.VISIBLE);
        }if(user.verifyTypes.equals("2")){
            binding.get().facebookImage.setVisibility(View.VISIBLE);
            binding.get().emailImage.setVisibility(View.GONE);
        }

    }

    private void itemReplaceData(List<Item> itemList){
        adapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE__PROFILE_FRAGMENT
                && resultCode == Constants.RESULT_CODE__LOGOUT_ACTIVATED) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                //navigationController.navigateToUserFBRegister((MainActivity) getActivity());
                navigationController.navigateToUserLogin((MainActivity) getActivity());
            }
        }
    }

}
