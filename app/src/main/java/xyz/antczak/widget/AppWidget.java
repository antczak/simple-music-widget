package xyz.antczak.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        views.setOnClickPendingIntent(R.id.webButton, getWebIntent(context, "http://google.pl"));
        views.setOnClickPendingIntent(R.id.imagesButton, getIntent(context, Constants.ACTION_IMAGES));
        views.setOnClickPendingIntent(R.id.musicButton, getIntent(context, Constants.ACTION_MUSIC));
        views.setOnClickPendingIntent(R.id.imageFlip, getIntent(context, Constants.ACTION_FLIP));
        views.setOnClickPendingIntent(R.id.playButton, getIntent(context, Constants.ACTION_MUSIC_PLAY));
        views.setOnClickPendingIntent(R.id.nextButton, getIntent(context, Constants.ACTION_MUSIC_NEXT));
        views.setOnClickPendingIntent(R.id.previousButton, getIntent(context, Constants.ACTION_MUSIC_PREV));
        views.setOnClickPendingIntent(R.id.stopButton, getIntent(context, Constants.ACTION_MUSIC_STOP));

        addImage(views, context, R.drawable.android);
        addImage(views, context, R.drawable.chrome);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getWebIntent(Context context, String url) {
        Intent intent = new Intent(Constants.ACTION_WEBSITE);
        intent.putExtra("url", url);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static PendingIntent getIntent(Context context, String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static void addImage(RemoteViews mainViews, Context context, int res) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flipper_item);
        views.setImageViewResource(R.id.imageView, res);
        mainViews.addView(R.id.imageFlip, views);
    }
}

