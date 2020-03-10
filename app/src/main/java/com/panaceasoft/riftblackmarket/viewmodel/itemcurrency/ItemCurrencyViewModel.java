package com.panaceasoft.riftblackmarket.viewmodel.itemcurrency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.riftblackmarket.repository.itemcurrency.ItemCurrencyTypeRepository;
import com.panaceasoft.riftblackmarket.utils.AbsentLiveData;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.common.PSViewModel;
import com.panaceasoft.riftblackmarket.viewobject.ItemCurrency;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.CategoryParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemCurrencyViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemCurrency>>> itemTypeListData;
    private MutableLiveData<ItemCurrencyViewModel.TmpDataHolder> itemTypeListObj = new MutableLiveData<>();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemCurrencyViewModel(ItemCurrencyTypeRepository repository) {

        Utils.psLog("ItemCurrencyViewModel");

        itemTypeListData = Transformations.switchMap(itemTypeListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemCurrencyViewModel : categories");
            return repository.getAllItemCurrencyList(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setItemCurrencyListObj(String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            itemTypeListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemCurrency>>> getItemCurrencyListData() {
        return itemTypeListData;
    }


    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String cityId = "";
    }
}
