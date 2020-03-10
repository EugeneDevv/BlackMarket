package com.panaceasoft.riftblackmarket.repository.rating;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.db.RatingDao;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.Rating;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class RatingRepository extends PSRepository {

    private final RatingDao ratingDao;

    //region Constructor

    @Inject
    protected SharedPreferences pref;

    @Inject
    RatingRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, RatingDao ratingDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside RatingRepository");

        this.ratingDao = ratingDao;
    }

    //Get Rating List by CatId
    public LiveData<Resource<List<Rating>>> getRatingListByProductId(String apiKey, String itemUserId, String limit, String offset) {

        return new NetworkBoundResource<List<Rating>, List<Rating>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Rating> ratingList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    ratingDao.deleteAll();

                    ratingDao.insertAll(ratingList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of product list by catId .", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Rating> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Rating>> loadFromDb() {
                Utils.psLog("Load product From Db by catId");

                return ratingDao.getRatingById(itemUserId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Rating>>> createCall() {
                Utils.psLog("Call API Service to get product list by catId.");

                return psApiService.getAllRatingList(apiKey, itemUserId, limit, offset);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (get product list by catId) : " + message);
            }

        }.asLiveData();
    }


    // get next page Rating List by catId
    public LiveData<Resource<Boolean>> getNextPageRatingListByProductId(String itemUserId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<Rating>>> apiResponse = psApiService.getAllRatingList(Config.API_KEY, itemUserId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (apiResponse.getValue() != null) {

                            ratingDao.insertAll(apiResponse.getValue().body);
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

                });
            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }
    //endregion

    //rating post
    public LiveData<Resource<Boolean>> uploadRatingPostToServer(String title, String description, String rating,String userId, String itemUserId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {

            // Call the API Service
            Response<Rating> response;

            response = psApiService.postRating(Config.API_KEY, userId,itemUserId,rating,title, description).execute();

            // Wrap with APIResponse Class
            ApiResponse<Rating> apiResponse = new ApiResponse<>(response);

            // If response is successful
            if (apiResponse.isSuccessful()) {

                try {
                    db.beginTransaction();

                    if (apiResponse.body != null) {
                        ratingDao.insert(apiResponse.body);
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

            } else {

                statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
            }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }
        });

        return statusLiveData;
    }
    //endregion
}
