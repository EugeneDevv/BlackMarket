package com.panaceasoft.riftblackmarket.ui.language;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.MainActivity;
import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentLanguageBinding;
import com.panaceasoft.riftblackmarket.ui.apploading.AppLoadingActivity;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.PSDialogMsg;

import javax.inject.Inject;


public class LanguageFragment extends PSFragment {

    //region Variables

    @Inject
    SharedPreferences sharedPreferences;

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String LANG_CURRENT = Config.DEFAULT_LANGUAGE;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentLanguageBinding> binding;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentLanguageBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_language, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        LANG_CURRENT = sharedPreferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        switch (LANG_CURRENT) {
            case Config.LANGUAGE_EN:
                binding.get().englishRadioButton.setChecked(true);
                binding.get().arabicRadioButton.setChecked(false);
                binding.get().spanishRadioButton.setChecked(false);
                break;
            case Config.LANGUAGE_AR:
                binding.get().englishRadioButton.setChecked(false);
                binding.get().arabicRadioButton.setChecked(true);
                binding.get().spanishRadioButton.setChecked(false);
                break;
            case Config.LANGUAGE_ES:
                binding.get().englishRadioButton.setChecked(false);
                binding.get().arabicRadioButton.setChecked(false);
                binding.get().spanishRadioButton.setChecked(true);
                break;
            default:
                binding.get().englishRadioButton.setChecked(true);
                binding.get().arabicRadioButton.setChecked(false);
                binding.get().spanishRadioButton.setChecked(false);
                break;
        }

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().englishRadioButton.setOnClickListener(view -> {

            if (LANG_CURRENT.equals(Config.LANGUAGE_ES) || LANG_CURRENT.equals(Config.LANGUAGE_AR)) {

                if (getActivity() != null) {

                    psDialogMsg.showConfirmDialog(getString(R.string.language__language_change), getString(R.string.app__ok), getString(R.string.app__cancel));

                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(v -> {
                        changeLang(Config.LANGUAGE_EN);
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), AppLoadingActivity.class));
                        psDialogMsg.cancel();
                    });

                    psDialogMsg.cancelButton.setOnClickListener(v -> {
                        binding.get().arabicRadioButton.setChecked(true);
                        psDialogMsg.cancel();
                    });

                }
            }

        });

        binding.get().arabicRadioButton.setOnClickListener(view -> {

            if (LANG_CURRENT.equals(Config.LANGUAGE_EN) || LANG_CURRENT.equals(Config.LANGUAGE_ES)) {

                if (getActivity() != null) {

                    psDialogMsg.showConfirmDialog(getString(R.string.language__language_change), getString(R.string.app__ok), getString(R.string.app__cancel));

                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(v -> {
                        changeLang(Config.LANGUAGE_AR);
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), AppLoadingActivity.class));
                        psDialogMsg.cancel();
                    });

                    psDialogMsg.cancelButton.setOnClickListener(v -> {
                        binding.get().englishRadioButton.setChecked(true);
                        psDialogMsg.cancel();
                    });

                }
            }

        });

        binding.get().spanishRadioButton.setOnClickListener(view -> {

            if (LANG_CURRENT.equals(Config.LANGUAGE_EN) || LANG_CURRENT.equals(Config.LANGUAGE_AR)) {

                if (getActivity() != null) {

                    psDialogMsg.showConfirmDialog(getString(R.string.language__language_change), getString(R.string.app__ok), getString(R.string.app__cancel));

                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(v -> {
                        changeLang(Config.LANGUAGE_ES);
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), AppLoadingActivity.class));
                        psDialogMsg.cancel();
                    });

                    psDialogMsg.cancelButton.setOnClickListener(v -> {
                        binding.get().englishRadioButton.setChecked(true);
                        psDialogMsg.cancel();
                    });

                }
            }

        });


    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {
    }

    @Override
    protected void initData() {

    }


    private void changeLang(String lang) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LANGUAGE, lang);
        editor.apply();
    }


}
