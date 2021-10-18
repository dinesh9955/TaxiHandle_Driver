package com.rydz.driver.viewModel.login.editProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.requests.UserRatingRequest;
import com.rydz.driver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class UserRatingViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public UserRatingViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }
    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitUserRatingApi(String loginRequest) {


        disposables.add(repository.executeUserRating(loginRequest)
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
    public void hittoUserRating(UserRatingRequest loginRequest) {


        disposables.add(repository.executesetRatingToUser(loginRequest)
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
