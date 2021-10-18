package com.rydz.driver.viewModel.login.editProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.requests.PhoneStatusRequest;
import com.rydz.driver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ChangePhoneStatusViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public ChangePhoneStatusViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitCheckPhoneStatusApi(PhoneStatusRequest loginRequest) {


        disposables.add(repository.executeCheckPhoneStatus(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
