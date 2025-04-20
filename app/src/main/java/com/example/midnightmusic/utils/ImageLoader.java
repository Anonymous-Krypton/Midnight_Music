package com.example.midnightmusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.InputStream;
import java.net.URL;

public class ImageLoader {
    public static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;
        private final String originalUrl;

        public LoadImageTask(ImageView imageView, String url) {
            this.imageView = imageView;
            this.originalUrl = url;
            // Set tag to current URL to prevent wrong image loading
            imageView.setTag(url);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                String secureUrl = urls[0].replace("http://", "https://");
                InputStream in = new URL(secureUrl).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Check if ImageView still needs this image
            if (imageView.getTag() != null && imageView.getTag().equals(originalUrl)) {
                if (result != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }

    public static void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            new LoadImageTask(imageView, url).execute(url);
        }
    }
} 