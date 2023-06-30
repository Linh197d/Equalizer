package com.example.equalizer.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class InfoMusic implements Serializable {
    private String namePk;
    private Bitmap bitmap;

    public InfoMusic(String namePk, Bitmap bitmap) {
        this.namePk = namePk;
        this.bitmap = bitmap;
    }

    protected InfoMusic(Parcel in) {
        namePk = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }
    public String getNamePk() {
        return namePk;
    }

    public void setNamePk(String namePk) {
        this.namePk = namePk;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
