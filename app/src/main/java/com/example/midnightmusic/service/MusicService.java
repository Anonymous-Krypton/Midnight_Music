package com.example.midnightmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionResult;
import androidx.media3.session.SessionToken;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaSession.ControllerInfo;

import com.example.midnightmusic.R;
import com.example.midnightmusic.ui.player.PlayerActivity;
import com.example.midnightmusic.player.MusicPlayerManager;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class MusicService extends MediaSessionService {
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.midnightmusic.MUSIC_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
    
    private MediaSession mediaSession;
    private MusicPlayerManager playerManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        
        playerManager = MusicPlayerManager.getInstance(this);

        PendingIntent sessionActivity = PendingIntent.getActivity(
            this,
            0,
            new Intent(this, PlayerActivity.class),
            PendingIntent.FLAG_IMMUTABLE
        );

        mediaSession = new MediaSession.Builder(this, playerManager.getPlayer())
                .setSessionActivity(sessionActivity)
                .build();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public void onDestroy() {
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
        playerManager.release();
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription(getString(R.string.notification_channel_description));
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
} 