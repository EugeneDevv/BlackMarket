package com.panaceasoft.riftblackmarket.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.riftblackmarket.Config;
import com.panaceasoft.riftblackmarket.repository.item.ItemRepository;
import com.panaceasoft.riftblackmarket.utils.AbsentLiveData;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.common.PSViewModel;
import com.panaceasoft.riftblackmarket.viewobject.Image;
import com.panaceasoft.riftblackmarket.viewobject.Item;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemViewModel extends PSViewModel {

    private final LiveData<Resource<List<Item>>> itemListByKeyData;
    private final MutableLiveData<ItemViewModel.ItemTmpDataHolder> itemListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Item>> productDetailListData;
    private MutableLiveData<ItemViewModel.TmpDataHolder> productDetailObj = new MutableLiveData<>();

    private final LiveData<Item> productDetailFromDBByIdData;
    private MutableLiveData<ItemViewModel.TmpDataHolder> productDetailFromDBByIdObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageItemListByKeyData;
    private final MutableLiveData<ItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    private LiveData<Resource<Image>> uploadItemImageData;
    private MutableLiveData<UploadItemImageTmpDataHolder> uploadItemImageObj = new MutableLiveData<>();

    private LiveData<Resource<Item>> uploadItemData;
    private MutableLiveData<UploadItemTmpDataHolder> uploadItemObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> reportItemStatusData;
    private final MutableLiveData<ItemViewModel.ReportItemTmpDataHolder> reportItemStatusObj = new MutableLiveData<>();

    public ItemParameterHolder holder = new ItemParameterHolder();
    public String latValue = "", lngValue = "";

    public String itemDescription = "";
    public String itemId = "";
    public String cityId = "";
    public String historyFlag = "";
    public String customImageUri;
    public String locationId = "";
    public String locationName = "";
    public String otherUserId = "";
    public String otherUserName = "";

    public Item currentItem;

    public String firstImagePath = Constants.EMPTY_STRING, secImagePath = Constants.EMPTY_STRING,
            thirdImagePath = Constants.EMPTY_STRING, fouthImagePath = Constants.EMPTY_STRING, fifthImagePath = Constants.EMPTY_STRING;

    public String userId = "";


    @Inject
    ItemViewModel(ItemRepository repository) {

        itemListByKeyData = Transformations.switchMap(itemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);

        });

        nextPageItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });

        reportItemStatusData = Transformations.switchMap(reportItemStatusObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.reportItem(obj.itemId, obj.reportUserId );

        });


        //  item detail List
        productDetailListData = Transformations.switchMap(productDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product detail List.");
            return repository.getItemDetail(Config.API_KEY, obj.itemId, obj.historyFlag, obj.userId);
        });

        productDetailFromDBByIdData = Transformations.switchMap(productDetailFromDBByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product detail List.");
            return repository.getItemDetailFromDBById(obj.itemId);
        });

        uploadItemImageData = Transformations.switchMap(uploadItemImageObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.uploadItemImage(obj.filePath, obj.imageId, obj.itemId);

        });

        uploadItemData = Transformations.switchMap(uploadItemObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.uploadItem(obj.catId, obj.subCatId, obj.itemTypeId, obj.itemPriceTypeId, obj.itemCurrencyId, obj.conditionId, obj.locationId, obj.remark,
                    obj.description, obj.highlightInfo, obj.price, obj.dealOptionId, obj.brand, obj.businessMode, obj.isSoldOut, obj.title, obj.address, obj.lat, obj.lng, obj.itemId,obj.userId);


        });
    }


    //item image upload

    public void setUploadItemImageObj(String filePath, String itemId, String imageId) {
        UploadItemImageTmpDataHolder tmpDataHolder = new UploadItemImageTmpDataHolder(filePath, itemId, imageId);

        this.uploadItemImageObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Image>> getUploadItemImageData() {
        return uploadItemImageData;
    }

    //region getItemList

    //item upload

    public void setUploadItemObj(String catId, String subCatId, String itemTypeId, String itemPriceTypeId, String itemCurrencyId, String conditionId, String locationId,
                                 String remark, String description, String highlightInfo, String price, String dealOptionId, String brand, String businessMode,
                                 String isSoldOut, String title, String address, String lat, String lng, String itemId, String userId) {
        UploadItemTmpDataHolder tmpDataHolder = new UploadItemTmpDataHolder(catId, subCatId, itemTypeId, itemPriceTypeId, itemCurrencyId, conditionId,locationId,remark,
                description, highlightInfo, price, dealOptionId, brand, businessMode,
                isSoldOut, title, address, lat, lng, itemId, userId);

        this.uploadItemObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Item>> getUploadItemData() {
        return uploadItemData;
    }

    //endregion

    public void setItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {
        if (!isLoading) {
            ItemTmpDataHolder tmpDataHolder = new ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

            this.itemListByKeyObj.setValue(tmpDataHolder);
            setLoadingState(true);

        }
    }

    public LiveData<Resource<List<Item>>> getItemListByKeyData() {
        return itemListByKeyData;
    }

    public void setNextPageItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

        if (!isLoading) {
            ItemTmpDataHolder tmpDataHolder = new ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

            setLoadingState(true);

            this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageItemListByKeyData() {
        return nextPageItemListByKeyData;
    }

    //report item

    public void setReportItemStatusObj(String itemId, String reportUserId) {

//        if (!isLoading) {
            ReportItemTmpDataHolder tmpDataHolder = new ReportItemTmpDataHolder(itemId , reportUserId);

//            setLoadingState(true);

            this.reportItemStatusObj.setValue(tmpDataHolder);
//        }
    }

    public LiveData<Resource<Boolean>> getReportItemStatusData() {
        return reportItemStatusData;
    }

    //endregion

    class ItemTmpDataHolder {

        private String limit, offset, loginUserId;
        private ItemParameterHolder itemParameterHolder;

        public ItemTmpDataHolder(String limit, String offset, String loginUserId, ItemParameterHolder itemParameterHolder) {
            this.limit = limit;
            this.offset = offset;
            this.loginUserId = loginUserId;
            this.itemParameterHolder = itemParameterHolder;
        }
    }

    class ReportItemTmpDataHolder {

        private String itemId, reportUserId;

        public ReportItemTmpDataHolder(String itemId, String reportUserId) {
            this.itemId = itemId;
            this.reportUserId = reportUserId;
        }
    }
    //region Getter And Setter for item detail List

    public void setItemDetailObj(String itemId, String historyFlag, String userId) {
        if (!isLoading) {
            ItemViewModel.TmpDataHolder tmpDataHolder = new ItemViewModel.TmpDataHolder();
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.historyFlag = historyFlag;
            tmpDataHolder.userId = userId;
            productDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Item>> getItemDetailData() {
        return productDetailListData;
    }

    //endregion

    //region Getter And Setter for item detail List

    public void setItemDetailFromDBById(String itemId) {
        if (!isLoading) {
            ItemViewModel.TmpDataHolder tmpDataHolder = new ItemViewModel.TmpDataHolder();
            tmpDataHolder.itemId = itemId;
            productDetailFromDBByIdObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Item> getItemDetailFromDBByIdData() {
        return productDetailFromDBByIdData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String itemId = "";
        public String historyFlag = "";
        public String userId = "";
        public String cityId = "";
        public Boolean isConnected = false;
    }
    //endregion

    class UploadItemImageTmpDataHolder {

        String filePath, itemId, imageId;

        private UploadItemImageTmpDataHolder(String filePath, String itemId, String imageId) {
            this.filePath = filePath;
            this.itemId = itemId;
            this.imageId = imageId;
        }
    }

    class UploadItemTmpDataHolder {

        String catId, subCatId, itemTypeId, itemPriceTypeId, itemCurrencyId, conditionId, locationId, remark, description, highlightInfo, price, dealOptionId, brand, businessMode,
                isSoldOut, title, address, lat, lng,  itemId,userId;

        public UploadItemTmpDataHolder(String catId, String subCatId, String itemTypeId, String itemPriceTypeId, String itemCurrencyId, String conditionId, String locationId, String remark, String description, String highlightInfo, String price, String dealOptionId, String brand, String businessMode, String isSoldOut, String title, String address, String lat, String lng, String itemId, String userId) {
            this.catId = catId;
            this.subCatId = subCatId;
            this.itemTypeId = itemTypeId;
            this.itemPriceTypeId = itemPriceTypeId;
            this.itemCurrencyId = itemCurrencyId;
            this.conditionId = conditionId;
            this.locationId = locationId;
            this.remark = remark;
            this.description = description;
            this.highlightInfo = highlightInfo;
            this.price = price;
            this.dealOptionId = dealOptionId;
            this.brand = brand;
            this.businessMode = businessMode;
            this.isSoldOut = isSoldOut;
            this.title = title;
            this.address = address;
            this.lat = lat;
            this.lng = lng;
            this.itemId = itemId;
            this.userId = userId;
        }
    }
}

