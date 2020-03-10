package com.panaceasoft.riftblackmarket.ui.contactus;

import com.panaceasoft.riftblackmarket.repository.contactus.ContactUsRepository;
import com.panaceasoft.riftblackmarket.ui.common.BackgroundTaskHandler;

/**
 * Created by Panacea-Soft on 7/2/18.
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */

public class ContactUsBackgroundTaskHandler extends BackgroundTaskHandler{

    private final ContactUsRepository repository;

    public ContactUsBackgroundTaskHandler(ContactUsRepository repository) {
        super();
        this.repository = repository;
    }


    public void postContactUs(String apiKey, String contactName, String contactEmail, String contactDesc, String contactPhone) {

        unregister();

        holdLiveData = repository.postContactUs(apiKey, contactName, contactEmail, contactDesc, contactPhone);
        loadingState.setValue(new LoadingState(true, null));

        //noinspection ConstantConditions
        holdLiveData.observeForever(this);
    }
}
