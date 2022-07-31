package com.example.baseproject.presenter;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.baseproject.IRemoteService;
import com.example.baseproject.MyProduct;
import com.example.baseproject.observer.Observer;
import com.example.baseproject.observer.ProductObserver;
import com.example.baseproject.service.RemoteService;

import java.util.List;

public class MainPresenter implements MainContract.MainPresenter, Observer {
    private final MainContract.MainView mView;
    private final Context mContext;
    private IRemoteService iService;
    private final ServiceConnection bindService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
        }
    };
    private boolean isServiceConnected;

    public MainPresenter(MainContract.MainView mView) {
        this.mView = mView;
        this.mContext = (Context) mView;
        isServiceConnected = false;
    }

    @Override
    public void notifyProducts(List<MyProduct> productList) {
        mView.updateView(productList);
    }

    @Override
    public void startService() {
        if (!isServiceConnected) {
            Intent intent = new Intent(mContext, RemoteService.class);
            isServiceConnected = mContext.bindService(intent, bindService, BIND_AUTO_CREATE);
            mContext.startService(intent);
            ProductObserver.getInstance().addObserver(this);
        }
    }

    @Override
    public void disconnectService() {
        if (isServiceConnected) {
            mContext.unbindService(bindService);
            ProductObserver.getInstance().removeObserver(this);
        }

    }
}
