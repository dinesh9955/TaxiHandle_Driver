package com.rydz.driver.viewModel.login.editProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.forgot.ForgotPasswordRequest;
import com.rydz.driver.model.requests.ChangePasswordRequest;
import com.rydz.driver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ChangePasswordViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public ChangePasswordViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitChangePasswordApi(ChangePasswordRequest loginRequest) {


        disposables.add(repository.executeChangePasswoed(loginRequest)
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
    public void hitForgotPhoneApi(ForgotPasswordRequest loginRequest) {


        disposables.add(repository.executeForgotChangePasswoed(loginRequest)
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
