package com.panaceasoft.riftblackmarket.repository.user;

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
import com.panaceasoft.riftblackmarket.db.UserDao;
import com.panaceasoft.riftblackmarket.repository.common.NetworkBoundResource;
import com.panaceasoft.riftblackmarket.repository.common.PSRepository;
import com.panaceasoft.riftblackmarket.utils.AbsentLiveData;
import com.panaceasoft.riftblackmarket.utils.Constants;
import com.panaceasoft.riftblackmarket.utils.Utils;
import com.panaceasoft.riftblackmarket.viewobject.ApiStatus;
import com.panaceasoft.riftblackmarket.viewobject.User;
import com.panaceasoft.riftblackmarket.viewobject.UserLogin;
import com.panaceasoft.riftblackmarket.viewobject.UserMap;
import com.panaceasoft.riftblackmarket.viewobject.common.Resource;
import com.panaceasoft.riftblackmarket.viewobject.holder.UserParameterHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by Panacea-Soft on 11/17/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Singleton
public class UserRepository extends PSRepository {


    //region Variables

    private final UserDao userDao;
    private String isSelected;

    //endregion


    //region Constructor

    @Inject
    UserRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, UserDao userDao) {
        super(psApiService, appExecutors, db);

        this.userDao = userDao;
    }

    //endregion


    //region User Repository Functions for ViewModel

    /**
     * Function to login
     *
     * @param apiKey   APIKey to access web services
     * @param email    User Email
     * @param password User Password
     * @return Login User Data
     */
    public LiveData<Resource<UserLogin>> doLogin(String apiKey, String email, String password, String deviceToken) {

        Utils.psLog("Do Login : " + email + " & " + password);

        return new NetworkBoundResource<UserLogin, User>(appExecutors) {

            String userId = "";

            @Override
            protected void saveCallResult(@NonNull User user) {
                Utils.psLog("SaveCallResult of doLogin.");

                db.beginTransaction();
                try {

                    // set User id
                    userId = user.userId;

                    // clear user login data
                    userDao.deleteUserLogin();

                    // insert user data
                    userDao.insert(user);

                    // insert user login
                    UserLogin userLogin = new UserLogin(userId, true, user);
                    userDao.insert(userLogin);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of doLogin.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserLogin data) {
                // for user login, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<UserLogin> loadFromDb() {
                Utils.psLog("Load User Login data from database.");
                if (userId == null || userId.equals("")) {
                    return AbsentLiveData.create();
                }

                return userDao.getUserLoginData(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Utils.psLog("Call API Service to do user login.");
                return psApiService.postUserLogin(apiKey, email, password, deviceToken);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed in doLogin.");
            }
        }.asLiveData();
    }

    /**
     * Function to get User Login Data.
     *
     * @return UserLogin Data.
     */
    public LiveData<List<UserLogin>> getLoginUser() {

        Utils.psLog("Get Login User");

        return userDao.getUserLoginData();
    }

    /**
     * Function to get Login User
     *
     * @param apiKey APIKey to access to API Service
     * @param userId User Id to fetch
     * @return Login User
     */
    public LiveData<Resource<User>> getLoginUser(String apiKey, String userId) {

        return new NetworkBoundResource<User, User>(appExecutors) {


            @Override
            protected void saveCallResult(@NonNull User user) {
                Utils.psLog("SaveCallResult of doLogin.");

                db.beginTransaction();
                try {

                    // clear user login data
                    userDao.deleteUserLogin();

                    // insert user data
                    userDao.insert(user);

                    // insert user login
                    UserLogin userLogin = new UserLogin(userId, true, user);
                    userDao.insert(userLogin);

                    db.setTransactionSuccessful();


                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of doLogin.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                // for user login, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Utils.psLog("Load User Login data from database.");

                return userDao.getUserData(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Utils.psLog("Call API Service to do user login.");
                return psApiService.getUserDetail(apiKey, Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING,Constants.EMPTY_STRING, Constants.EMPTY_STRING, userId);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed in doLogin.");
            }
        }.asLiveData();
    }

    /**
     * Function to get User
     *
     * @param apiKey APIKey to access to API Service
     * @param userId User Id to fetch
     * @return Login User
     */

    public LiveData<Resource<User>> getOtherUser(String apiKey, String userId, String otherUserId) {

        return new NetworkBoundResource<User, User>(appExecutors) {


            @Override
            protected void saveCallResult(@NonNull User user) {
                Utils.psLog("SaveCallResult of doLogin.");

                db.beginTransaction();
                try {
                    // clear user login data
                    userDao.deleteUser();

                    // insert user data
                    userDao.insert(user);

                    db.setTransactionSuccessful();


                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of doLogin.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                // for user login, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Utils.psLog("Load User Login data from database.");

                return userDao.getUserData(otherUserId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Utils.psLog("Call API Service to do user login.");
                return psApiService.getUserDetail(apiKey, Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING, Constants.EMPTY_STRING,userId,otherUserId);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed in doLogin.");
            }
        }.asLiveData();
    }

    /**
     * Function to register new user.
     *
     * @param apiKey   APIKey to access web services
     * @param userName User Name
     * @param email    User Email
     * @param password User Password
     * @return Login User Data
     */
    public LiveData<Resource<User>> registerUser(String apiKey, String userName, String email, String password, String deviceToken) {

        final MutableLiveData<Resource<User>> statusLiveData = new MutableLiveData<>(); // To update the status to the listener


        appExecutors.networkIO().execute(() -> {

            try {

                // Call the API Service
                Response<User> response = psApiService.postUser(apiKey, userName, email, password, deviceToken).execute();


                // Wrap with APIResponse Class
                ApiResponse<User> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            // set User id
                            String userId = apiResponse.body.userId;

                            // clear user login data
                            userDao.deleteUserLogin();

                            // insert user data
                            userDao.insert(apiResponse.body);

//                            // insert user login
//                            UserLogin userLogin = new UserLogin(userId, true, apiResponse.body);
//                            userDao.insert(userLogin);

                            db.setTransactionSuccessful();

                            statusLiveData.postValue(Resource.success(response.body()));
                        }

                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
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

    /**
     * Function to register FB user.
     *
     * @param apiKey   APIKey to access web services
     * @param userName User Name
     * @param email    User Email
     * @param imageUrl Image URL
     * @return Login User Data
     */
    public LiveData<Resource<UserLogin>> registerFBUser(String apiKey, String fbId, String userName, String email, String imageUrl, String deviceToken) {

        final MutableLiveData<Resource<UserLogin>> statusLiveData = new MutableLiveData<>(); // To update the status to the listener

        appExecutors.networkIO().execute(() -> {

            try {

                // Call the API Service
                Response<User> response = psApiService
                        .postFBUser(apiKey, fbId, userName, email, imageUrl, deviceToken).execute();


                // Wrap with APIResponse Class
                ApiResponse<User> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            // set User id
                            String userId = apiResponse.body.userId;

                            // clear user login data
                            userDao.deleteUserLogin();

                            // insert user data
                            userDao.insert(apiResponse.body);

                            // insert user login
                            UserLogin userLogin = new UserLogin(userId, true, apiResponse.body);
                            userDao.insert(userLogin);

                            db.setTransactionSuccessful();

                            statusLiveData.postValue(Resource.success(userLogin));
                        }

                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
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

    /**
     * Function to update user.
     *
     * @param apiKey APIKey to access web services
     * @param user   User Data to update.
     * @return Status of Request.
     */
    public LiveData<Resource<ApiStatus>> updateUser(String apiKey, User user) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>(appExecutors) {

            String userId = "";
            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus apiStatus) {
                Utils.psLog("SaveCallResult of update user.");

                db.beginTransaction();
                try {

                    if (apiStatus.status.equals("success")) {

                        // set User id
                        userId = user.userId;

                        // update user data
                        userDao.update(user);

                        // update user login
                        UserLogin userLogin = new UserLogin(userId, true, user);
                        userDao.update(userLogin);

                        db.setTransactionSuccessful();

                    }
                    resultsDb = apiStatus;

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of update user.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                // for user update, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (userId == null || userId.equals("")) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                Utils.psLog("Call API Service to update user.");
                return psApiService.putUser(apiKey, user.userId, user.userName, user.userEmail, user.userPhone, user.userAboutMe);
            }

            @Override
            protected void onFetchFailed(String message) {

                Utils.psLog("Fetch Failed (updateUser)." + message);
            }
        }.asLiveData();
    }

    /**
     * Function to request forgot password
     *
     * @param apiKey APIKey to access web services
     * @param email  User Email
     * @return Status Of request.
     */
    public LiveData<Resource<ApiStatus>> forgotPassword(String apiKey, String email) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>(appExecutors) {

            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus apiStatus) {

                Utils.psLog("SaveCallResult of forgotPassword");

                resultsDb = apiStatus;

            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                // for forgot password, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                Utils.psLog("Call API Service to Request Forgot Password.");
                return psApiService.postForgotPassword(apiKey, email);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of forgot Password.");
            }
        }.asLiveData();
    }

    /**
     * Function to Password Update
     *
     * @param apiKey      APIKey to access web services
     * @param loginUserId Current Login User Id
     * @param password    New Password
     * @return Status of Request.
     */
    public LiveData<Resource<ApiStatus>> passwordUpdate(String apiKey, String loginUserId, String password) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>(appExecutors) {

            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus apiStatus) {

                Utils.psLog("SaveCallResult of passwordUpdate");
                resultsDb = apiStatus;

            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                // for passwordUpdate, always should fetch
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                Utils.psLog("Call API Service to update password.");
                return psApiService.postPasswordUpdate(apiKey, loginUserId, password);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of password update.");
            }
        }.asLiveData();
    }

    /**
     * Upload image ( Used in profile image upload now)
     *
     * @param filePath file path of selected image.
     * @param userId   user id to set image.
     * @param platform current platform ( " android " )
     * @return User
     */
    public LiveData<Resource<User>> uploadImage(String filePath, String userId, String platform) {

        //Init File
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file news_title
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody fullName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), file.getName());

        RequestBody platformRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), platform);

        RequestBody useIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userId);

        return new NetworkBoundResource<User, User>(appExecutors) {

            // Temp ResultType To Return
            private User resultsDb;
            String userId = "";

            @Override
            protected void saveCallResult(@NonNull User user) {
                Utils.psLog("SaveCallResult");
                db.beginTransaction();
                try {

                    // set User id
                    userId = user.userId;

                    // update user data
                    userDao.update(user);

                    // update user login
                    UserLogin userLogin = new UserLogin(userId, true, user);
                    userDao.update(userLogin);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error", e);
                } finally {
                    db.endTransaction();
                }

                resultsDb = user;
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                // Image upload should always connect to server.
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<User>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(resultsDb);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Utils.psLog("Call API Service to upload image.");

                return psApiService.doUploadImage(Config.API_KEY, useIdRB, fullName, body, platformRB);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of uploading image.");
            }
        }.asLiveData();
    }


    //endregion

    public LiveData<Resource<User>> getUserById(String id) {

        final MediatorLiveData<Resource<User>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<User>> apiResponse = psApiService.getUserById(Config.API_KEY, id);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    statusLiveData.postValue(Resource.successWithMsg("success", response.body));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;

    }


    public LiveData<Resource<UserLogin>> verificationCodeForUser(String userId, String code) {

        final MutableLiveData<Resource<UserLogin>> statusLiveData = new MutableLiveData<>();


        appExecutors.networkIO().execute(() -> {

            try {

                // Call the API Service
                Response<User> response = psApiService.verifyEmail(Config.API_KEY, userId, code).execute();


                // Wrap with APIResponse Class
                ApiResponse<User> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            // set User id
                            String user_id = apiResponse.body.userId;

                            // clear user login data
                            userDao.deleteUserLogin();

                            // insert user data
                            userDao.insert(apiResponse.body);

                            // insert user login
                            UserLogin userLogin = new UserLogin(user_id, true, apiResponse.body);
                            userDao.insert(userLogin);

                            db.setTransactionSuccessful();

                            statusLiveData.postValue(Resource.success(userLogin));
                        }

                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
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


    public LiveData<Resource<Boolean>> resentCodeForUser(String userEmail) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<ApiStatus> response;

            try {
                response = psApiService.resentCodeAgain(Config.API_KEY, userEmail).execute();


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

    //region Get User List

    public LiveData<Resource<List<User>>> getUserListByKey(String loginUserId, String limit, String offset, UserParameterHolder userParameterHolder) {

        return new NetworkBoundResource<List<User>, List<User>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<User> userList) {
                Utils.psLog("SaveCallResult of getProductListByKey.");

                try {

                    db.beginTransaction();

                    String mapKey = userParameterHolder.getUserMapKey();

                    db.userMapDao().deleteUsersByMapKey(mapKey);

                    userDao.insertAll(userList);

                    String dateTime = Utils.getDateTime();

                    for (int i = 0; i < userList.size(); i++) {
                        db.userMapDao().insert(new UserMap(mapKey + userList.get(i).userId, mapKey, userList.get(i).userId, i + 1, dateTime));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductListByKey.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<User>> loadFromDb() {
                Utils.psLog("Load getProductListByKey From Db");
                String mapKey = userParameterHolder.getUserMapKey();

                return userDao.getUserByKey(mapKey);

            }


            @NonNull
            @Override
            protected LiveData<ApiResponse<List<User>>> createCall() {
                Utils.psLog("Call API Service to getProductListByKey.");

                return psApiService.searchUser(Config.API_KEY,limit,offset,userParameterHolder.user_name,userParameterHolder.overall_rating,
                        userParameterHolder.return_types,loginUserId, userParameterHolder.otherUserId);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getUserListByKey) : " + message);
            }

        }.asLiveData();

    }

    public LiveData<Resource<Boolean>> getNextPageUserListByKey(UserParameterHolder userParameterHolder, String loginUserId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<User>>> apiResponse = psApiService.searchUser(Config.API_KEY,limit,offset,userParameterHolder.user_name,userParameterHolder.overall_rating,
                userParameterHolder.return_types,loginUserId, userParameterHolder.otherUserId);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                if (response.body != null) {
                    appExecutors.diskIO().execute(() -> {

                        try {

                            db.beginTransaction();

                            userDao.insertAll(response.body);

                            int finalIndex = db.userMapDao().getMaxSortingByValue(userParameterHolder.getUserMapKey());

                            int startIndex = finalIndex + 1;

                            String mapKey = userParameterHolder.getUserMapKey();
                            String dateTime = Utils.getDateTime();

                            for (int i = 0; i < response.body.size(); i++) {
                                db.userMapDao().insert(new UserMap(mapKey + response.body.get(i).userId, mapKey, response.body.get(i).userId, startIndex + i, dateTime));
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

    //rating post
    public LiveData<Resource<Boolean>> uploadUserFollowPostToServer(String userId, String followedUserId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                try {
                    db.beginTransaction();

                    isSelected = userDao.selectUserFollowById(userId);
                    if (isSelected.equals(Constants.ONE)) {
                        userDao.updateUserFollowById(userId, Constants.ZERO);
                    } else {
                        userDao.updateUserFollowById(userId, Constants.ONE);
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
                Response<User> response;

                response = psApiService.setPostUserFollow(Config.API_KEY,  userId, followedUserId).execute();

                // Wrap with APIResponse Class
                ApiResponse<User> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            userDao.insert(apiResponse.body);

                            if(apiResponse.body.isFollowed.equals(Constants.ZERO)){
                                //delete data of unfollow user
                                db.itemDao().deleteAllItemFromFollowerByUserId(followedUserId);
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

                        isSelected = userDao.selectUserFollowById(userId);
                        if (isSelected.equals(Constants.ONE)) {
                            userDao.updateUserFollowById(userId, Constants.ZERO);
                        } else {
                            userDao.updateUserFollowById(userId, Constants.ONE);
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

}
