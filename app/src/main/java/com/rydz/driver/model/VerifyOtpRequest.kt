package cabuser.com.rydz.ui.home

data class VerifyOtpRequest(
        var otp: String = "",
        var mobile: String = ""
)