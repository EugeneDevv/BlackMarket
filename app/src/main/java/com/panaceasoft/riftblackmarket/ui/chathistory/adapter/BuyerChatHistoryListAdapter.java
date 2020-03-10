package com.panaceasoft.riftblackmarket.ui.chathistory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.riftblackmarket.R;
import com.panaceasoft.riftblackmarket.databinding.ItemBuyerChatHistoryListAdapterBinding;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundListAdapter;
import com.panaceasoft.riftblackmarket.ui.common.DataBoundViewHolder;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Objects;
import com.panaceasoft.riftblackmarket.viewobject.ChatHistory;

public class BuyerChatHistoryListAdapter extends DataBoundListAdapter<ChatHistory, ItemBuyerChatHistoryListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ChatHistoryClickCallback callback;
    private DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public BuyerChatHistoryListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                       ChatHistoryClickCallback callback,
                                       DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemBuyerChatHistoryListAdapterBinding createBinding(ViewGroup parent) {
        ItemBuyerChatHistoryListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_buyer_chat_history_list_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatHistory chatHistory = binding.getChatHistory();

                if (chatHistory != null && callback != null) {

                    callback.onClick(chatHistory, chatHistory.id);
                }
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemBuyerChatHistoryListAdapterBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemBuyerChatHistoryListAdapterBinding binding, ChatHistory chatHistory) {
        binding.setChatHistory(chatHistory);

        binding.itemConditionTextView.setText(binding.getRoot().getResources().getString(R.string.item_condition__type, chatHistory.item.itemCondition.name));

        if(chatHistory.sellerUnreadCount.equals(Constants.ZERO)) {
            binding.countTextView.setVisibility(View.GONE);
        }else {
            binding.countTextView.setVisibility(View.VISIBLE);
        }

        if(chatHistory.item.isSoldOut.equals(Constants.ONE)) {
            binding.soldTextView.setVisibility(View.VISIBLE);
        }else {
            binding.soldTextView.setVisibility(View.GONE);
        }

    }

    @Override
    protected boolean areItemsTheSame(ChatHistory oldItem, ChatHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                oldItem.addedDate.equals(newItem.addedDate) &&
                oldItem.sellerUnreadCount.equals(newItem.sellerUnreadCount);
    }

    @Override
    protected boolean areContentsTheSame(ChatHistory oldItem, ChatHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                oldItem.addedDate.equals(newItem.addedDate) &&
                oldItem.sellerUnreadCount.equals(newItem.sellerUnreadCount);
    }

    public interface ChatHistoryClickCallback {
        void onClick(ChatHistory chatHistory, String id);
    }


}
