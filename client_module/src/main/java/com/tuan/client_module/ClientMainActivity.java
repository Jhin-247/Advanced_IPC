package com.tuan.client_module;

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
import com.tuan.client_module.databinding.ClientActivityMainBinding;

public class ClientMainActivity extends AppCompatActivity {
    private ClientActivityMainBinding mBinding;
    private final ICallBack callBack = new ICallBack.Stub() {
        @Override
        public void onReceiveProduct(MyProduct myProduct) {
            runOnUiThread(() -> mBinding.tvRes.setText(getString(R.string.result, myProduct.mId, myProduct.mProductName)));
        }

        @Override
        public void onReceiveFailed() {
            runOnUiThread(() -> Toast.makeText(ClientMainActivity.this, "Error", Toast.LENGTH_SHORT).show());
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
        mBinding = ClientActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        String pkgName = "com.example.baseproject";
        String serviceName = "com.example.baseproject.service.RemoteService";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName, serviceName));
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        HandlerThread handlerThread = new HandlerThread("request");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        mBinding.request.setOnClickListener(v -> handler.post(() -> {
            if (iRemoteService != null)
                try {
                    iRemoteService.sendProduct(callBack, iRemoteService.getPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }));

    }
}