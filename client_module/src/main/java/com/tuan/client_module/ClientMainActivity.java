package com.tuan.client_module;

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
import com.tuan.client_module.databinding.ClientActivityMainBinding;

public class ClientMainActivity extends AppCompatActivity {
    private static final String TAG = "RemoteService";
    private ClientActivityMainBinding mBinding;
    private IRemoteService iRemoteService;
    private HandlerThread handlerThread;
    private ICallBack callBack = new ICallBack.Stub() {
        @Override
        public void onReceiveProduct(MyProduct myProduct) throws RemoteException {
            runOnUiThread(() -> Toast.makeText(ClientMainActivity.this, "First client: " + myProduct.aName, Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onReceiveFailed() throws RemoteException {
            Toast.makeText(ClientMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        mBinding = ClientActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        String pkgName = "com.example.baseproject";
        String serviceName = "com.example.baseproject.service.RemoteService";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName, serviceName));
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

        handlerThread = new HandlerThread("request");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        mBinding.request.setOnClickListener(v -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        iRemoteService.sendProduct(callBack, iRemoteService.getPid());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

    }
}