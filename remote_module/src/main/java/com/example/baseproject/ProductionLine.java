package com.example.baseproject;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
public class ProductionLine {
    private static final String TAG = "ProductionLine";
    private static ProductionLine sInstance;
    private Queue<MyProduct> products;
    private boolean canProduce;

    private ProductionLine() {
        canProduce = true;
        products = new LinkedList<>();
    }

    public static synchronized ProductionLine getsInstance() {
        if (sInstance == null) {
            sInstance = new ProductionLine();
        }
        return sInstance;
    }

    public boolean hasProduct(){
        return products.size() != 0;
    }

    public MyProduct getProduct(){
        return products.remove();
    }

    public void startProducing() {
        HandlerThread producingThread = new HandlerThread("producingThread");
        producingThread.start();
        Handler workerHandler = new Handler(producingThread.getLooper());
        workerHandler.post(() -> {
            int i = 1;
            while (true) {
                try {
                    Thread.sleep(3000);
                    String productName = "Product number: " + i + "_" + System.currentTimeMillis();
                    MyProduct myProduct = new MyProduct();
                    myProduct.aName = productName;
                    if (canProduce) {
                        products.add(myProduct);
                        i++;
                        Log.i(TAG, "run: " + productName + "\nNumber: " + products.size());
                        canProduce = products.size() < 10;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
