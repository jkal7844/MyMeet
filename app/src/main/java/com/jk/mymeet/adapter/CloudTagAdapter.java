package com.jk.mymeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jk.framework.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

/**
 * Created By Admin on 2020/5/26
 * Describe: 3D球体星球适配器
 */
public class CloudTagAdapter extends TagsAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;

    public CloudTagAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        View mView = null;
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            mView = inflater.inflate(R.layout.layout_star_view_item, null);
            //初始化控件
            viewHolder.iv_star_icon = mView.findViewById(R.id.iv_star_icon);
            viewHolder.tv_star_name = mView.findViewById(R.id.tv_star_name);
            mView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) mView.getTag();
        }

        viewHolder.iv_star_icon.setImageResource(R.drawable.icon_message);

        return mView;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }

    class ViewHolder {
        private ImageView iv_star_icon;
        private TextView tv_star_name;
    }
}
