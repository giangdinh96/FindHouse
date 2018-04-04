package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.giangdinh.returnnotfound.findhouse.Adapter.ProvincesAdapter;
import com.giangdinh.returnnotfound.findhouse.Adapter.TownsAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by GiangDinh on 02/04/2018.
 */

public class FilterHouseForRentFragment extends Fragment implements IFilterHouseForRentView {
    private IFilterHouseForRentPresenter iFilterHouseForRentPresenter;

    private ArrayList<Province> provinces;
    private ArrayList<Town> towns;
    ProvincesAdapter provincesAdapter;
    TownsAdapter townsAdapter;

    @BindView(R.id.sProvinces)
    Spinner sProvinces;
    @BindView(R.id.sTowns)
    Spinner sTowns;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.crsPrice)
    CrystalRangeSeekbar crsPrice;
    @BindView(R.id.tvStretch)
    TextView tvStretch;
    @BindView(R.id.crsStretch)
    CrystalRangeSeekbar crsStretch;
    @BindView(R.id.tvPubDate)
    TextView tvPubDate;
    @BindView(R.id.csPubDate)
    CrystalSeekbar csPubDate;

    public static final String HAWK_PROVINCE = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_PROVINCE";
    public static final String HAWK_TOWN = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_TOWN";
    public static final String HAWK_MIN_START_PRICE = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_MIN_START_PRICE";
    public static final String HAWK_MAX_START_PRICE = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_MAX_START_PRICE";
    public static final String HAWK_MIN_START_STRETCH = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_MIN_START_STRETCH";
    public static final String HAWK_MAX_START_STRETCH = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_MAX_START_STRETCH";
    public static final String HAWK_MIN_START_PUBDATE = "com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.HAWK_MIN_START_PUBDATE";

    public static final int MIN_PRICE = 500000;
    public static final int MAX_PRICE = 15000000;
    public static final int MIN_STRETCH = 5;
    public static final int MAX_STRETCH = 100;
    public static final int MIN_PUBDATE = 1;
    public static final int MAX_PUBDATE = 30;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_house_for_rent, container, false);
        unbinder = ButterKnife.bind(this, view);
        iFilterHouseForRentPresenter = new FilterHouseForRentPresenter(this);
        iFilterHouseForRentPresenter.getProvinces();
        initViews();
        initEvents();
        return view;
    }

    private void initViews() {
        initSpinnerProvince();
        initSpinnerTown();
        initPrice();
        initStretch();
        initPubDate();
    }

    public void initSpinnerProvince() {
        provinces = new ArrayList<>();
        provinces.add(new Province("", "Tất cả", null));
        provincesAdapter = new ProvincesAdapter(getContext(), provinces);
        sProvinces.setAdapter(provincesAdapter);
    }

    public void initSpinnerTown() {
        towns = new ArrayList<>();
        towns.add(new Town("", "Tất cả"));
        townsAdapter = new TownsAdapter(getContext(), towns);
        sTowns.setAdapter(townsAdapter);
    }

    public void initPrice() {
        crsPrice.setDataType(CrystalRangeSeekbar.DataType.INTEGER);
        crsPrice.setCornerRadius(10);
        crsPrice.setSteps(MIN_PRICE);
        crsPrice.setMinValue(MIN_PRICE);
        crsPrice.setMaxValue(MAX_PRICE);

        // set minStartPrice
        if (Hawk.get(HAWK_MIN_START_PRICE) != null) {
            float temp = Float.parseFloat(String.valueOf(Hawk.get(HAWK_MIN_START_PRICE)));
            crsPrice.setMinStartValue(temp);
        } else {
            crsPrice.setMinStartValue(MIN_PRICE);
        }
        // set maxStartPrice
        if (Hawk.get(HAWK_MAX_START_PRICE) != null) {
            float temp = Float.parseFloat(String.valueOf(Hawk.get(HAWK_MAX_START_PRICE)));
            crsPrice.setMaxStartValue(temp);
        } else {
            crsPrice.setMaxStartValue(MAX_PRICE);
        }

        crsPrice.apply();
    }

    public void initStretch() {
        crsStretch.setDataType(CrystalRangeSeekbar.DataType.INTEGER);
        crsStretch.setCornerRadius(10);
        crsStretch.setSteps(MIN_STRETCH);
        crsStretch.setMinValue(MIN_STRETCH);
        crsStretch.setMaxValue(MAX_STRETCH);

        // set minStartStretch
        if (Hawk.get(HAWK_MIN_START_STRETCH) != null) {
            float temp = Float.parseFloat(String.valueOf(Hawk.get(HAWK_MIN_START_STRETCH)));
            crsStretch.setMinStartValue(temp);
        } else {
            crsStretch.setMinStartValue(MIN_STRETCH);
        }
        // set maxStartStretch
        if (Hawk.get(HAWK_MAX_START_STRETCH) != null) {
            float temp = Float.parseFloat(String.valueOf(Hawk.get(HAWK_MAX_START_STRETCH)));
            crsStretch.setMaxStartValue(temp);
        } else {
            crsStretch.setMaxStartValue(MAX_STRETCH);
        }

        crsStretch.apply();
    }

    public void initPubDate() {
        csPubDate.setDataType(CrystalRangeSeekbar.DataType.INTEGER);
        csPubDate.setCornerRadius(10);
        csPubDate.setSteps(MIN_PUBDATE);
        csPubDate.setMinValue(MIN_PUBDATE);
        csPubDate.setMaxValue(MAX_PUBDATE);

        // set minStartPubDate
        if (Hawk.get(HAWK_MIN_START_PUBDATE) != null) {
            float temp = Float.parseFloat(String.valueOf(Hawk.get(HAWK_MIN_START_PUBDATE)));
            csPubDate.setMinStartValue(temp);
        } else {
            csPubDate.setMinStartValue(MAX_PUBDATE);
        }

        csPubDate.apply();
    }

    @Override
    public void loadSpinnerProvinces(ArrayList<Province> provinces) {
        this.provinces.clear();
        this.provinces.add(new Province("", "Tất cả", null));
        this.provinces.addAll(provinces);
        provincesAdapter.notifyDataSetChanged();
        sProvinces.setSelection(0);
    }

    @Override
    public void loadSpinnerTowns(ArrayList<Town> towns) {
        this.towns.clear();
        this.towns.add(new Town("", "Tất cả"));
        if (towns != null)
            this.towns.addAll(towns);
        townsAdapter.notifyDataSetChanged();
        sTowns.setSelection(0);
    }

    @Override
    public void selectProvince(Province province) {
        if (provinces.indexOf(province) != -1)
            sProvinces.setSelection(provinces.indexOf(province));
    }

    @Override
    public void selectTown(Town town) {
        if (towns.indexOf(town) != -1)
            sTowns.setSelection(towns.indexOf(town));
    }

    @Override
    public void setPrice(String price) {
        tvPrice.setText(TextUtils.formatHtml("Giá phòng", price, "#757575", "#25B7D3", true));
    }

    @Override
    public void setStretch(String stretch) {
        tvStretch.setText(TextUtils.formatHtml("Diện tích", stretch, "#757575", "#25B7D3", true));
    }

    @Override
    public void setPubDate(String pubDate) {
        tvPubDate.setText(TextUtils.formatHtml("Thời gian", pubDate, "#757575", "#25B7D3", true));
    }


    private void initEvents() {
        sProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iFilterHouseForRentPresenter.handleProvinceSelected(provinces.get(i), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sTowns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iFilterHouseForRentPresenter.handleTownSelected(towns.get(i), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        crsPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Log.d("Test", "" + "crsPrice Change");
                iFilterHouseForRentPresenter.handlePriceChange(minValue, maxValue);
            }
        });

        crsPrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("Test", "" + "crsPrice Change Final");
                iFilterHouseForRentPresenter.handlePriceFinalChange(minValue, maxValue);
            }
        });

        crsStretch.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                iFilterHouseForRentPresenter.handleStretchChange(minValue, maxValue);
            }
        });

        crsStretch.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("Test", "" + "crsStretch Change Final");
                iFilterHouseForRentPresenter.handleStretchFinalChange(minValue, maxValue);
            }
        });

        csPubDate.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                iFilterHouseForRentPresenter.handlePubDateChange(value);
            }
        });

        csPubDate.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                Log.d("Test", "" + "csPubDate Change Final");
                iFilterHouseForRentPresenter.handlePubDateFinalChange(value);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
