package com.rydz.driver.viewModel.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.rydz.driver.apiConstants.ApiResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class GetDocumentViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public GetDocumentViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }
    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitGetDocumentApi(String driver_id) {


        disposables.add(repository.executeGetDocument(driver_id)
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
