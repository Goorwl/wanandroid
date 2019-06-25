package com.goorwl.wandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.HomeArticleResBean;

import java.util.List;

public class XiangmuAdapter extends RecyclerView.Adapter<XiangmuViewHolder> implements View.OnClickListener {
    private List<HomeArticleResBean.DataBean.DatasBean> mDataBean;

    private Context     mContext;
    private RvItemClick mRvItemClick;

    public XiangmuAdapter(List<HomeArticleResBean.DataBean.DatasBean> dataBean, Context context) {
        mDataBean = dataBean;
        mContext = context;
    }

    @NonNull
    @Override
    public XiangmuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_item_xiangmu, parent, false);
        inflate.setOnClickListener(this);
        return new XiangmuViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull XiangmuViewHolder holder, int position) {
        HomeArticleResBean.DataBean.DatasBean datasBean = mDataBean.get(position);

        Glide.with(mContext).load(datasBean.getEnvelopePic()).into(holder.mIvIcon);
        holder.mTvAuthor.setText(datasBean.getAuthor());
        holder.mTvTitle.setText(datasBean.getTitle());
        holder.mTvDes.setText(datasBean.getDesc());
        holder.mTvType.setText(datasBean.getChapterName());
        holder.mTvTime.setText(datasBean.getNiceDate());
        holder.itemView.setTag(datasBean.getLink());
    }

    @Override
    public int getItemCount() {
        return mDataBean.size();
    }

    public void setRvItemClick(RvItemClick rvItemClick) {
        mRvItemClick = rvItemClick;
    }

    @Override
    public void onClick(View v) {
        if (mRvItemClick != null) {
            mRvItemClick.onItemClick(v.getTag());
        }
    }
}

class XiangmuViewHolder extends RecyclerView.ViewHolder {

    final ImageView mIvIcon;
    final TextView  mTvAuthor;
    final TextView  mTvTitle;
    final TextView  mTvDes;
    final TextView  mTvType;
    final TextView  mTvTime;

    XiangmuViewHolder(@NonNull View itemView) {
        super(itemView);
        mIvIcon = itemView.findViewById(R.id.item_xiangmu_iv);
        mTvAuthor = itemView.findViewById(R.id.item_xiangmu_tv_author);
        mTvTitle = itemView.findViewById(R.id.item_xiangmu_tv_title);
        mTvDes = itemView.findViewById(R.id.item_xiangmu_tv_des);
        mTvType = itemView.findViewById(R.id.item_xiangmu_tv_type);
        mTvTime = itemView.findViewById(R.id.item_xiangmu_tv_time);
    }
}