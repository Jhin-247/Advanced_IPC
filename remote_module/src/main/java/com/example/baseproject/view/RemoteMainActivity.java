package com.example.baseproject.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baseproject.IRemoteService;
import com.example.baseproject.databinding.RemoteActivityMainBinding;
import com.example.baseproject.presenter.MainContract;
import com.example.baseproject.presenter.MainPresenter;
import com.example.baseproject.service.RemoteService;

public class RemoteMainActivity extends AppCompatActivity implements MainContract.MainView {
    private RemoteActivityMainBinding mBinding;

    private MainPresenter mPresenter;
    private IRemoteService iService;

    ServiceConnection bindService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = RemoteActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mPresenter = new MainPresenter(this);
        setupClickListener();
    }

    private void setupClickListener() {
        mBinding.btnStartProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, RemoteService.class);
            bindService(intent, bindService, BIND_AUTO_CREATE);
            startService(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}