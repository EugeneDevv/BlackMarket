package com.panaceasoft.riftblackmarket.ui.user.userlist.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ItemUserAdapterBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.utils.Objects;
import com.panaceasoft.riftblackmarket.viewobject.User;

public class UserAdapter extends DataBoundListAdapter<User, ItemUserAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private UserAdapter.ItemClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;

    public UserAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, UserAdapter.ItemClickCallback itemClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = itemClickCallback;
    }

    @Override
    protected ItemUserAdapterBinding createBinding(ViewGroup parent) {
        ItemUserAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_user_adapter, parent, false,
                        dataBindingComponent);

        
        binding.getRoot().setOnClickListener(v -> {
            User followerUser = binding.getFollowerUser();
            if (followerUser != null && callback != null) {
                callback.onClick(followerUser);
            }

        });


        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemUserAdapterBinding binding, User followerUser) {
       binding.setFollowerUser(followerUser);
       binding.ratingBarInformation.setRating(followerUser.ratingDetails.totalRatingValue);
       binding.ratingCountTextView.setText(String.format("%s%s%s","(",followerUser.ratingCount,")"));
    }

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.userId, newItem.userId);
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.userId, newItem.userId);
    }

    public interface ItemClickCallback {
        void onClick(User followerUser);
    }
}

