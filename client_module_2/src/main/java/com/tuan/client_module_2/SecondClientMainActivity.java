package com.tuan.client_module_2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baseproject.ICallBack;
import com.example.baseproject.IRemoteService;
import com.example.baseproject.MyProduct;
import com.tuan.client_module_2.databinding.ActivitySecondClientMainBinding;

public class SecondClientMainActivity extends AppCompatActivity {
    private ActivitySecondClientMainBinding mBinding;
    private final ICallBack callBack = new ICallBack.Stub() {
        @Override
        public void onReceiveProduct(MyProduct myProduct) {
            runOnUiThread(() -> mBinding.tvRes.setText(getString(R.string.result, myProduct.mId, myProduct.mProductName)));
        }

        @Override
        public void onReceiveFailed() {
            runOnUiThread(() -> Toast.makeText(SecondClientMainActivity.this, "Error", Toast.LENGTH_SHORT).show());
        }
    };
    private IRemoteService iRemoteService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iRemoteService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySecondClientMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        String packageName = "com.example.baseproject";
        String serviceName = "com.example.baseproject.service.RemoteService";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, serviceName));
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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