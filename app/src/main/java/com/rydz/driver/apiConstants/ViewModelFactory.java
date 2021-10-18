package com.rydz.driver.apiConstants;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import com.rydz.driver.viewModel.ConfirmTripViewModel;
import com.rydz.driver.viewModel.login.GetDocumentViewModel;
import com.rydz.driver.viewModel.login.LoginViewModel;
import com.rydz.driver.viewModel.login.Repository;
import com.rydz.driver.viewModel.login.editProfile.*;
import com.rydz.driver.viewModel.login.tripHistory.TripHistoryViewModel;


import javax.inject.Inject;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    public Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(EditProfileViewModel.class)) {
            return (T) new EditProfileViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChangeDriverStatusViewModel.class)) {
            return (T) new ChangeDriverStatusViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChangeEmailViewModel.class)) {
            return (T) new ChangeEmailViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChangePasswordViewModel.class)) {
            return (T) new ChangePasswordViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChangePhoneStatusViewModel.class)) {
            return (T) new ChangePhoneStatusViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(UploadImageViewModel.class)) {
            return (T) new UploadImageViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(GetDocumentViewModel.class)) {
            return (T) new GetDocumentViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(UserRatingViewModel.class)) {
            return (T) new UserRatingViewModel(repository);
        }

        else if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChangePhoneStatusViewModel.class)) {
            return (T) new ChangePhoneStatusViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(TripHistoryViewModel.class))
        {
            return (T) new TripHistoryViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel.class))
        {
            return (T) new RegisterViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ChathistoryViewModel.class))
        {
            return (T) new ChathistoryViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(AlertViewModel.class))
        {
            return (T) new AlertViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(SupportViewModel.class))
        {
            return (T) new SupportViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ConfirmTripViewModel.class))
        {
            return (T) new ConfirmTripViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
