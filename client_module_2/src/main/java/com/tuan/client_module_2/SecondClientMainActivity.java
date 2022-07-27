package com.tuan.client_module_2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baseproject.ICallBack;
import com.example.baseproject.IRemoteService;
import com.example.baseproject.MyProduct;

public class SecondClientMainActivity extends AppCompatActivity {
    private static final String TAG = "RemoteService";
    private IRemoteService iRemoteService;
    private ICallBack callBack = new ICallBack.Stub() {
        @Override
        public void onReceiveProduct(MyProduct myProduct) throws RemoteException {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Second client: " + myProduct.aName, Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onReceiveFailed() throws RemoteException {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_client_main);

        String packageName = "com.example.baseproject";
        String serviceName = "com.example.baseproject.service.RemoteService";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, serviceName));
        ComponentName name = startService(intent);
        if (name == null) {
            Log.e(TAG, "onCreate: Cannot found");
        } else {
            Log.e(TAG, "onCreate: found");
        }
//
        boolean res = bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        if (res) {
            Log.e(TAG, "onCreate: connected");
        } else {
            Log.e(TAG, "onCreate: cannot connect");
        }

        findViewById(R.id.request).setOnClickListener(v -> {
            HandlerThread handlerThread = new HandlerThread("request");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            handler.post(() -> {
                try {
                    iRemoteService.sendProduct(callBack, iRemoteService.getPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });

    }
}