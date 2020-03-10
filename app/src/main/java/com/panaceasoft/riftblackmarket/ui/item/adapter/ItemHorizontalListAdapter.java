package com.panaceasoft.riftblackmarket.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ItemItemHorizontalWithUserBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundViewHolder;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Objects;
import com.panaceasoft.riftblackmarket.viewobject.Item;

public class ItemHorizontalListAdapter extends DataBoundListAdapter<Item, ItemItemHorizontalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemHorizontalListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemHorizontalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                     ItemHorizontalListAdapter.NewsClickCallback callback,
                                     DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemItemHorizontalWithUserBinding createBinding(ViewGroup parent) {
        ItemItemHorizontalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_item_horizontal_with_user, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Item item = binding.getItem();
            if (item != null && callback != null) {
                callback.onClick(item);
            }
        });
        return binding;

        
    }

    
    @Override
    public void bindView(DataBoundViewHolder<ItemItemHorizontalWithUserBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemItemHorizontalWithUserBinding binding, Item item) {

        binding.setItem(item);

        binding.conditionTextView.setText(binding.getRoot().getResources().getString(R.string.item_condition__type,item.itemCondition.name));
        binding.priceTextView.setText(String.format("%s%s",item.itemCurrency.currencySymbol,item.price));

        if(item.isSoldOut.equals(Constants.ONE)){
            binding.isSoldTextView.setVisibility(View.VISIBLE);
        }else {
            binding.isSoldTextView.setVisibility(View.GONE);
        }

    }

    @Override
    protected boolean areItemsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title);
    }

    @Override
    protected boolean areContentsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title);
    }

    public interface NewsClickCallback {
        void onClick(Item item);
    }


}


