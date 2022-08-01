package com.tuan.client_module_2.presenter.client_main;

import com.example.baseproject.MyProduct;

public interface Contract {
    interface View {
        void onReceiveItem(MyProduct myProduct);
        void onReceiveFail();
        void onConnectFail();
        void onConnectSuccess();
        void onRequesting();
    }

    interface Presenter {
        void requestOrReconnect();
        void connectServer();
        void disconnectServer();
    }

}
