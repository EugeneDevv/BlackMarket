package com.panaceasoft.riftblackmarket.repository.itemcurrency;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.ItemCurrencyDao;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.ItemCurrency;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemCurrencyTypeRepository extends PSRepository {
    private ItemCurrencyDao itemCurrencyDao;

    @Inject
    ItemCurrencyTypeRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemCurrencyDao itemCurrencyDao) {

        super(psApiService, appExecutors, db);
        this.itemCurrencyDao = itemCurrencyDao;
    }

    public LiveData<Resource<List<ItemCurrency>>> getAllItemCurrencyList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemCurrency>, List<ItemCurrency>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemCurrency> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");


                try {
                    db.beginTransaction();

                    itemCurrencyDao.deleteAllItemCurrency();

                    itemCurrencyDao.insertAll(itemTypeList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e);
                } finally {
                    db.endTransaction();
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemCurrency> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemCurrency>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemCurrencyDao.getAllItemCurrency();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemCurrency>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemCurrencyTypeList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }


}