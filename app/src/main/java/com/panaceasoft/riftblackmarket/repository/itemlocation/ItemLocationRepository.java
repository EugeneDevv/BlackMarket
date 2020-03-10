package com.panaceasoft.riftblackmarket.repository.itemlocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.ItemLocationDao;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.ItemLocation;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemLocationRepository extends PSRepository {
    private ItemLocationDao itemLocationDao;

    @Inject
    ItemLocationRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemLocationDao itemLocationDao) {

        super(psApiService, appExecutors, db);
        this.itemLocationDao = itemLocationDao;
    }

    public LiveData<Resource<List<ItemLocation>>> getAllItemLocationList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemLocation>, List<ItemLocation>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemLocation> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");


                try {
                    db.beginTransaction();

                    itemLocationDao.deleteAllItemLocation();

                    itemLocationDao.insertAll(itemTypeList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e);
                } finally {
                    db.endTransaction();
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemLocation> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemLocation>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemLocationDao.getAllItemLocation();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemLocation>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemLocationList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }


}