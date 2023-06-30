package com.example.equalizer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


import com.example.equalizer.model.InfoMusic;

import java.io.Serializable;
import java.util.ArrayList;

public class BroadcastService extends NotificationListenerService {
    ArrayList<InfoMusic> musicList = new ArrayList<>();
    public String title,name;
    private AudioManager audioManager;
    private static final String spotify = "com.spotify.music";
    private static final String zingmp3 = "com.zing.mp3";
    private static final String nhaccuatui = "ht.nct";
    private static final String musicplayer = "musicplayer.musicapps.music.mp3player";
        private static final String keeng = "com.vttm.keeng";
    private static final String soundcloud = "com.soundcloud.android";
    private static final String pimusic = "com.Project100Pi.themusicplayer";
    private static final String ytmusic = "com.google.android.apps.youtube.music";
    String categoryTitle;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String s = intent.getPackage();
        Log.d("noti", "p: " + s);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        // Lấy thông tin về thông báo
        String packageName = sbn.getPackageName();
//        PackageManager pm = getPackageManager();
//        ApplicationInfo applicationInfo = null;
//        try {
//            applicationInfo = pm.getApplicationInfo(packageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int appCategory = applicationInfo.category;
//             categoryTitle = (String) ApplicationInfo.getCategoryTitle(this, appCategory);
//
//            // ...
//        }
//        if (!sbn.getPackageName().equals(spotify)&&!sbn.getPackageName().equals(zingmp3)&&!sbn.getPackageName().equals(nhaccuatui)
//                &&!sbn.getPackageName().equals(musicplayer)&&!sbn.getPackageName().equals(keeng)
//        &&!sbn.getPackageName().equals(soundcloud)&&!sbn.getPackageName().equals(pimusic)) {return;}
                Log.d("Notification", "Package: " + packageName);

       //get all App Notify Active
       StatusBarNotification[] ssss = getActiveNotifications();
        for(StatusBarNotification s: ssss){
            String s1 = s.getPackageName();

            if(s1.contains("music")||s1.contains("mp3")||s1.contains("sound")||s1.equals("ht.nct")||s1.equals("com.vttm.keeng")){
//                Bitmap bitmap= (Bitmap) s.getNotification().extras.get(Notification.EXTRA_PICTURE);
//                musicList.add(new InfoMusic(s1,bitmap));
            }

            Log.d("notileng",s1);
        }
//        Intent i = new Intent("ListMusic");
//        Bundle b = new Bundle();
//
////        i.putExtra("LIST",(Serializable) musicList);
//        sendBroadcast(i);
        //get Notify App post recent
        Notification notification = sbn.getNotification();
        Bundle bundle = notification.extras;
        if(packageName.contains("music")||packageName.contains("mp3")||packageName.contains("audio")||packageName.contains("sound")||packageName.equals("ht.nct")||packageName.equals("com.vttm.keeng")){
            if(bundle!=null&&bundle.getCharSequence(Notification.EXTRA_TEXT)!=null
                    &&bundle.getCharSequence(Notification.EXTRA_TITLE)!=null
                    &&bundle.get(Notification.EXTRA_LARGE_ICON)!=null   ) {
                name = bundle.getCharSequence(Notification.EXTRA_TEXT).toString();
                title = bundle.getCharSequence(Notification.EXTRA_TITLE).toString();
//                title = bundle.getCharSequence(Notification.EXTRA_MEDIA_SESSION).toString();
                Icon largeIcon =  notification.getLargeIcon();
                if( largeIcon!=null){
                    Log.d("fat","bitmapone");
                }
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (name != null && title != null) {//&& audioManager.isMusicActive()
                    Intent intent = new Intent("keySong"); //FILTER is a string to identify this intent
                    intent.putExtra("nameSong", name);
                    intent.putExtra("titleSong", title);
                    intent.putExtra("bitmap", largeIcon);
                    intent.putExtra("package", packageName);
                    sendBroadcast(intent);
                }
            }
        }
    }

    private PendingIntent getPendingIntent(Context context, int action){
        Intent intent = new Intent(this,BroadcastService.class);
        intent.putExtra("action_music",action);
        return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

}
