package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 26/01/2018.
 */

public class HousePicturePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> pictures;
    private String[] picturesUrl;
    private RequestManager requestManager;

    public HousePicturePagerAdapter(Context context, HouseForRent houseForRent) {
        this.context = context;
        pictures = houseForRent.getPicturesList();
        picturesUrl = new String[pictures.size()];
        if (houseForRent.getFirstPictureUrl() != null) {
            picturesUrl[0] = houseForRent.getFirstPictureUrl();
        }
        requestManager = Glide.with(context);
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_house_detail, container, false);
//
//        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivHousePicturePager);
//        imageView.setImageResource(mResources[position]);
////        imageView.setImageDrawable (container.getResources().getDrawable(mResources[position]));
//        container.addView(itemView);

        final ImageView itemView = new ImageView(context);
        itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (picturesUrl[position] != null) {
            requestManager.load(picturesUrl[position]).into(itemView);
        } else {
            FirebaseStorage.getInstance().getReference().child(pictures.get(position).toString()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        picturesUrl[position] = task.getResult().toString();
                        requestManager.load(picturesUrl[position]).into(itemView);
                    }
                }
            });
        }
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}