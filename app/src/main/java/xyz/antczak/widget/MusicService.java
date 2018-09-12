package xyz.antczak.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class MusicService extends Service {

    private final IBinder binder = new LocalBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Integer> tracks;
    private int current = 0;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        tracks = new ArrayList<>();
        tracks.add(R.raw.track1);
        tracks.add(R.raw.track2);
        loadTrack(current);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra("command");
        if (command != null) {
            switch (command) {
                case "play":
                    play();
                    break;
                case "next":
                    next();
                    break;
                case "prev":
                    prev();
                    break;
                case "stop":
                    stop();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void loadTrack(int num) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, tracks.get(num));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.musicText, getTrackName());
        updateWidget(getApplicationContext(), views);
    }

    public void next() {
        current++;
        if (current > (tracks.size() - 1))
            current = 0;
        loadTrack(current);
        play();
    }

    public void prev() {
        current--;
        if (current < 0)
            current = 0;
        loadTrack(current);
        play();
    }

    public void play() {
        if (mediaPlayer == null)
            return;
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.app_widget);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            views.setImageViewResource(R.id.playButton, R.drawable.ic_play_arrow_black_24dp);
        }
        else {
            mediaPlayer.start();
            views.setImageViewResource(R.id.playButton, R.drawable.ic_pause_black_24dp);
        }
        updateWidget(getApplicationContext(), views);
    }

    public void stop() {
        if (mediaPlayer == null)
            return;

        mediaPlayer.stop();
        current = -1;
    }

    public String getTrackName() {
        return getResources().getResourceEntryName(tracks.get(current));
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        super.onDestroy();
    }

    private void updateWidget(Context context, RemoteViews views) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, AppWidget.class);
        appWidgetManager.updateAppWidget(widget, views);
    }
}
