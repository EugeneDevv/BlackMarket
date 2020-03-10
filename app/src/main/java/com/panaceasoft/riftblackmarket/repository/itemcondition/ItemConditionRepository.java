package com.panaceasoft.riftblackmarket.repository.itemcondition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.ItemConditionDao;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.ItemCondition;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemConditionRepository extends PSRepository {
    private ItemConditionDao itemConditionDao;

    @Inject
    ItemConditionRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemConditionDao itemConditionDao) {

        super(psApiService, appExecutors, db);
        this.itemConditionDao = itemConditionDao;
    }

    public LiveData<Resource<List<ItemCondition>>> getAllItemConditionList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemCondition>, List<ItemCondition>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemCondition> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");


                try {
                    db.beginTransaction();

                    itemConditionDao.deleteAllItemCondition();

                    itemConditionDao.insertAll(itemTypeList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e);
                } finally {
                    db.endTransaction();
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemCondition> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemCondition>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemConditionDao.getAllItemCondition();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemCondition>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemConditionTypeList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }


}