package com.rydz.driver.viewModel.login.editProfile;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cabuser.com.rydz.ui.home.SendOtpRequest;
import cabuser.com.rydz.ui.home.VerifyOtpRequest;
import com.rydz.driver.apiConstants.ApiResponse;
import com.rydz.driver.model.forgot.EmailOtpRequest;
import com.rydz.driver.model.requests.EditProfilerequest;
import com.rydz.driver.viewModel.login.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ForgotPasswordViewModel extends ViewModel {

    public Repository repository;
    public final CompositeDisposable disposables = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> resendOtpLiveData = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> responseSmsOtpLiveData = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> verifySmsOtpLiveData = new MutableLiveData<>();
    public ForgotPasswordViewModel(Repository repository) {
        this.repository = repository;
    }


    public MutableLiveData<ApiResponse> resendOtpResponse() {
        return resendOtpLiveData;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }
    public MutableLiveData<ApiResponse> smsOtpResponse() {
        return responseSmsOtpLiveData;
    }
    public MutableLiveData<ApiResponse> verifySmsOtpResponse() {
        return verifySmsOtpLiveData;
    }
    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitEditProfileApi(EditProfilerequest loginRequest) {


        disposables.add(repository.executeEditProfile(loginRequest)
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
    public void hitVerifyOtpApi(EmailOtpRequest loginRequest) {


        disposables.add(repository.executeVerifyOtp(loginRequest)
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
    public void hitForgotEmailApi(EmailOtpRequest loginRequest) {


        disposables.add(repository.executeEmailOtp(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> resendOtpLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> resendOtpLiveData.setValue(ApiResponse.success(result)),
                        throwable -> resendOtpLiveData.setValue(ApiResponse.error(throwable))
                ));

    }



    public void getSmsOtp( SendOtpRequest sendOtpRequest) {

        Log.e("139",sendOtpRequest.toString());

        disposables.add(repository.getSMSOtp(sendOtpRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseSmsOtpLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseSmsOtpLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseSmsOtpLiveData.setValue(ApiResponse.error(throwable))
                ));



      /*  CapyApplication.getRetroApiClient().sendOtp(sendOtpRequest).enqueue(object : Callback<SendSmsOtpResponse> {
            override fun onFailure(call: Call<SendSmsOtpResponse>, t: Throwable) {
                mProgress?.value = false
                Utils.showMessage(activity.getString(R.string.str_wrong), activity)

                Log.e("139","139")
            }

            override fun onResponse(call: Call<SendSmsOtpResponse>, response: Response<SendSmsOtpResponse>) {

                mProgress?.value = false
                Log.e("139",response.toString())



            }
        })*/

    }


    public void verifySmsOtp( VerifyOtpRequest verifyOtpRequest) {

        disposables.add(repository.verifySMSOtp(verifyOtpRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> verifySmsOtpLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> verifySmsOtpLiveData.setValue(ApiResponse.success(result)),
                        throwable -> verifySmsOtpLiveData.setValue(ApiResponse.error(throwable))
                ));



/*
        CapyApplication.getRetroApiClient().verifyOtp(verifyOtpRequest).enqueue(object : Callback<VerifySmsOtpResponse> {
            override fun onFailure(call: Call<VerifySmsOtpResponse>, t: Throwable) {
                mProgress?.value = false
                Utils.showMessage(activity.getString(R.string.str_wrong), activity)

            }

            override fun onResponse(call: Call<VerifySmsOtpResponse>, response: Response<VerifySmsOtpResponse>) {

                mProgress?.value = false
                otpResponse?.value = response.body()

            }
        })
*/

    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
