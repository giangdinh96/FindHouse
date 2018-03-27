package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GiangDinh on 02/02/2018.
 */

public class ProvincesAdapter extends BaseAdapter {

    private Context context;
    private List<Province> provinces;
    private LayoutInflater layoutInflater;

    public ProvincesAdapter(Context context, List<Province> provinces) {
        this.context = context;
        this.provinces = provinces;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return provinces.size();
    }

    @Override
    public Object getItem(int i) {
        return provinces.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProvinceHolder provinceHolder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_province, viewGroup, false);
            provinceHolder = new ProvinceHolder(view);
            view.setTag(provinceHolder);
        } else {
            provinceHolder = (ProvinceHolder) view.getTag();
        }
        Province province = provinces.get(i);
        provinceHolder.tvProvinceName.setText(province.getName());
        if (i == 0) {
            provinceHolder.tvProvinceName.setTextColor(Color.GRAY);
        } else {
            provinceHolder.tvProvinceName.setTextColor(Color.BLACK);
        }
        return view;
    }

    public static class ProvinceHolder {
        @BindView(R.id.tvProvinceName)
        TextView tvProvinceName;

        public ProvinceHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
