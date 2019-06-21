package com.goorwl.wandemo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.TixiHomeResBean;

import java.util.List;

public class TixiResAdapter extends RecyclerView.Adapter<TixiResViewHolder> implements View.OnClickListener {
    private Activity mActivity;

    private List<TixiHomeResBean.DataBean.ChildrenBean> mList;

    private RvItemClick mRvItemClick;

    public void setRvItemClick(RvItemClick rvItemClick) {
        mRvItemClick = rvItemClick;
    }

    public TixiResAdapter(Activity activity, List<TixiHomeResBean.DataBean.ChildrenBean> list) {
        mActivity = activity;
        mList = list;
    }

    @NonNull
    @Override
    public TixiResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_article, parent, false);
        inflate.setOnClickListener(this);
        return new TixiResViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TixiResViewHolder holder, int position) {
        TixiHomeResBean.DataBean.ChildrenBean childrenBean = mList.get(position);
        holder.itemView.setTag(childrenBean.getId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mRvItemClick != null) {
            mRvItemClick.onItemClick(v.getTag());
        }
    }
}

class TixiResViewHolder extends RecyclerView.ViewHolder {

    public final TextView mTvAuthor;
    public final  TextView mTvType;
    public final  TextView mTvTitle;
    public final  TextView mTvDing;
    public final  TextView mTvTime;

    TixiResViewHolder(@NonNull View itemView) {
        super(itemView);

        mTvAuthor = itemView.findViewById(R.id.item_article_author);
        mTvType = itemView.findViewById(R.id.item_article_type);
        mTvTitle = itemView.findViewById(R.id.item_article_title);
        mTvDing = itemView.findViewById(R.id.item_article_ding);
        mTvTime = itemView.findViewById(R.id.item_article_time);
    }
}