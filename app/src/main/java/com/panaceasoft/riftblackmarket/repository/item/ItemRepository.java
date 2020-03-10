package com.panaceasoft.riftblackmarket.repository.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.panaceasoft.riftblackmarket.AppExecutors;
import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.api.ApiResponse;
import com.panaceasoft.riftblackmarket.api.PSApiService;
import com.panaceasoft.riftblackmarket.db.ItemDao;
import com.panaceasoft.riftblackmarket.db.PSCoreDb;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.ApiStatus;
import com.panaceasoft.riftblackmarket.viewobject.Image;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.ItemFavourite;
import com.panaceasoft.riftblackmarket.viewobject.ItemFromFollower;
import com.panaceasoft.riftblackmarket.viewobject.ItemHistory;
import com.panaceasoft.riftblackmarket.viewobject.ItemMap;
import com.panaceasoft.riftblackmarket.viewobject.ItemSpecs;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.ItemParameterHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

@Singleton
public class ItemRepository extends PSRepository {
    /**
     * Constructor of PSRepository
     *
     * @param psApiService Panacea-Soft API Service Instance
     * @param appExecutors Executors Instance
     * @param db           Panacea-Soft DB
     */
    private String isSelected;
    private final ItemDao itemDao;

    @Inject
    protected ItemRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db,ItemDao itemDao) {
        super(psApiService, appExecutors, db);
        this.itemDao = itemDao;
    }

    //region Get Favourite Product

    public LiveData<Resource<List<Item>>> getFavouriteList(String apiKey, String loginUserId, String offset) {

        return new NetworkBoundResource<List<Item>, List<Item>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Item> itemList) {
                Utils.psLog("SaveCallResult of related products.");

                db.beginTransaction();

                try {

                    db.itemDao().deleteAllFavouriteItems();

                    db.itemDao().insertAll(itemList);

                    for (int i = 0; i < itemList.size(); i++) {
                        db.itemDao().insertFavourite(new ItemFavourite(itemList.get(i).id, i + 1));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Item> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Item>> loadFromDb() {
                Utils.psLog("Load related From Db");

                return db.itemDao().getAllFavouriteProducts();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Item>>> createCall() {
                Utils.psLog("Call API Service to get related.");

                return psApiService.getFavouriteList(apiKey, Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), offset);


            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getRelated) : " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageFavouriteProductList(String loginUserId, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Item>>> apiResponse = psApiService.getFavouriteList(Config.API_KEY, Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.beginTransaction();

                        if (response.body != null) {

                            int lastIndex = db.itemDao().getMaxSortingFavourite();
                            lastIndex = lastIndex + 1;

                            for (int i = 0; i < response.body.size(); i++) {
                                db.itemDao().insertFavourite(new ItemFavourite(response.body.get(i).id, lastIndex + i));
                            }

                            db.itemDao().insertAll(response.body);
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
    //region Get Item List

    public LiveData<Resource<List<Item>>> getItemListByKey(String loginUserId, String limit, String offset, ItemParameterHolder itemParameterHolder) {

        return new NetworkBoundResource<List<Item>, List<Item>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Item> itemList) {
                Utils.psLog("SaveCallResult of getProductListByKey.");

                try {

                    db.beginTransaction();

                    if(!itemParameterHolder.lat.isEmpty() && !itemParameterHolder.lng.isEmpty() && !itemParameterHolder.mapMiles.isEmpty()){
                        itemParameterHolder.location_id = Constants.EMPTY_STRING;
                    }

                    String mapKey = itemParameterHolder.getItemMapKey();

                    db.itemMapDao().deleteByMapKey(mapKey);

                    itemDao.insertAll(itemList);

                    String dateTime = Utils.getDateTime();

                    for (int i = 0; i < itemList.size(); i++) {
                        db.itemMapDao().insert(new ItemMap(mapKey + itemList.get(i).id, mapKey, itemList.get(i).id, i + 1, dateTime));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductListByKey.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Item> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Item>> loadFromDb() {
                Utils.psLog("Load getProductListByKey From Db");

                if(!itemParameterHolder.lat.isEmpty() && !itemParameterHolder.lng.isEmpty() && !itemParameterHolder.mapMiles.isEmpty()){
                    itemParameterHolder.location_id = Constants.EMPTY_STRING;
                }

                String mapKey = itemParameterHolder.getItemMapKey();
                if(!limit.equals(Config.LIMIT_FROM_DB_COUNT)){
                    return itemDao.getItemByKey(mapKey);
                }
                else {
                    return itemDao.getItemByKeyByLimit(mapKey,limit);
                }

            }


            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Item>>> createCall() {
                Utils.psLog("Call API Service to getProductListByKey.");

                return psApiService.searchItem(Config.API_KEY, limit, offset, loginUserId, itemParameterHolder.keyword, itemParameterHolder.cat_id, itemParameterHolder.sub_cat_id,
                        itemParameterHolder.order_by, itemParameterHolder.order_type, itemParameterHolder.type_id,itemParameterHolder.price_type_id,itemParameterHolder.currency_id,
                        itemParameterHolder.location_id,itemParameterHolder.deal_option_id,itemParameterHolder.condition_id,itemParameterHolder.max_price,
                        itemParameterHolder.min_price,"",itemParameterHolder.lat,itemParameterHolder.lng,itemParameterHolder.mapMiles,itemParameterHolder.userId);


            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getProductListByKey) : " + message);
            }

        }.asLiveData();

    }

    public LiveData<Resource<Boolean>> getNextPageProductListByKey(ItemParameterHolder itemParameterHolder, String loginUserId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

//        prepareRatingValueForServer(productParameterHolder);

        LiveData<ApiResponse<List<Item>>> apiResponse = psApiService.searchItem(Config.API_KEY, limit, offset, loginUserId, itemParameterHolder.keyword, itemParameterHolder.cat_id, itemParameterHolder.sub_cat_id,
                itemParameterHolder.order_by, itemParameterHolder.order_type, itemParameterHolder.type_id,itemParameterHolder.price_type_id,itemParameterHolder.currency_id,
                itemParameterHolder.location_id,itemParameterHolder.deal_option_id,itemParameterHolder.condition_id,itemParameterHolder.max_price,
                itemParameterHolder.min_price,"",itemParameterHolder.lat,itemParameterHolder.lng,itemParameterHolder.mapMiles,itemParameterHolder.userId);



        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                if (response.body != null) {
                    appExecutors.diskIO().execute(() -> {

                        try {

                            db.beginTransaction();

                            itemDao.insertAll(response.body);

                            int finalIndex = db.itemMapDao().getMaxSortingByValue(itemParameterHolder.getItemMapKey());

                            int startIndex = finalIndex + 1;

                            String mapKey = itemParameterHolder.getItemMapKey();
                            String dateTime = Utils.getDateTime();

                            for (int i = 0; i < response.body.size(); i++) {
                                db.itemMapDao().insert(new ItemMap(mapKey + response.body.get(i).id, mapKey, response.body.get(i).id, startIndex + i, dateTime));
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

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;

    }

    //endregion


    //region Favourite post

    public LiveData<Resource<Boolean>> uploadFavouritePostToServer(String itemId, String userId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                try {
                    db.beginTransaction();

                    isSelected = itemDao.selectFavouriteById(itemId);
                    if (isSelected.equals(Constants.ONE)) {
                        itemDao.updateProductForFavById(itemId, Constants.ZERO);
                    } else {
                        itemDao.updateProductForFavById(itemId, Constants.ONE);
                    }


                    db.setTransactionSuccessful();
                } catch (NullPointerException ne) {
                    Utils.psErrorLog("Null Pointer Exception : ", ne);
                } catch (Exception e) {
                    Utils.psErrorLog("Exception : ", e);
                } finally {
                    db.endTransaction();
                }

                // Call the API Service
                Response<Item> response;

                response = psApiService.setPostFavourite(Config.API_KEY, itemId, userId).execute();

                // Wrap with APIResponse Class
                ApiResponse<Item> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            itemDao.insert(apiResponse.body);

                            if (isSelected.equals(Constants.ONE)) {
                                db.itemDao().deleteFavouriteItemByItemId(apiResponse.body.id);
                            }else {
                                int lastIndex = db.itemDao().getMaxSortingFavourite();
                                lastIndex = lastIndex + 1;

                                db.itemDao().insertFavourite(new ItemFavourite(apiResponse.body.id, lastIndex ));
                            }
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(apiResponse.getNextPage() != null));

                } else {

                    try {
                        db.beginTransaction();

                        isSelected = itemDao.selectFavouriteById(itemId);
                        if (isSelected.equals(Constants.ONE)) {
                            itemDao.updateProductForFavById(itemId, Constants.ZERO);
                        } else {
                            itemDao.updateProductForFavById(itemId, Constants.ONE);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }
        });

        return statusLiveData;
    }

    //endregion

    //item image upload

    public LiveData<Resource<Image>> uploadItemImage(String filePath,String imageId, String itemId) {

        //Init File
        MultipartBody.Part body = null;
        if (!filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        }
        // add another part within the multipart request
        RequestBody idRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), itemId);

        RequestBody imgIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), imageId);

        MultipartBody.Part finalBody = body;
        return new NetworkBoundResource<Image, Image>(appExecutors) {

            // Temp ResultType To Return

            @Override
            protected void saveCallResult(@NonNull Image image) {
                Utils.psLog("SaveCallResult");
                db.beginTransaction();
                try {

//                    Item item = itemDao.getItemObjectById(itemId);
//                    // update user data
//                    item.defaultPhoto = image;
//
//                    itemDao.insert(item);

                    db.imageDao().deleteTable();
                    db.imageDao().insert(image);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Image data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<Image> loadFromDb() {

                return db.imageDao().getUploadImage();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Image>> createCall() {
                Utils.psLog("Call API Service to upload image.");

                return psApiService.itemImageUpload(Config.API_KEY,idRB,imgIdRB ,finalBody);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of uploading image.");
            }
        }.asLiveData();
    }

    //endregion

    //item upload

    public LiveData<Resource<Item>> uploadItem(String catId, String subCatId, String itemTypeId, String itemPriceTypeId, String itemCurrencyId, String conditionId,String locationId,String remark,
                                                         String description, String highlightInfo, String price, String dealOptionId, String brand, String businessMode,
                                               String isSoldOut, String title, String address, String lat, String lng, String itemId,String userId) {

        final MutableLiveData<Resource<Item>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                // Call the API Service
                Response<List<Item>> response;

                response = psApiService.itemUpload(Config.API_KEY, catId, subCatId, itemTypeId, itemPriceTypeId, itemCurrencyId, conditionId,locationId,remark,
                        description, highlightInfo, price, dealOptionId, brand, businessMode,
                        isSoldOut, title, address, lat, lng, itemId,userId).execute();

                // Wrap with APIResponse Class
                ApiResponse<List<Item>> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    if (apiResponse.body != null) {
                        try {
                            db.beginTransaction();

                            itemDao.insert(apiResponse.body.get(0));

                            db.setTransactionSuccessful();
                        } catch (NullPointerException ne) {
                            Utils.psErrorLog("Null Pointer Exception : ", ne);
                        } catch (Exception e) {
                            Utils.psErrorLog("Exception : ", e);
                        } finally {
                            db.endTransaction();
                        }

                        statusLiveData.postValue(Resource.success(apiResponse.body.get(0)));
                    }

                } else {

                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }
        });

        return statusLiveData;
    }

    //endregion

    public LiveData<Resource<List<Item>>> getItemFromFollowerList(String loginUserId, String limit, String offset) {

        return new NetworkBoundResource<List<Item>, List<Item>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Item> itemList) {
                Utils.psLog("SaveCallResult of related products.");

                db.beginTransaction();

                try {

                    db.itemDao().deleteAllItemFromFollower();

                    db.itemDao().insertAll(itemList);

                    for (int i = 0; i < itemList.size(); i++) {
                        db.itemDao().insertItemFromFollower(new ItemFromFollower(itemList.get(i).id, itemList.get(i).user.userId,i + 1));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Item> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Item>> loadFromDb() {
                Utils.psLog("Load related From Db");

                return db.itemDao().getAllItemFromFollower();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Item>>> createCall() {
                Utils.psLog("Call API Service to get related.");

                return psApiService.getItemListFromFollower(Config.API_KEY, Utils.checkUserId(loginUserId), limit, offset);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getRelated) : " + message);
            }

        }.asLiveData();

    }

    public LiveData<Resource<Boolean>> getNextPageItemFromFollowerList(String loginUserId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Item>>> apiResponse = psApiService.getItemListFromFollower(Config.API_KEY,loginUserId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                if (response.body != null) {
                    appExecutors.diskIO().execute(() -> {

                        try {

                            db.beginTransaction();

                            itemDao.insertAll(response.body);

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

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;
    }


    //region Get Product detail

    public LiveData<Resource<Item>> getItemDetail(String apiKey, String itemId, String historyFlag, String userId) {

        return new NetworkBoundResource<Item, Item>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Item itemList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    itemDao.insert(itemList);

                    db.specsDao().deleteItemSpecsById(itemId);
//                    db.specsDao().insertAll(itemList.productSpecsList);

//                    db.productAttributeHeaderDao().deleteProductAttributeHeaderById(itemId);
//                    db.productAttributeHeaderDao().insertAll(itemList.attributesHeaderList);
//
//                    for (int i = 0; i < itemList.attributesHeaderList.size(); i++) {
//                        db.productAttributeDetailDao().deleteProductAttributeDetailById(itemId, itemList.attributesHeaderList.get(i).id);
//                        db.productAttributeDetailDao().insertAll(itemList.attributesHeaderList.get(i).attributesDetailList);
//                    }

                    if (historyFlag.equals(Constants.ONE)) {

                        db.historyDao().insert(new ItemHistory(itemId, itemList.title, itemList.defaultPhoto.imgPath, Utils.getDateTime()));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Item data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<Item> loadFromDb() {
                Utils.psLog("Load discount From Db");

                return itemDao.getItemById(itemId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Item>> createCall() {
                Utils.psLog("Call API Service to get discount.");

                return psApiService.getItemDetail(apiKey, itemId, Utils.checkUserId(userId));

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getDiscount) : " + message);
            }

        }.asLiveData();
    }

    //endregion


    //region Touch count post

    public LiveData<Resource<Boolean>> uploadTouchCountPostToServer(String userId, String itemId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<ApiStatus> response;

            try {
                response = psApiService.setrawPostTouchCount(
                        Config.API_KEY, itemId, Utils.checkUserId(userId)).execute();

                ApiResponse<ApiStatus> apiResponse = new ApiResponse<>(response);

                if (apiResponse.isSuccessful()) {
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

    //report item

    public LiveData<Resource<Boolean>> reportItem(String itemId, String reportUserId)
    {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<ApiStatus> response;

            try {
                response = psApiService.reportItem(Config.API_KEY, itemId, reportUserId).execute();

                if (response.isSuccessful()) {
                    statusLiveData.postValue(Resource.success(true));
                } else {
                    statusLiveData.postValue(Resource.error("error", false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return statusLiveData;
    }

    //region Get history

    public LiveData<List<ItemHistory>> getAllHistoryList(String offset) {

        return db.historyDao().getAllHistoryItemListData(offset);

    }

    //endregion

    //region Get Product specs

    public LiveData<List<ItemSpecs>> getAllSpecificaions(String itemId) {
        return db.specsDao().getItemSpecsById(itemId);
    }

    //endregion

    //region Get item detail

    public LiveData<Item> getItemDetailFromDBById(String itemId) {
        return itemDao.getItemById(itemId);
    }

    //endregion

}
//endregion
