package com.rydz.driver.viewModel.login.editProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.requests.ChangeDriverStatusRequest;
import com.rydz.driver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ChangeDriverStatusViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public ChangeDriverStatusViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitDriverStatusApi(ChangeDriverStatusRequest loginRequest) {


        disposables.add(repository.executeChangeDriverStatus(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitScreenCapture(RequestBody dataBody, MultipartBody.Part driverLicence) {

        disposables.add(repository.executeScreenCapture(dataBody,driverLicence)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(

                        result ->
                        {
                            Log.e("66",result.toString());
                            responseLiveData.setValue(ApiResponse.success(result));
                        },
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
