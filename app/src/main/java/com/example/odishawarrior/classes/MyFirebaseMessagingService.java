package com.example.odishawarrior.classes;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.ProductDetailsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //The much awaited product is out...more dynamic and vibrant than before. Much longer text that cannot fit one line...Check it out

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String productID = "ODWR001";
        Class act = MainActivity.class;

        if(remoteMessage.getData().containsKey("intent_type")){
            if(remoteMessage.getData().get("intent_type").equals("product_page")){
                act = ProductDetailsActivity.class;
            }
        }

        if(remoteMessage.getData().containsKey("product_id")){
            productID = remoteMessage.getData().get("product_id");
        }

        triggerNotification(title,body,productID,act);
    }

    private void triggerNotification(String title, String body, String productID, Class cl){

        Random random = new Random();
        int id = random.nextInt(9999);

        Intent intent = new Intent(this, cl);
        intent.putExtra("product_id",productID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTI001")
                .setSmallIcon(R.drawable.home_icon_image)
                .setColor(243)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("The much awaited product is out...more dynamic and vibrant than before. Much longer text that cannot fit one line...Check it out"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());

    }
}
