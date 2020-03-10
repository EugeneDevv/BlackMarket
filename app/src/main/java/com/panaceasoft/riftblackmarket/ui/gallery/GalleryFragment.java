package com.panaceasoft.riftblackmarket.ui.gallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.binding.FragmentDataBindingComponent;
import com.panaceasoft.riftblackmarket.databinding.FragmentGalleryBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.PSFragment;
import com.panaceasoft.riftblackmarket.ui.gallery.adapter.GalleryAdapter;
import com.panaceasoft.riftblackmarket.utils.AutoClearedValue;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.image.ImageViewModel;
import com.panaceasoft.riftblackmarket.viewobject.Image;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import java.util.List;

/**
 * GalleryFragment
 */
public class GalleryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ImageViewModel imageViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentGalleryBinding> binding;
    private AutoClearedValue<GalleryAdapter> adapter;

    //private String imgParentId = "";

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentGalleryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        binding.get().newsImageList.setHasFixedSize(true);
        binding.get().newsImageList.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        binding.get().newsImageList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initViewModels() {

        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel.class);

    }

    private void imageClicked(Image image) {
        navigationController.navigateToDetailGalleryActivity(getActivity(), imageViewModel.imgType, imageViewModel.id, image.imgId);
    }

    @Override
    protected void initAdapters() {
        // ViewModel need to get from ViewModelProviders
        GalleryAdapter nvAdapter = new GalleryAdapter(dataBindingComponent, this::imageClicked, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().newsImageList.setAdapter(adapter.get());
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initData() {
        //load category

        try {
            if(getActivity() != null) {
                imageViewModel.imgType = getActivity().getIntent().getStringExtra(Constants.IMAGE_TYPE);
            }
        }catch (Exception e){
            Utils.psErrorLog("Error in getting intent.", e);
        }

        try {
            if(getActivity() != null) {
                imageViewModel.id = getActivity().getIntent().getStringExtra(Constants.IMAGE_PARENT_ID);
            }
        }catch (Exception e){
            Utils.psErrorLog("Error in getting intent.", e);
        }

        LiveData<Resource<List<Image>>> imageListLiveData = imageViewModel.getImageListLiveData();
        imageViewModel.setImageParentId(imageViewModel.imgType, imageViewModel.id);
        imageListLiveData.observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                // Update the data
                this.adapter.get().replace(listResource.data);
                this.binding.get().executePendingBindings();

            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");
            }
        });

    }

    //endregion


}
