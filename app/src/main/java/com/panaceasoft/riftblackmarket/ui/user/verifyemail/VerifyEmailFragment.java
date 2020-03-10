package com.panaceasoft.riftblackmarket.ui.user.verifyemail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentVerifyEmailBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;
import com.panaceasoft.riftblackmarket.viewobject.UserLogin;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

public class VerifyEmailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    @VisibleForTesting
    private AutoClearedValue<FragmentVerifyEmailBinding> binding;

//endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVerifyEmailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateMenuIconWhite();
            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().emailTextView.setText(userOldEmail);

        binding.get().submitButton.setOnClickListener(v -> userViewModel.setEmailVerificationUser(Utils.checkUserId(userOldId), binding.get().enterCodeEditText.getText().toString()));

        binding.get().resentCodeButton.setOnClickListener(v -> userViewModel.setResentVerifyCodeObj(userOldEmail));

        binding.get().changeEmailButton.setOnClickListener(v -> navigationController.navigateToUserRegister((MainActivity) getActivity()));
    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        LiveData<Resource<UserLogin>> itemList = userViewModel.getEmailVerificationUser();

        if (itemList != null) {

            itemList.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                Toast.makeText(getContext(), "Loading Loading", Toast.LENGTH_SHORT).show();

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                try {
                                    if (getActivity() != null) {
                                        pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                        pref.edit().putString(Constants.USER_NAME, listResource.data.user.userName).apply();
                                        pref.edit().putString(Constants.USER_EMAIL, listResource.data.user.userEmail).apply();
                                        pref.edit().putString(Constants.USER_PASSWORD, listResource.data.user.userPassword).apply();

                                        pref.edit().putString(Constants.USER_OLD_EMAIL, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_PASSWORD, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_NAME, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_ID, Constants.EMPTY_STRING).apply();

                                    }

                                } catch (NullPointerException ne) {
                                    Utils.psErrorLog("Null Pointer Exception.", ne);
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in getting notification flag data.", e);
                                }

                                if (getActivity() instanceof MainActivity) {
                                    ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                    navigationController.navigateToUserProfile((MainActivity) VerifyEmailFragment.this.getActivity());

                                } else {
                                    try {
                                        if (getActivity() != null) {
                                            getActivity().finish();
                                        }
                                    } catch (Exception e) {
                                        Utils.psErrorLog("Error in closing parent activity.", e);
                                    }
                                }



//                                psDialogMsg.showSuccessDialog("Success Success", getString(R.string.app__ok));
//                                psDialogMsg.show();
//                                psDialogMsg.okButton.setOnClickListener(view -> {
//                                    psDialogMsg.cancel();
//
//
//                                    if(getActivity() != null) {
//                                        getActivity().finish();
//                                    }
//
//                                });
//

                            }

                            break;

                        case ERROR:
                            // Error State
                            psDialogMsg.showErrorDialog(getString(R.string.error_message__code_not_verify),getString(R.string.app__ok));
                            psDialogMsg.show();

                            break;
                        default:
                            // Default

                            break;
                    }

                }

            });
        }



        //For resent code
        userViewModel.getResentVerifyCodeData().observe(this, result -> {

            if(result != null)
            {
                switch (result.status)
                {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Success Success", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Fail resent code again", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


}
