package com.example.firebasefileslisting;

import android.net.Uri;

public class Video {
    String name;
    Uri uri;

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }
}