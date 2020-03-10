package com.panaceasoft.riftblackmarket.repository.city;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.City;
import com.panaceasoft.riftblackmarket.viewobject.CityMap;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.CityParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class CityRepository extends PSRepository {

    @Inject
    protected SharedPreferences pref;

    @Inject
    public CityRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside CityRepository");
    }

//    public LiveData<Resource<City>> getCity(String api_key, String cityId) {
//        return new NetworkBoundResource<City, City>(appExecutors) {
//
//            @Override
//            protected void saveCallResult(@NonNull City itemList) {
//                Utils.psLog("SaveCallResult of recent products.");
//
//                db.beginTransaction();
//
//                try {
//
//                    db.cityDao().insert(itemList);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
//                } finally {
//                    db.endTransaction();
//                }
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable City data) {
//
//                // Recent news always load from server
//                return connectivity.isConnected();
//
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<City> loadFromDb() {
//                Utils.psLog("Load discount From Db");
//
//                return db.cityDao().getCityById(cityId);
//
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<City>> createCall() {
//                Utils.psLog("Call API Service to get discount.");
//
//                return psApiService.getCityById(api_key, cityId);
//
//            }
//
//            @Override
//            protected void onFetchFailed(String message) {
//                Utils.psLog("Fetch Failed (getDiscount) : " + message);
//            }
//
//        }.asLiveData();
//    }

    public LiveData<Resource<List<City>>> getCityListByValue(String limit, String offset, CityParameterHolder parameterHolder) {

        return new NetworkBoundResource<List<City>, List<City>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<City> itemList) {
                Utils.psLog("SaveCallResult of getCityList.");

                db.beginTransaction();

                try {

                    String mapKey = parameterHolder.getCityMapKey();

                    db.cityMapDao().deleteByMapKey(mapKey);

                    db.cityDao().insertAll(itemList);

                    String dateTime = Utils.getDateTime();

                    for (int i = 0 ; i < itemList.size(); i++) {
                        db.cityMapDao().insert(new CityMap(mapKey+itemList.get(i).id, mapKey, itemList.get(i).id, i + 1 , dateTime));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getCityList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<City> data) {

                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<City>> loadFromDb() {
                Utils.psLog("getCityList From Db");

                String mapKey = parameterHolder.getCityMapKey();

                return db.cityDao().getCityListByMapKey(mapKey);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<City>>> createCall() {
                Utils.psLog("Call API Service to getCityList.");

                return psApiService.searchCity(Config.API_KEY, limit, offset, parameterHolder.id, parameterHolder.keyword, parameterHolder.isFeatured, parameterHolder.orderBy, parameterHolder.orderType);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getCityList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageCityList(String limit, String offset, CityParameterHolder parameterHolder) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<City>>> apiResponse = psApiService.searchCity(Config.API_KEY, limit, offset, parameterHolder.id, parameterHolder.keyword, parameterHolder.isFeatured, parameterHolder.orderBy, parameterHolder.orderType);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    if(response.body != null) {
                        try {

                            db.beginTransaction();

                            db.cityDao().insertAll(response.body);

                            int finalIndex = db.cityMapDao().getMaxSortingByValue(parameterHolder.getCityMapKey());

                            int startIndex = finalIndex + 1;

                            String mapKey = parameterHolder.getCityMapKey();
                            String dateTime = Utils.getDateTime();

                            for (int i = 0; i < response.body.size(); i++) {
                                db.cityMapDao().insert(new CityMap(mapKey + response.body.get(i).id, mapKey, response.body.get(i).id, startIndex + i, dateTime));
                            }

                            db.setTransactionSuccessful();

                        } catch (NullPointerException ne) {
                            Utils.psErrorLog("Null Pointer Exception : ", ne);
                        } catch (Exception e) {
                            Utils.psErrorLog("Exception : ", e);
                        } finally {
                            db.endTransaction();
                        }

                        statusLiveData.postValue(Resource.success(true));

                    }else {
                        statusLiveData.postValue(Resource.error(response.errorMessage, null));
                    }
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;
    }
}
