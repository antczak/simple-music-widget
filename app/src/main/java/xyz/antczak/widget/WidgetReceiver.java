package xyz.antczak.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetReceiver extends BroadcastReceiver {

    private MusicService musicService;
    private boolean bound = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Constants.ACTION_WEBSITE:
                onUrl(context, intent);
                break;
            case Constants.ACTION_IMAGES:
                onImages(context, intent);
                break;
            case Constants.ACTION_MUSIC:
                onMusic(context, intent);
                break;
            case Constants.ACTION_FLIP:
                onFlip(context, intent);
                break;
            case Constants.ACTION_MUSIC_PLAY:
                onPlay(context, intent);
                break;
            case Constants.ACTION_MUSIC_NEXT:
                onNext(context, intent);
                break;
            case Constants.ACTION_MUSIC_PREV:
                onPrev(context, intent);
                break;
            case Constants.ACTION_MUSIC_STOP:
                onStop(context, intent);
                break;
        }
        context.startService(new Intent(context, MusicService.class));
    }

    private void onUrl(Context context, Intent intent) {
        String url = intent.getStringExtra("url");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(webIntent);
    }

    private void onImages(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setViewVisibility(R.id.imagesLayout, View.VISIBLE);
        views.setViewVisibility(R.id.musicLayout, View.INVISIBLE);
        updateWidget(context, views);
    }

    private void onMusic(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setViewVisibility(R.id.musicLayout, View.VISIBLE);
        views.setViewVisibility(R.id.imagesLayout, View.INVISIBLE);
        updateWidget(context, views);
    }

    private void onFlip(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.showNext(R.id.imageFlip);
        updateWidget(context, views);
    }

    private void onPlay(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent i = new Intent(context, MusicService.class);
        i.putExtra("command", "play");
        context.startService(i);
        updateWidget(context, views);
    }

    private void onNext(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent i = new Intent(context, MusicService.class);
        i.putExtra("command", "next");
        context.startService(i);
        updateWidget(context, views);
    }

    private void onPrev(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent i = new Intent(context, MusicService.class);
        i.putExtra("command", "prev");
        context.startService(i);
        updateWidget(context, views);
    }

    private void onStop(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent i = new Intent(context, MusicService.class);
        i.putExtra("command", "stop");
        context.startService(i);
        updateWidget(context, views);
    }

    private void updateWidget(Context context, RemoteViews views) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, AppWidget.class);
        appWidgetManager.updateAppWidget(widget, views);
    }



}
