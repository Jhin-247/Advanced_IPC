package com.example.baseproject.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseproject.MyProduct;
import com.example.baseproject.adapter.ProductAdapter;
import com.example.baseproject.databinding.RemoteActivityMainBinding;
import com.example.baseproject.presenter.MainContract;
import com.example.baseproject.presenter.MainPresenter;

import java.util.List;

public class RemoteMainActivity extends AppCompatActivity implements MainContract.MainView {
    private RemoteActivityMainBinding mBinding;

    private MainPresenter mPresenter;
    private boolean isServiceStarted;

    private ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = RemoteActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setupData();
        setupClickListener();
    }

    private void setupData() {
        isServiceStarted = false;
        mPresenter = new MainPresenter(this);
        mAdapter = new ProductAdapter();

        mBinding.rcvProducts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvProducts.setAdapter(mAdapter);
    }

    private void setupClickListener() {
        mBinding.btnStartProduct.setOnClickListener(v -> {
            mPresenter.startService();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceStarted)
            mPresenter.disconnectService();
    }

    @Override
    public void updateView(List<MyProduct> productList) {
        runOnUiThread(() -> mAdapter.setData(productList));
    }
}