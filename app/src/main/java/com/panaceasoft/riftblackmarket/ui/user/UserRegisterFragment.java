package com.panaceasoft.riftblackmarket.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentUserRegisterBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;
import com.panaceasoft.riftblackmarket.viewobject.User;

/**
 * UserRegisterFragment
 */
public class UserRegisterFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserRegisterBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            if (connectivity.isConnected()) {

                if (getActivity() instanceof MainActivity) {
                    navigationController.navigateToUserLogin((MainActivity) getActivity());
                } else {

                    navigationController.navigateToUserLoginActivity(getActivity());

                    try {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    } catch (Exception e) {
                        Utils.psErrorLog("Error in closing activity.", e);
                    }
                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));

                psDialogMsg.show();
            }

        });

        binding.get().registerButton.setOnClickListener(view -> UserRegisterFragment.this.registerUser());

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

        bindingData();

        userViewModel.getRegisterUser().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            if (getActivity() != null) {
                                pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                pref.edit().putString(Constants.USER_NAME, listResource.data.userName).apply();
                                pref.edit().putString(Constants.USER_EMAIL, listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_PASSWORD, binding.get().passwordEditText.getText().toString()).apply();

                                pref.edit().putString(Constants.USER_OLD_EMAIL,listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_OLD_PASSWORD,binding.get().passwordEditText.getText().toString()).apply();
                                pref.edit().putString(Constants.USER_OLD_NAME,listResource.data.userName).apply();
                                pref.edit().putString(Constants.USER_OLD_ID,listResource.data.userId).apply();

                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();
                            updateRegisterBtnStatus();

                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.verify_email));

                                  navigationController.navigateToVerifyEmail((MainActivity) getActivity());
                            } else {
                                try {
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in closing parent activity.", e);
                                }
                            }
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showWarningDialog(getString(R.string.error_message__email_exists), getString(R.string.app__ok));
                        binding.get().registerButton.setText(getResources().getString(R.string.register__register));
                        psDialogMsg.show();

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });
    }

    private void bindingData() {

        if(!userOldEmail.isEmpty()){
            binding.get().emailEditText.setText(userOldEmail);
        }
        if(!userOldPassword.isEmpty()){
            binding.get().passwordEditText.setText(userOldPassword);
        }
        if(!userOldName.isEmpty()){
            binding.get().nameEditText.setText(userOldName);
        }
    }

    //endregion


    //region Private Methods

    private void updateRegisterBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().registerButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
        }
    }

    private void registerUser() {

        Utils.hideKeyboard(getActivity());

        String userName = binding.get().nameEditText.getText().toString().trim();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        String userEmail = binding.get().emailEditText.getText().toString().trim();
        if (userEmail.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        String userPassword = binding.get().passwordEditText.getText().toString().trim();
        if (userPassword.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }


        userViewModel.isLoading = true;
        updateRegisterBtnStatus();

        String token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        userViewModel.setRegisterUser(new User(
                "",
                "",
                "",
                "",
                userName,
                userEmail,
                "",
                userPassword,
                "",
                "",
                "",
                "",
                "",
                "",
                "", token, "", "", "", "","","","","","","",null));
    }

    //endregion

}

