package com.example.baseproject.service;

import static com.example.baseproject.service.ApplicationController.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.baseproject.ICallBack;
import com.example.baseproject.IRemoteService;
import com.example.baseproject.ProductionLine;
import com.example.baseproject.R;

public class RemoteService extends Service {
    private HandlerThread requestThread;
    private final IRemoteService.Stub iRemoteService = new IRemoteService.Stub() {
        @Override
        public int getPid() {
            return Process.myPid();
        }

        @Override
        public void sendProduct(ICallBack callBack, int pId) {
            Handler handler = new Handler(requestThread.getLooper());
            handler.post(() -> {
                for (; ; ) {
                    if (ProductionLine.getsInstance().hasProduct()) {
                        try {
                            callBack.onReceiveProduct(ProductionLine.getsInstance().getProduct());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            });
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iRemoteService.asBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        initProductionChain();
        return START_NOT_STICKY;
    }

    private void createNotification() {
        Notification notification = new NotificationCompat.Builder(getApplication(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Progressing")
                .build();
        startForeground(1, notification);
    }

    private void initProductionChain() {
        ProductionLine.getsInstance().startProducing();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        requestThread = new HandlerThread("request_thread");
        requestThread.start();
    }
}
