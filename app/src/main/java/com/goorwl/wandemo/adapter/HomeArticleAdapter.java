package com.goorwl.wandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.HomeArticleResBean;
import com.goorwl.wandemo.utils.TimeUtils;

public class HomeArticleAdapter extends RecyclerView.Adapter<HomeArticleViewholder> implements View.OnClickListener {

    private HomeArticleResBean.DataBean mDataBean;
    private Context                     mContext;

    private RvItemClick mRvItemClick;

    public void setRvItemClick(RvItemClick rvItemClick) {
        if (mRvItemClick == null) {
            mRvItemClick = rvItemClick;
        }
    }

    public HomeArticleAdapter(HomeArticleResBean.DataBean dataBean, Context context) {
        mDataBean = dataBean;
        mContext = context;
    }

    public HomeArticleResBean.DataBean getDataBean() {
        return mDataBean;
    }

    public void setDataBean(HomeArticleResBean.DataBean dataBean) {
        mDataBean = null;
        mDataBean = dataBean;
    }

    @NonNull
    @Override
    public HomeArticleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_home_article, parent, false);
        inflate.setOnClickListener(this);
        return new HomeArticleViewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeArticleViewholder holder, int position) {
        HomeArticleResBean.DataBean.DatasBean datasBean = mDataBean.getDatas().get(position);
        holder.mTvAuthor.setText(String.valueOf(datasBean.getAuthor()));
        holder.mTvType.setText(String.valueOf(datasBean.getSuperChapterName()));
        holder.mTvTitle.setText(String.valueOf(datasBean.getTitle()));
        if (datasBean.getZan() == 1) {
            holder.mTvDing.setVisibility(View.VISIBLE);
        } else {
            holder.mTvDing.setVisibility(View.INVISIBLE);
        }
//        holder.mTvTime.setText(TimeUtils.stampToString(datasBean.getPublishTime()));
        holder.mTvTime.setText(datasBean.getNiceDate());
        holder.itemView.setTag(datasBean.getLink());
    }

    @Override
    public int getItemCount() {
        return mDataBean.getDatas().size();
    }

    @Override
    public void onClick(View v) {
        mRvItemClick.onItemClick((String) v.getTag());
    }
}

class HomeArticleViewholder extends RecyclerView.ViewHolder {

    public final TextView mTvAuthor;
    public final TextView mTvType;
    public final TextView mTvTitle;
    public final TextView mTvDing;
    public final TextView mTvTime;

    public HomeArticleViewholder(@NonNull View itemView) {
        super(itemView);

        mTvAuthor = itemView.findViewById(R.id.item_article_author);
        mTvType = itemView.findViewById(R.id.item_article_type);
        mTvTitle = itemView.findViewById(R.id.item_article_title);
        mTvDing = itemView.findViewById(R.id.item_article_ding);
        mTvTime = itemView.findViewById(R.id.item_article_time);
    }
}
