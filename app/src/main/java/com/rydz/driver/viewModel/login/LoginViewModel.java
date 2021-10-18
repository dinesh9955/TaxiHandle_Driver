package com.rydz.driver.viewModel.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.requests.LoginRequest;
import com.rydz.driver.model.requests.LoginWithMobileRequest;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class LoginViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public LoginViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitLoginApi(LoginRequest loginRequest) {


        disposables.add(repository.executeLogin(loginRequest)
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
    public void hitMobileLoginApi(LoginWithMobileRequest loginRequest) {

        disposables.add(repository.executeMobileLogin(loginRequest)
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
