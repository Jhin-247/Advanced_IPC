package com.tuan.client_module_2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.MyProduct;
import com.tuan.client_module_2.R;
import com.tuan.client_module_2.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<MyProduct> productList;

    public ProductAdapter() {
        productList = new ArrayList<>();
    }

    public void addData(MyProduct data) {
        this.productList.add(0, data);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        MyProduct myProduct = productList.get(position);
        holder.mBinding.tvId.setText(
                holder.mBinding.getRoot().getContext().getString(
                        R.string.product_id, myProduct.mId
                )
        );
        holder.mBinding.tvName.setText(
                holder.mBinding.getRoot().getContext().getString(
                        R.string.product_name, myProduct.mProductName
                )
        );
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {
        ItemProductBinding mBinding;

        public ProductHolder(@NonNull ItemProductBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
