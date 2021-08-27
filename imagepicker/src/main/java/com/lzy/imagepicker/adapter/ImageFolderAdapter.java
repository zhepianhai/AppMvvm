package com.lzy.imagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.bean.ImageFolder;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * 创建日期：2019/4/12
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImageFolderAdapter extends BaseAdapter {

    private ImagePicker imagePicker;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<ImageFolder> imageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity, List<ImageFolder> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0){
            imageFolders = folders;
        } else {
            imageFolders = new ArrayList<>();
        }
        imagePicker = ImagePicker.getInstance();
        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<ImageFolder> folders) {
        if (folders != null && folders.size() > 0) {
            imageFolders = folders;
        }
        else {
            imageFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders==null?0:imageFolders.size();
    }

    @Override
    public ImageFolder getItem(int position) {
        return imageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageFolder folder = getItem(position);
        if (null!=folder){
            holder.folderName.setText(folder.name);
            int folderimgSize=folder.images==null?0:folder.images.size();

            holder.imageCount.setText(
                    mActivity.getString(R.string.ip_folder_image_count
                    , folderimgSize));
            imagePicker
                    .getImageLoader()
                    .displayImage(mActivity
                    , folder.cover.path
                    , holder.cover
                    , mImageSize
                    , mImageSize);
        }


        if (lastSelected == position) {
            holder.folderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.folderCheck.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover =  view.findViewById(R.id.iv_cover);
            folderName =  view.findViewById(R.id.tv_folder_name);
            imageCount =  view.findViewById(R.id.tv_image_count);
            folderCheck =  view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}
