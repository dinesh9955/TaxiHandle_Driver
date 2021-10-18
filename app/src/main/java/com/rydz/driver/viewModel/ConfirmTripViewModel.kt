package com.rydz.driver.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.viewModel.login.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ConfirmTripViewModel(var repository: Repository) : ViewModel() {
    val disposables = CompositeDisposable()
    val responseLiveData = MutableLiveData<ApiResponse>()

    fun loginResponse(): MutableLiveData<ApiResponse> {
        return responseLiveData
    }

    /*
     * method to get vehicle rates info
     * */
    fun hitGetVehicleRate(id: String) {


        disposables.add(repository.executeGetVehicleInfo(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { d -> responseLiveData.setValue(ApiResponse.loading()) }
            .subscribe(
                { result -> responseLiveData.setValue(ApiResponse.success(result)) },
                { throwable -> responseLiveData.setValue(ApiResponse.error(throwable)) }
            ))

    }



    override fun onCleared() {
        disposables.clear()
    }
}
