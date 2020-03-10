package com.panaceasoft.riftblackmarket.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentUserLoginBinding;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.user.UserViewModel;
import com.panaceasoft.riftblackmarket.viewobject.User;

import org.json.JSONException;


/**
 * UserLoginFragment
 */
public class UserLoginFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserLoginBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    private CallbackManager callbackManager;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        FacebookSdk.sdkInitialize(getContext());

        callbackManager = CallbackManager.Factory.create();
        // Inflate the layout for this fragment
        FragmentUserLoginBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            Utils.hideKeyboard(getActivity());

            if (connectivity.isConnected()) {
                String userEmail = binding.get().emailEditText.getText().toString().trim();
                String userPassword = binding.get().passwordEditText.getText().toString().trim();

                Utils.psLog("Email " + userEmail);
                Utils.psLog("Password " + userPassword);

                if (userEmail.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }

                if (userPassword.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }

                if (!userViewModel.isLoading) {

                    updateLoginBtnStatus();

                    doSubmit(userEmail, userPassword);

                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });

        binding.get().registerButton.setOnClickListener(view -> {

            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserRegister((MainActivity) getActivity());
            } else {

                navigationController.navigateToUserRegisterActivity(getActivity());

                try {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in closing activity.", e);
                }
            }
        });

        binding.get().forgotPasswordButton.setOnClickListener(view -> {

            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserForgotPassword((MainActivity) getActivity());
            } else {

                navigationController.navigateToUserForgotPasswordActivity(getActivity());

                try {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in closing activity.", e);
                }
            }
        });
    }

    private void updateLoginBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().loginButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().loginButton.setText(getResources().getString(R.string.login__login));
        }
    }

    private void doSubmit(String email, String password) {

        String token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        //prgDialog.get().show();
        userViewModel.setUserLogin(new User(
                "",
                "",
                "",
                "",
                email,
                email,
                "",
                password,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                token,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null));

        userViewModel.isLoading = true;

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

        userViewModel.getLoadingState().observe(this, loadingState -> {

            if (loadingState != null && loadingState) {
                prgDialog.get().show();
            } else {
                prgDialog.get().cancel();
            }

            updateLoginBtnStatus();

        });

        userViewModel.getUserLoginStatus().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

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
                                    pref.edit().putString(Constants.USER_PASSWORD, binding.get().passwordEditText.getText().toString()).apply();
                                }

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                navigationController.navigateToUserProfile((MainActivity) getActivity());
                            } else {
                                try {
                                    if(getActivity() != null) {
                                        getActivity().finish();
                                    }
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in closing parent activity.", e);
                                }
                            }

                        }

                        userViewModel.setLoadingState(false);

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });

        String token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        binding.get().fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {

                            String name = "";
                            String email = "";
                            String id = "";
                            String imageURL = "";
                            try {
                                if (object != null) {

                                    name = object.getString("name");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    email = object.getString("email");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    id = object.getString("id");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (!id.equals("")) {
                                prgDialog.get().show();
                                userViewModel.registerFBUser(id, name, email, imageURL, token);
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,name,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Utils.psLog("OnCancel.");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Utils.psLog("OnError." + e);
            }


        });


        userViewModel.getRegisterFBUserData().observe(this, listResource -> {

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
                            try {
                                if (getActivity() != null) {
                                    pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                }

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();


                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                navigationController.navigateToUserProfile((MainActivity) getActivity());
                            } else {
                                try {
                                    if(getActivity() != null) {
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

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        //userViewModel.isLoading = false;
                        //prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

        // Tmp Later to delete
        userViewModel.getUserLoginStatus().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                if (getActivity() != null) {

                                    pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();

                                }

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.setLoadingState(false);

                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                navigationController.navigateToUserProfile((MainActivity) getActivity());
                            } else {
                                try {
                                    if(getActivity() != null) {
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

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


//endregion

