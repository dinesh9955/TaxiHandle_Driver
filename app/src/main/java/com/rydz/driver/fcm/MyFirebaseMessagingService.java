package com.rydz.driver.fcm;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rydz.driver.CommonUtils.AppConstants;
import com.rydz.driver.CommonUtils.sharedpreferences.MySharedPreferences;
import com.rydz.driver.R;
import com.rydz.driver.view.activity.MainActivity;
import com.rydz.driver.view.fragment.ChatFargment;
import org.json.JSONObject;

import java.util.List;

import static com.rydz.driver.CommonUtils.AppConstants.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static Boolean inChatScreen = false;
    public static String CHANNEL_ID = "com.rydz.driver";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    private Long acceptReceiveTime = 0L;


    @Override
    public void onNewToken(String token) {
        getMyPreferences().setStringValue(DEVICETOKEN,token);
        /*if (token != null && token.length > 0) {
            val prefs = PreferenceHelper.defaultPrefs(this)
            prefs[PreferenceHelper.Key.FCMTOKEN] = token
            RydzApplication.deviceToken = token

            val editor = prefs.edit()
            editor.putString(PreferenceHelper.Key.FCMTOKEN, token)
            editor.commit()
        }*/
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            Log.e("Active process : ", activeProcess);
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                Log.e("componentInfo : ", componentInfo.toString());
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return isInBackground;
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (isLogin()) {


            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
            try {
                Log.e("remotedata : ", remoteMessage.getData().toString());
                if (remoteMessage.getData().size() > 0) {
                    JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                    Log.e("jsonObject", jsonObject.toString());
                    if (jsonObject.get("type").toString().equals("51")) {
                        acceptReceiveTime = remoteMessage.getSentTime();
                    }

                    displayCustomNotificationForOrders(jsonObject);
              /*      if (isAppIsInBackground(getApplicationContext())) {
                        displayCustomNotificationForOrders(jsonObject);
                        // Also if you intend on generating your own notifications as a result of a received FCM
                        // message, here is where that should be initiated. See sendNotification method below.
                    } else {
                        if (inChatScreen) {
                            if (jsonObject.get("type").toString().equals("52"))
                            {
                                displayCustomNotificationForOrders(jsonObject);
                            }

                        } else {
                            if (jsonObject.get("type").toString().equals("53")) {
                                displayCustomNotificationForOrders(jsonObject);
                            }
                            else
                            {
                                displayCustomNotificationForOrders(jsonObject);
                            }
                        }
                    }*/


                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public MySharedPreferences getMyPreferences() {
        return new MySharedPreferences(this);
    }

    public Boolean isLogin() {
        return getMyPreferences().getStringValue(AppConstants.USER_ID) != null && !getMyPreferences().getStringValue(AppConstants.USER_ID).isEmpty();
    }

    @SuppressLint("WrongConstant")
    private void displayCustomNotificationForOrders(JSONObject jsonObject) {
        Log.e("DATA : ", jsonObject + "");
        try {
            if (jsonObject != null) {
                if (notifManager == null) {
                    notifManager = (NotificationManager) getSystemService
                            (Context.NOTIFICATION_SERVICE);
                }
                String title = getString(R.string.you_have_new_message);
                String type = jsonObject.get("type").toString();
                String message = "";
//                Log.e("166",MainActivity.mainActivity.getFragmentTag());
                if (!isAppIsInBackground(getApplicationContext())) {
                    if (type.equals("51")) {
                        if (!isAcceptScreen) {
                            //Log.e("150","isAcceptScreen false");

                            JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                            try {
                                if (acceptReceiveTime < (acceptReceiveTime + 16000))
                                    MainActivity.mainActivity.sendRequesttoFragment(myData.toString());
                                return;

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                        else
                        {
                           // Log.e("150","isAcceptScreen");

                        }
                    } else if (type.equals("16") && (MainActivity.mainActivity.getFragmentTag().equals(AppConstants.HOMESCREEN) ||  MainActivity.mainActivity.getFragmentTag().equals(ACCEPTSCREEN) || MainActivity.mainActivity.getFragmentTag().equals(POOLSCREEN) || (MainActivity.mainActivity.getFragmentTag().equals(PAYSCREEN)))) {
                        Log.e("163",MainActivity.mainActivity.getFragmentTag());
                        MainActivity.mainActivity.sendObjectToSocket();
                    }


                }
                else
                {
                    //Log.e("150","isAcceptScreen 177");
                }


                if (type.equals("53")) {
                    JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                    JSONObject newMessageObject = new JSONObject(myData.get("user").toString());
                    title = getString(R.string.you_have_new_message);
                    message = getString(R.string.you_have_new_message) + " from : " + newMessageObject.get("firstName").toString() + " " + newMessageObject.get("lastName").toString();
                } else {
                    message = jsonObject.get("body").toString();
                }
                int icon = R.mipmap.ic_launcher;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent newintent = new Intent();
                    String packageName = getPackageName();
                    PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                        newintent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        newintent.setData(Uri.parse("package:" + packageName));
                        startActivity(newintent);
                    }
                    NotificationCompat.Builder builder;
                    Intent intent;
                    if (type.equals("53")) {
                        intent = new Intent(this, ChatFargment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                        JSONObject newMessageObject = new JSONObject(myData.get("user").toString());
                        intent.putExtra("fromNotification", "not");
                        intent.putExtra(KEYBOOKINGID, myData.get("bookingId").toString());
                        intent.putExtra(KEYPROFILEPIC, newMessageObject.get("profilePic").toString());
                        intent.putExtra(KEYUSERID, newMessageObject.get("id").toString());
                        intent.putExtra(KEYNAME, newMessageObject.get("firstName").toString() + " " + newMessageObject.get("lastName").toString());
                    } else {
                        intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        if (type.equals("51")) {
                            JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                            intent.putExtra("fromNotification", myData.toString());

                        } else if (type.equals("54")) {
                            intent.putExtra("fromNotification", "54");
                        }
                    }

                    PendingIntent pendingIntent;
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(getPackageName(), title, importance);
                        mChannel.setDescription(message);
                        mChannel.enableVibration(true);
                        notifManager.createNotificationChannel(mChannel);
                    }
                    builder = new NotificationCompat.Builder(this, getPackageName());

                    pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
                    builder.setContentTitle(title)
                            .setSmallIcon(getNotificationIcon()) // required
                            .setContentText(message)  // required
                            .setAutoCancel(true)

                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setLargeIcon(BitmapFactory.decodeResource
                                    (getResources(), icon))
                            .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDarkRed))
                            .setBadgeIconType(icon)
                            .setShowWhen(true)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri
                                    (RingtoneManager.TYPE_NOTIFICATION));
                    Notification notification = builder.build();
                    notifManager.notify(1251, notification);
                } else {

                    Intent intent;
                    if (type.equals("53")) {
                        intent = new Intent(this, ChatFargment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                        JSONObject newMessageObject = new JSONObject(myData.get("user").toString());
                        intent.putExtra("fromNotification", "not");
                        intent.putExtra(KEYBOOKINGID, myData.get("bookingId").toString());
                        intent.putExtra(KEYPROFILEPIC, newMessageObject.get("profilePic").toString());
                        intent.putExtra(KEYUSERID, newMessageObject.get("id").toString());
                        intent.putExtra(KEYNAME, newMessageObject.get("firstName").toString() + " " + newMessageObject.get("lastName").toString());
                    } else {
                        intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        if (type.equals("51")) {
                            JSONObject myData = new JSONObject(jsonObject.get("notiData").toString());
                            intent.putExtra("fromNotification", myData.toString());
                        }
                    }
                    PendingIntent pendingIntent = null;

                    pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDarkRed))
                            .setSound(defaultSoundUri)
                            .setSmallIcon(getNotificationIcon())
                            .setContentIntent(pendingIntent)
                            .setShowWhen(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(message));

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1251, notificationBuilder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getNotificationIcon() {
        int icon = R.drawable.logo;
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? icon : icon;
    }


}