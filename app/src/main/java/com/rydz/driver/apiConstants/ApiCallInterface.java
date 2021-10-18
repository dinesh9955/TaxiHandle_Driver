package com.rydz.driver.apiConstants;

import cabuser.com.rydz.ui.home.SendOtpRequest;
import cabuser.com.rydz.ui.home.VerifyOtpRequest;
import com.google.gson.JsonElement;
import com.rydz.driver.model.alert.AlertRequest;
import com.rydz.driver.model.chat.ChatHistoryRequest;
import com.rydz.driver.model.forgot.EmailOtpRequest;
import com.rydz.driver.model.forgot.ForgotPasswordRequest;
import com.rydz.driver.model.requests.*;
import com.rydz.driver.model.tripHistory.TriphistoryRequest;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.*;


public interface ApiCallInterface {

    //    @FormUrlEncoded
    @POST(Urls.LOGIN)
    Observable<JsonElement> login(@Body LoginRequest loginRequest);

    @POST(Urls.MOBILELOGIN)
    Observable<JsonElement> loginwithMobile(@Body LoginWithMobileRequest loginRequest);

    @PUT(Urls.EDITPROFILE)
    Observable<JsonElement> edirProfile(@Body EditProfilerequest editRequest);

    @PUT(Urls.CHECKPHONESTATUS)
    Observable<JsonElement> checkPhoneSatus(@Body PhoneStatusRequest editRequest);

    @PUT(Urls.CHANGEEMAIL)
    Observable<JsonElement> changeEmail(@Body UpdateEmailRequest editRequest);


    @PUT(Urls.CHANGEPASSWORD)
    Observable<JsonElement> changePassword(@Body ChangePasswordRequest editRequest);

    @PUT(Urls.CHANGEDRIVERSATUSS)
    Observable<JsonElement> changeDriverStatus(@Body ChangeDriverStatusRequest editRequest);

    @Multipart
    @POST(Urls.UPLOADIMAGE)
    Observable<JsonElement> uploadImage(@Part MultipartBody.Part image);

    @GET(Urls.GETDOCUMENT)
    Observable<JsonElement> getDocument(@Path("id") String id);


    @GET(Urls.GETVEHICLETYPE)
    Observable<JsonElement> getVehicle(@Path("id") String id);


    @GET(Urls.GETUSERRATING)
    Observable<JsonElement> getUserRating(@Path("id") String id);

    @POST(Urls.RATINGTOUSER)
    Observable<JsonElement> getUserRating(@Body UserRatingRequest loginRequest);


    @POST(Urls.TRIPHISTORY)
    Observable<JsonElement> gettripHistoryrequest(@Body TriphistoryRequest loginRequest);


    @POST(Urls.REGISTERCHECK)
    Observable<JsonElement> checkRegisterNumber(@Body RegistrerCheckStatusRequest loginRequest);


    @POST(Urls.CHATHISTORY)
    Observable<JsonElement> executeChatHistory(@Body ChatHistoryRequest loginRequest);


    @POST(Urls.ALERT)
    Observable<JsonElement> executeAlert(@Body AlertRequest loginRequest);

    @POST(Urls.FORGOT_PASSWORD)
    Observable<JsonElement> forgotPassword(@Body ForgotPasswordRequest loginRequest);


    @POST(Urls.EMAIL_OTP)
    Observable<JsonElement> emailOtp(@Body EmailOtpRequest loginRequest);

    @POST(Urls.VERIFY_OTP)
    Observable<JsonElement> verifyOtp(@Body EmailOtpRequest loginRequest);

    @POST(Urls.SUPPORT)
    Observable<JsonElement> executeSuppoert(@Body SupportRequest loginRequest);


    @Multipart
    @PUT(Urls.SCREENCAPTURE)
    Observable<JsonElement> getScreenCapture(@Part("bookingId") RequestBody dataBody, @Part MultipartBody.Part image);

    @Multipart
    @POST(Urls.REGISTER)
    Observable<JsonElement> onProfilePicUpload(
            @Part("data") RequestBody dataBody,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3);


    @GET(Urls.GETDRIVERINFO)
    Observable<JsonElement> getVehicleInfo(@Path("id") String id);

    @PUT(Urls.GETSMSOTP)
    Observable<JsonElement> executeGetSmsOtp(@Body SendOtpRequest sendOtpRequest);

    @PUT(Urls.VERIFYSMSOTP)
    Observable<JsonElement> executeVerifySmsOtp(@Body VerifyOtpRequest verifyOtpRequest);


}
