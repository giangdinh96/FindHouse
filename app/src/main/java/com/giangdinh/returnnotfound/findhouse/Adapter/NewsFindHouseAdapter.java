package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail.HouseForRentDetailActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GiangDinh on 25/01/2018.
 */

public class NewsFindHouseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<FindHouse> items;
    private LayoutInflater layoutInflater;
    private RequestManager requestManager;

    public NewsFindHouseAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.requestManager = Glide.with(context);
        this.items = new ArrayList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View houseNewFirstView = layoutInflater.inflate(R.layout.item_find_house, parent, false);
        return new FindHouseHolder(houseNewFirstView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FindHouse findHouse = items.get(position);
        final FindHouseHolder findHouseHolder = (FindHouseHolder) holder;

        Log.d("Test", "Binder--" + findHouse);

        // set Price
        findHouseHolder.tvPrice.setText(TextUtils.formatHtml("Giá phòng", TextUtils.formatPrice(findHouse.getPrice()), "#000000"));
        // set Stretch
        findHouseHolder.tvStretch.setText(TextUtils.formatHtml("Diện tích", TextUtils.formatStretch(findHouse.getStretch()), "#000000"));
        // set PubDate
        findHouseHolder.tvPubDate.setText(DateUtils.getDateTimeAgoString(-findHouse.getPubDate()));

        // Get UserPicture and Username
        if (findHouse.getUserPicture() != null && findHouse.getUserName() != null) {
            findHouseHolder.tvUserName.setText(findHouse.getUserName());
            requestManager.load(findHouse.getUserPicture()).asBitmap().centerCrop().into(new BitmapImageViewTarget(findHouseHolder.ivUserPicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    findHouseHolder.ivUserPicture.setImageDrawable(circularBitmapDrawable);
                }
            });

        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(findHouse.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("name") != null) {
                            String userName = hashMapUser.get("name").toString();
                            findHouseHolder.tvUserName.setText(userName);
                            findHouse.setUserName(userName);
                        }
                        if (hashMapUser.get("photoUrl") != null) {
                            String userPicture = hashMapUser.get("photoUrl").toString();
                            requestManager.load(userPicture).asBitmap().centerCrop().into(new BitmapImageViewTarget(findHouseHolder.ivUserPicture) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    findHouseHolder.ivUserPicture.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            findHouse.setUserPicture(userPicture);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Get Address
        if (findHouse.getFullAddress() != null) {
            findHouseHolder.tvFullAddress.setText(TextUtils.formatHtml("Địa chỉ", findHouse.getFullAddress(), "#000000"));
        } else {
            FirebaseUtils.getDatabase().getReference().child("provinces").child(findHouse.getAddress().getProvinceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null & dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshot.getValue();
                        String provinceName = hashMapProvince.get("name").toString();
                        HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
                        HashMap<String, Object> hashMapTown = (HashMap<String, Object>) hashMapTowns.get(findHouse.getAddress().getTownId());
                        String townName = (String) hashMapTown.get("name");
                        String detail = findHouse.getAddress().getDetail();
                        String fullAddress = String.format("%s, %s, %s", detail, townName, provinceName);
                        findHouseHolder.tvFullAddress.setText(TextUtils.formatHtml("Địa chỉ", fullAddress, "#000000"));
                        findHouse.setFullAddress(fullAddress);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Set usersLike
        if (findHouse.getUsersLike().size() <= 0) {
            findHouseHolder.tvUsersLike.setVisibility(View.GONE);
            findHouseHolder.vDivider2.setVisibility(View.GONE);
            findHouseHolder.tvLike.setSelected(false);
        } else {
            if (FirebaseUtils.isSignIn() && findHouse.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                findHouseHolder.tvLike.setSelected(true);
            } else {
                findHouseHolder.tvLike.setSelected(false);
            }
            findHouseHolder.tvUsersLike.setText(findHouse.getUsersLike().size() + " lượt thích");
            findHouseHolder.tvUsersLike.setVisibility(View.VISIBLE);
            findHouseHolder.vDivider2.setVisibility(View.VISIBLE);
        }

        // Set call click
        findHouseHolder.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + findHouse.getPhone()));
                context.startActivity(intent);
            }
        });

        // Set like click
        findHouseHolder.tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseUtils.isSignIn()) {
                    if (findHouse.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId()))
                        FirebaseUtils.getDatabase().getReference().child("news/findHouse/" + findHouse.getId() + "/usersLike/" + FirebaseUtils.getCurrentUserId())
                                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(context, "Error UnLike", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    else {
                        FirebaseUtils.getDatabase().getReference().child("news/findHouse/" + findHouse.getId() + "/usersLike/" + FirebaseUtils.getCurrentUserId())
                                .setValue(FirebaseUtils.getCurrentUserId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(context, "Error Like", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "Bạn cần đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set share click
        findHouseHolder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder textShare = new StringBuilder();
                textShare.append("Địa chỉ: ").append(findHouse.getFullAddress()).append("\n")
                        .append("Giá phòng: ").append(TextUtils.formatPrice(findHouse.getPrice())).append("\n")
                        .append("Diện tích: ").append(TextUtils.formatStretch(findHouse.getStretch())).append("\n")
                        .append("Người đăng: ").append(findHouse.getUserName()).append("\n")
                        .append("Email: ").append(findHouse.getEmail()).append("\n")
                        .append("Điện thoại: ").append(findHouse.getPhone()).append("\n");

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(textShare));
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        // Set house click
        findHouseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentHouseForRentDetail = new Intent(context, HouseForRentDetailActivity.class);
//                intentHouseForRentDetail.putExtra(VariableGlobal.EXTRA_HOUSE_FOR_RENT, findHouse);
//                context.startActivity(intentHouseForRentDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // method more
    public ArrayList<FindHouse> getItems() {
        return items;
    }

    public void setItems(ArrayList<FindHouse> items) {
        this.items = items;
    }

    public void addItemHouse(FindHouse findHouse) {
        items.add(findHouse);
        notifyItemInserted(items.size() - 1);
    }

    public void addItemHouse(int position, FindHouse findHouse) {
        items.add(position, findHouse);
        notifyItemInserted(position);
    }

    public void addAllItemHouse(int position, ArrayList<FindHouse> findHouses) {
        items.addAll(position, findHouses);
        notifyItemRangeInserted(position, position + findHouses.size());
    }

    public void addAllItemHouse(ArrayList<FindHouse> findHouses) {
        items.addAll(findHouses);
        notifyItemRangeInserted(items.size() - 1, items.size() - 1 + findHouses.size());
    }

    public void changeItemHouse(FindHouse findHouse) {
        int position = items.indexOf(findHouse);
        if (position == -1) {
            return;
        }
        FindHouse oldFindHouse = items.get(position);
        oldFindHouse.copyFrom(findHouse);
        notifyItemChanged(position);
    }

    public void removeItemHouse(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItemHouse() {
        items.clear();
        notifyDataSetChanged();
    }

    // Holder
    public class FindHouseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivUserPicture)
        public ImageView ivUserPicture;
        @BindView(R.id.tvUserName)
        public TextView tvUserName;
        @BindView(R.id.tvPubDate)
        public TextView tvPubDate;
        @BindView(R.id.tvFullAddress)
        public TextView tvFullAddress;
        @BindView(R.id.tvPrice)
        public TextView tvPrice;
        @BindView(R.id.tvStretch)
        public TextView tvStretch;
        @BindView(R.id.tvUsersLike)
        public TextView tvUsersLike;
        @BindView(R.id.vDivider2)
        public View vDivider2;
        @BindView(R.id.tvCall)
        public TextView tvCall;
        @BindView(R.id.tvLike)
        public TextView tvLike;
        @BindView(R.id.tvShare)
        public TextView tvShare;

        public FindHouseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}