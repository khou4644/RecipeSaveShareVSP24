package com.example.recipesavesharevsp24;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.recipesavesharevsp24.Activities.CreatePostActivity;
import com.example.recipesavesharevsp24.Activities.RecipeShareSave;
import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;

import java.util.List;

public class NoRecipeNotificationReceiver extends BroadcastReceiver {
    public NoRecipeNotificationReceiver(){}

    private static final String CHANNEL_ID = "no_recipe_notification_channel";



    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
        Log.d("API123",""+intent.getAction());

        if(intent.getAction().equals("com.journaldev.broadcastreceiver.SOME_ACTION"))
            Toast.makeText(context, "SOME_ACTION is received", Toast.LENGTH_LONG).show();

        else {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                try {
                    Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkAndShowNotification(Context context, int userId) {
        RecipeShareSaveDAO recipeShareSaveDAO = AppDataBase.getInstance(context).RecipeShareSaveDAO();
        List<RecipeShareSave> userPosts = recipeShareSaveDAO.getPostsByUserId(userId);
        if (userPosts.isEmpty()) {
            showNotification(context);
        }
    }

    private void showNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannel(context);

        Intent intent = CreatePostActivity.intentFactory(context, getCurrentUserId(context));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("No Recipes Yet")
                .setContentText("Get to Cooking!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "No Recipe Notification";
            String description = "Notifies when the user has no recipes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getCurrentUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("com.example.recipesavesharevsp24.PREFERENCES_KEY", Context.MODE_PRIVATE);
        return preferences.getInt("com.example.recipesavesharevsp24.userIdKey", -1);
    }
}