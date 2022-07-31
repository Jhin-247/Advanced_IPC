package com.tuan.client_module.presenter.client_main;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.tuan.client_module.utils.Constant.PACKAGE_NAME;
import static com.tuan.client_module.utils.Constant.SERVICE_NAME;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.baseproject.ICallBack;
import com.example.baseproject.IRemoteService;
import com.example.baseproject.MyProduct;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements Contract.Presenter {
    private final Contract.View mView;
    private final Context mContext;
    private final ICallBack mCallback = new ICallBack.Stub() {
        @Override
        public void onReceiveProduct(MyProduct myProduct) {
            mView.onReceiveItem(myProduct);
        }

        @Override
        public void onReceiveFailed() {
            mView.onReceiveFail();
        }
    };
    private boolean isConnected;
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;
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

    private List<MyProduct> myProducts;

    public Presenter(Contract.View mView) {
        this.mView = mView;
        this.mContext = (Context) mView;
        isConnected = false;
        mHandlerThread = new HandlerThread("requesting_thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        myProducts = new ArrayList<>();
    }

    @Override
    public void requestOrReconnect() {
        if (!isConnected) {
            connectServer();
        } else if (iRemoteService != null) {
            mView.onRequesting();
            mHandler.post(() -> {
                try {
                    iRemoteService.sendProduct(mCallback, iRemoteService.getPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void connectServer() {
        Intent intent = new Intent();
        intent.setComponent(
                new ComponentName(
                        PACKAGE_NAME,
                        SERVICE_NAME
                )
        );
        try {
            mContext.startService(intent);
            isConnected = mContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            mView.onConnectSuccess();
        } catch (Exception exception) {
            mView.onConnectFail();
        }
    }

    @Override
    public void disconnectServer() {
        if (isConnected) {
            mContext.unbindService(serviceConnection);
        }
    }
}
