package com.tuan.client_module_2.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseproject.MyProduct;
import com.tuan.client_module_2.R;
import com.tuan.client_module_2.adapter.ProductAdapter;
import com.tuan.client_module_2.databinding.ClientActivityMainBinding;
import com.tuan.client_module_2.presenter.client_main.Contract;
import com.tuan.client_module_2.presenter.client_main.Presenter;

public class ClientMainActivity extends AppCompatActivity implements Contract.View {
    private ClientActivityMainBinding mBinding;
    private Presenter mPresenter;
    private ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ClientActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initVariable();
        connectServer();
        initEvent();

    }

    private void initEvent() {
        mBinding.request.setOnClickListener(v -> {
            mPresenter.requestOrReconnect();
        });
    }

    private void initVariable() {
        mPresenter = new Presenter(this);
        mAdapter = new ProductAdapter();
        mBinding.rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvProduct.setAdapter(mAdapter);
    }

    private void connectServer() {
        mPresenter.connectServer();
    }

    @Override
    public void onReceiveItem(MyProduct myProduct) {
        runOnUiThread(() -> {
            mBinding.tvNotification.setText(R.string.request_success);
            mAdapter.addData(myProduct);
            mBinding.rcvProduct.smoothScrollToPosition(0);
        });
    }

    @Override
    public void onReceiveFail() {
        runOnUiThread(() -> mBinding.tvNotification.setText(R.string.request_failed));
    }

    @Override
    public void onConnectFail() {
        mBinding.request.setText(getString(R.string.retry));
        mBinding.tvNotification.setText(R.string.connection_error);
    }

    @Override
    public void onConnectSuccess() {
        mBinding.request.setText(getString(R.string.request));
        mBinding.tvNotification.setText(R.string.connect_success);
    }

    @Override
    public void onRequesting() {
        mBinding.tvNotification.setText(R.string.requesting);
    }
}