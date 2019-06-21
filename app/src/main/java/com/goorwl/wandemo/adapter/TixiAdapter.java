package com.goorwl.wandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.TixiHomeResBean;

import java.util.List;

public class TixiAdapter extends RecyclerView.Adapter<TixiViewHolder> implements View.OnClickListener {

    private List<TixiHomeResBean.DataBean> mDataBeans;

    private Context     mContext;
    private RvItemClick mItemOnClick;

    public void setItemOnClick(RvItemClick itemOnClick) {
        mItemOnClick = itemOnClick;
    }

    public TixiAdapter(Context context, List<TixiHomeResBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
        mContext = context;
    }

    @NonNull
    @Override
    public TixiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_item_tixi, parent, false);
        inflate.setOnClickListener(this);
        return new TixiViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TixiViewHolder holder, int position) {
        holder.mTvTitle.setText(mDataBeans.get(position).getName());
        List<TixiHomeResBean.DataBean.ChildrenBean> children      = mDataBeans.get(position).getChildren();
        StringBuilder                               stringBuilder = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            stringBuilder.append(children.get(i).getName()).append(" ");
        }
        holder.mTvContent.setText(stringBuilder.toString());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemOnClick != null) {
            mItemOnClick.onItemClick(v.getTag());
        }
    }
}

class TixiViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvTitle;
    public TextView mTvContent;

    public TixiViewHolder(@NonNull View itemView) {
        super(itemView);
        mTvTitle = itemView.findViewById(R.id.item_tixi_title);
        mTvContent = itemView.findViewById(R.id.item_tixi_content);
    }
}
