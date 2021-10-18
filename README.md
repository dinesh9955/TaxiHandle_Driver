

TrotDrive – Driver (Android)

A list of all external libraries and their licensing 

- Google Maps
- Google places libraray
- Retrofit(Networking calls)
- Fabric
- Crashlytics
- SinchVerification
- Socket.IO-Client
- Glide(Images)
- Firebase/Core
- Firebase/messaging (Push notifications)
- Gson


List of external APIs / services used by the app

BASEIMAGEURL = "https://trotdrive.com:8002/"
BASE_URL = "https://trotdrive.com:8002/v1/"

Phone Login -
https://trotdrive.com:8002/v1/driver/phonelogin

Login With Email -
https://trotdrive.com:8002/v1/driver/login

Register -
https://trotdrive.com:8002/v1/driver/register

Check Register Status -
https://trotdrive.com:8002/v1/driver/chkregstatus

Update Proile -
https://trotdrive.com:8002/v1/driver/profile

Upload Image –
https://trotdrive.com:8002/v1/driver/uploadpic

Update Profile Image-
https://trotdrive.com:8002/v1/driver/profilepic

Update Email -
https://trotdrive.com:8002/v1/driver/email

Update Password -
https://trotdrive.com:8002/v1/driver/password

Update Status -
https://trotdrive.com:8002/v1/driver/status

Check Contact Number -
https://trotdrive.com:8002/v1/driver/phonestatus

Get Driver Rating -
https://trotdrive.com:8002/v1/rating/driver/

Get Driver Document -
https://trotdrive.com:8002/v1/driver/docs/

Get user Rating -
https://trotdrive.com:8002/v1/rating/user

History -
https://trotdrive.com:8002/v1/booking/driver/history

Get Vehicle Type –
https://trotdrive.com:8002/v1/driver/vehicletype/Get Chat History  -
https://trotdrive.com:8002/v1/message/gethistory


Forget Password –
https://trotdrive.com:8002/v1/driver/forgotchangepass

Send Otp Over Email –
https://trotdrive.com:8002/v1/driver/sendmailotp

Verify Otp  -
https://trotdrive.com:8002/v1/driver/veryfyotp

User Support –
https://trotdrive.com:8002/v1/user/support


Google Direction Api  -
https://maps.googleapis.com/maps/api/directions/json?

Functional documentation: what each screen has, which controller is responsible for what functionality –We have used the MVVM architecture in the code. For instance, we used the following:
├─ Model
├─ View
├─ ViewModel

MODEL -
folder has the subfolders in which files are added as  per requirements –
chat
document
RatingModel.swift
triphistory
vehicletype
socketresponse

VIEWS – (Subfolders)
- activities
-  fragments
-  adapters

VIEW MODELS –
ChangeDriverStatusViewModel
AlertViewModel
RegisterViewModel
UserRatingViewModel
ChangePasswordViewModel
ChathistoryViewModel

Following link show how to compile project and install the app in Android. And if any error occurred how to troubleshoot the issues.

https://developer.android.com/studio/intro/migrate


Style guide (guidelines for usage of colors, fonts, and other graphical elements of the app design)
Images & Icons - Resource folder has “Drawable” and “mipmap” folders that have all the icons and app icon respectively used in the project.

Fonts – Montserrat is the custom font that we used in this project.Montserrat – Regular and Montserrat – Bold types are used mainly. Font Size is different as per the screen requirement.

Color Scheme – Colors.xml file has all the color codes which are used in th app.
