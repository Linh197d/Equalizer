package com.example.equalizer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioPlaybackConfiguration;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.equalizer.model.EqualizerModel;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_MEDIA_PROJECTION = 1;

    public final String TAG = "nother";
    public MediaPlayer mediaPlayer;
    EqualizerFragment equalizerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
//            // Yêu cầu quyền truy cập thông báo
//            requestPermissions(new String[]{Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE}, PERMISSION_REQUEST_CODE);
//
//        } else {
//            // Quyền đã được cấp, tiếp tục xử lý thông báo
///// receiver
//            Log.d("fat","Quyền cấp");
//                }
        loadEqualizerSettings();
//            BroadcastService broadcastService = new BroadcastService();
//            broadcastService.setFragmentDataListener(equalizerFragment);
//        Toast.makeText(this, "Ứng dụng cần được cấp quyền truy cập thông báo", Toast.LENGTH_LONG).show();
        equalizerFragment = EqualizerFragment.newBuilder()
                .setAccentColor(Color.parseColor("#4caf50"))
                .setAudioSessionId(0)
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eqFrame, equalizerFragment)
                .commit();
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Quyền đã được cấp, tiếp tục xử lý thông báo
//            } else {
//                // Quyền bị từ chối, xử lý tương ứng (ví dụ: hiển thị thông báo cho người dùng)
//                // ...
//            }
//        }
//    }

    private static final int PERMISSION_REQUEST_CODE = 1;

//    private void checkStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Yêu cầu quyền từ người dùng
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST_CODE);
//        } else {
//            // Ứng dụng đã có quyền truy cập
//            // Tiếp tục xử lý truy cập vào dữ liệu từ Content Provider
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Quyền đã được cấp
//                // Tiếp tục xử lý truy cập vào dữ liệu từ Content Provider
//                checkStoragePermission();
//            } else {
//                // Quyền bị từ chối, xử lý tương ứng (ví dụ: thông báo cho người dùng)
//                Toast.makeText(this, "Quyền bị từ chối ", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        saveEqualizerSettings();
//        Intent intent = new Intent(this,MusicService.class);
    }

    @Override
    protected void onPause() {
        try {
            mediaPlayer.pause();
        } catch (Exception ex) {

        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

        } catch (Exception ex) {
            //ignore
        }
//        Intent musicServiceIntent = new Intent(this, MusicService.class);
//        stopService(musicServiceIntent);
    }


    private void saveEqualizerSettings() {
        if (Settings.equalizerModel != null) {
            EqualizerSettings settings = new EqualizerSettings();
            settings.bassStrength = Settings.equalizerModel.getBassStrength();
            settings.presetPos = Settings.equalizerModel.getPresetPos();
            settings.reverbPreset = Settings.equalizerModel.getReverbPreset();
            settings.seekbarpos = Settings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            Gson gson = new Gson();
            preferences.edit()
                    .putString(PREF_KEY, gson.toJson(settings))
                    .apply();
        }
    }

    private void loadEqualizerSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);

        Settings.isEqualizerEnabled = true;
        Settings.isEqualizerReloaded = true;
        Settings.bassStrength = settings.bassStrength;
        Settings.presetPos = settings.presetPos;
        Settings.reverbPreset = settings.reverbPreset;
        Settings.seekbarpos = settings.seekbarpos;
        Settings.equalizerModel = model;
    }

    public static final String PREF_KEY = "equalizer";
}
