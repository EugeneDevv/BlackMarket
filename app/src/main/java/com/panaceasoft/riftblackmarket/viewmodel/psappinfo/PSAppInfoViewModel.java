package com.panaceasoft.riftblackmarket.viewmodel.psappinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.riftblackmarket.repository.appInfo.AppInfoRepository;
import com.panaceasoft.riftblackmarket.utils.AbsentLiveData;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewmodel.common.PSViewModel;
import com.panaceasoft.riftblackmarket.viewobject.PSAppInfo;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;

import javax.inject.Inject;

public class PSAppInfoViewModel extends PSViewModel {

    private final LiveData<Resource<PSAppInfo>> deleteHistoryData;
    private MutableLiveData<TmpDataHolder> deleteHistoryObj = new MutableLiveData<>();

    public String appSettingLat;
    public String appSettingLng;

    @Inject
    PSAppInfoViewModel(AppInfoRepository repository) {
        deleteHistoryData = Transformations.switchMap(deleteHistoryObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("PSAppInfoViewModel");
            return repository.deleteTheSpecificObjects(obj.startDate, obj.endDate);
        });
    }

    public void setDeleteHistoryObj(String startDate, String endDate) {

        TmpDataHolder tmpDataHolder = new TmpDataHolder(startDate, endDate);

        this.deleteHistoryObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<PSAppInfo>> getDeleteHistoryData() {
        return deleteHistoryData;
    }

    class TmpDataHolder {
        String startDate, endDate;

        private TmpDataHolder(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

}
