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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.baseproject.ICallBack;
import com.example.baseproject.IRemoteService;
import com.example.baseproject.MyProduct;
import com.example.baseproject.R;

import java.util.LinkedList;
import java.util.Queue;

public class RemoteService extends Service {
    private static final String TAG = RemoteService.class.getSimpleName() + "Remote";
    private HandlerThread productionThread;
    private HandlerThread requestThread;
    private Queue<MyProduct> mProductQueue;
    private final IRemoteService.Stub iRemoteService = new IRemoteService.Stub() {
        @Override
        public int getPid() {
            return Process.myPid();
        }

        @Override
        public void sendProduct(ICallBack callBack, int pId) throws RemoteException {
            Handler handler = new Handler(requestThread.getLooper());
            handler.post(() -> {
                for (; ; ) {
                    if (mProductQueue.size() > 0) {
                        try {
                            callBack.onReceiveProduct(mProductQueue.remove());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "sendProduct: ");
                        break;
                    }
                }
            });
        }
    };
    private boolean canProduce = true;
    private final Runnable runnable = new Runnable() {
        @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
        @Override
        public void run() {
            int i = 1;
            while (true) {
                try {
                    Thread.sleep(5000);
                    String productName = "Product : " + System.currentTimeMillis();
                    MyProduct myProduct = new MyProduct();
                    myProduct.mProductName = productName;
                    myProduct.mId = i;
                    if (canProduce) {
                        mProductQueue.add(myProduct);
                        i++;
                        Log.i(TAG, "run: " + productName + "\nNumber: " + mProductQueue.size());
                        canProduce = mProductQueue.size() < 10;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: `");
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
        Log.i(TAG, "initProductionChain: ");
        Handler handler = new Handler(productionThread.getLooper());
        handler.post(runnable);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        mProductQueue = new LinkedList<>();
        productionThread = new HandlerThread("production_thread");
        productionThread.start();

        requestThread = new HandlerThread("request_thread");
        requestThread.start();
    }
}
