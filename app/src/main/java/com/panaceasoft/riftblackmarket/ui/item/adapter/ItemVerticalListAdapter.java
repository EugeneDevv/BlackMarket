package com.panaceasoft.riftblackmarket.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ItemItemVerticalWithUserBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundViewHolder;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Objects;
import com.panaceasoft.riftblackmarket.viewobject.Item;

/**
 * Created by Panacea-Soft on 9/18/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ItemVerticalListAdapter extends DataBoundListAdapter<Item, ItemItemVerticalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemVerticalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                   NewsClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemItemVerticalWithUserBinding createBinding(ViewGroup parent) {
        ItemItemVerticalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_item_vertical_with_user, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Item item = binding.getItem();
            if (item != null && callback != null) {
                callback.onClick(item);
            }
        });

        
        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemItemVerticalWithUserBinding> holder, int position) {
        super.bindView(holder, position);


        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemItemVerticalWithUserBinding binding, Item item) {
        binding.setItem(item);
        binding.priceTextView.setText(String.format("%s%s",item.itemCurrency.currencySymbol,item.price));

        if(item.isSoldOut.equals(Constants.ONE)){
            binding.isSoldTextView.setVisibility(View.VISIBLE);
        }else {
            binding.isSoldTextView.setVisibility(View.GONE);
        }
//        binding.ratingValueTextView.setText(String.valueOf(item.ratingDetails.totalRatingValue));
//        binding.reviewValueTextView.setText(String.format("( %s %s )", String.valueOf(item.ratingDetails.totalRatingCount), binding.getRoot().getResources().getString(R.string.dashboard_review)));
    }

    @Override
    protected boolean areItemsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount);
    }

    @Override
    protected boolean areContentsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount);
    }

    public interface NewsClickCallback {
        void onClick(Item item);

    }


}
