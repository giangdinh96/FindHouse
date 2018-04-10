package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by GiangDinh on 09/01/2018.
 */

public class FindHouseInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private RequestManager requestManager;

    public FindHouseInfoWindowAdapter(Context context) {
        this.context = context;
        this.requestManager = Glide.with(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = null;
        if (marker.getTag() instanceof FindHouse) {
            view = LayoutInflater.from(context).inflate(R.layout.item_house_infor_window, null);

            final ImageView ivUserPicture = view.findViewById(R.id.ivUserPicture);
            TextView tvUsername = view.findViewById(R.id.tvUserName);
            TextView tvTime = view.findViewById(R.id.tvPubDate);
            TextView tvAddress = view.findViewById(R.id.tvFullAddress);
            TextView tvPrice = view.findViewById(R.id.tvPrice);
            TextView tvStretch = view.findViewById(R.id.tvStretch);
            FindHouse findHouse = (FindHouse) marker.getTag();

            // set UserPicture
            requestManager.load(findHouse.getUserPicture()).asBitmap().override(50, 50).listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    if (!isFromMemoryCache) marker.showInfoWindow();
                    return false;
                }
            }).into(new BitmapImageViewTarget(ivUserPicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivUserPicture.setImageDrawable(circularBitmapDrawable);
                }
            });

            tvUsername.setText(findHouse.getUserName());
            tvTime.setText(DateUtils.getDateTimeAgoString(-findHouse.getPubDate()));
            tvAddress.setText(TextUtils.formatAddress(findHouse.getFullAddress()));
            tvPrice.setText(TextUtils.formatPrice(findHouse.getPrice()));
            tvStretch.setText(TextUtils.formatStretch(findHouse.getStretch()));
        } else if (marker.getTag().equals(0)) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_infor_window, null);
        } else if (marker.getTag().equals(-1)) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search_infor_window, null);
            TextView tvTitleSearch = view.findViewById(R.id.tvTitleSearch);
            tvTitleSearch.setText(marker.getTitle());
        }
        return view;
    }
}
