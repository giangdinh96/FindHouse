package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.R;

import java.util.List;

/**
 * Created by GiangDinh on 02/02/2018.
 */

public class TownsAdapter extends BaseAdapter {
    private Context context;
    private List<Town> towns;
    private LayoutInflater layoutInflater;

    public TownsAdapter(Context context, List<Town> towns) {
        this.context = context;
        this.towns = towns;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return towns.size();
    }

    @Override
    public Object getItem(int i) {
        return towns.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TownHolder townHolder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_town, viewGroup, false);
            townHolder = new TownHolder(view);
            view.setTag(townHolder);
        } else {
            townHolder = (TownHolder) view.getTag();
        }
        Town town = towns.get(i);
        townHolder.tvTownName.setText(town.getName());
        if (i == 0) {
            townHolder.tvTownName.setTextColor(Color.GRAY);
        } else {
            townHolder.tvTownName.setTextColor(Color.BLACK);
        }
        return view;
    }

    public static class TownHolder {
        public TextView tvTownName;

        public TownHolder(View view) {
            tvTownName = view.findViewById(R.id.tvTownName);
        }
    }
}
