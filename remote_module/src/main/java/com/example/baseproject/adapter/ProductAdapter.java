package com.example.baseproject.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.MyProduct;
import com.example.baseproject.R;
import com.example.baseproject.databinding.ItemProductBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<MyProduct> productList;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MyProduct> data) {
        this.productList = data;
        notifyDataSetChanged();
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
