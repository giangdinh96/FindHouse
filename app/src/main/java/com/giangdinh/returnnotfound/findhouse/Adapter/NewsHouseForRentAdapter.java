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
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail.HouseForRentDetailActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.InternetUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GiangDinh on 25/01/2018.
 */

public class NewsHouseForRentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<HouseForRent> items;
    private LayoutInflater layoutInflater;
    private RequestManager requestManager;

    public NewsHouseForRentAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.requestManager = Glide.with(context);
        this.items = new ArrayList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View houseNewFirstView = layoutInflater.inflate(R.layout.item_house_for_rent, parent, false);
        return new HouseForRentHolder(houseNewFirstView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // set house and houseNewFirstHolder
        final HouseForRent houseForRent = items.get(position);
        final HouseForRentHolder houseForRentHolder = (HouseForRentHolder) holder;

        Log.d("Test", "Binder--" + houseForRent);

        // set House Picture Loading
        requestManager.load(R.drawable.ic_loading).into(houseForRentHolder.ivLoading);
        houseForRentHolder.ivLoading.setVisibility(View.VISIBLE);
        houseForRentHolder.ivHousePicture.setImageBitmap(null);

        // set Price
        houseForRentHolder.tvPrice.setText(TextUtils.formatHtml("Giá phòng", TextUtils.formatPrice(houseForRent.getPrice()), "#000000"));
        // set Stretch
        houseForRentHolder.tvStretch.setText(TextUtils.formatHtml("Diện tích", TextUtils.formatStretch(houseForRent.getStretch()), "#000000"));
        // set PubDate
        houseForRentHolder.tvPubDate.setText(DateUtils.getDateTimeAgoString(-houseForRent.getPubDate()));

        // Get UserPicture and Username
        if (houseForRent.getUserPicture() != null && houseForRent.getUserName() != null) {
            houseForRentHolder.tvUserName.setText(houseForRent.getUserName());
            requestManager.load(houseForRent.getUserPicture()).asBitmap().centerCrop().into(new BitmapImageViewTarget(houseForRentHolder.ivUserPicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    houseForRentHolder.ivUserPicture.setImageDrawable(circularBitmapDrawable);
                }
            });

        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(houseForRent.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("name") != null) {
                            String userName = hashMapUser.get("name").toString();
                            houseForRentHolder.tvUserName.setText(userName);
                            houseForRent.setUserName(userName);
                        }
                        if (hashMapUser.get("photoUrl") != null) {
                            String userPicture = hashMapUser.get("photoUrl").toString();
                            requestManager.load(userPicture).asBitmap().centerCrop().into(new BitmapImageViewTarget(houseForRentHolder.ivUserPicture) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    houseForRentHolder.ivUserPicture.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            houseForRent.setUserPicture(userPicture);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Get Address
        if (houseForRent.getFullAddress() != null) {
            houseForRentHolder.tvFullAddress.setText(TextUtils.formatHtml("Địa chỉ", houseForRent.getFullAddress(), "#000000"));
        } else {
            FirebaseUtils.getDatabase().getReference().child("provinces").child(houseForRent.getAddress().getProvinceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null & dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshot.getValue();
                        String provinceName = hashMapProvince.get("name").toString();
                        HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
                        HashMap<String, Object> hashMapTown = (HashMap<String, Object>) hashMapTowns.get(houseForRent.getAddress().getTownId());
                        String townName = (String) hashMapTown.get("name");
                        String detail = houseForRent.getAddress().getDetail();
                        String fullAddress = String.format("%s, %s, %s", detail, townName, provinceName);
                        houseForRentHolder.tvFullAddress.setText(TextUtils.formatHtml("Địa chỉ", fullAddress, "#000000"));
                        houseForRent.setFullAddress(fullAddress);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Get First Picture
        if (houseForRent.getFirstPictureUrl() != null) {
            requestManager.load(houseForRent.getFirstPictureUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(houseForRentHolder.ivHousePicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    houseForRentHolder.ivLoading.setVisibility(View.GONE);
                }
            });
        } else if (!FirebaseUtils.isSignIn() && !InternetUtils.isNetworkConnected(context)) {
            // Check haven't Internet and haven't SignIn
        } else if (InternetUtils.isNetworkConnected(context)) {
            FirebaseStorage.getInstance().getReference().child(houseForRent.getPicturesList().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String firstPictureUrl = task.getResult().toString();
                    requestManager.load(firstPictureUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(houseForRentHolder.ivHousePicture) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            houseForRentHolder.ivLoading.setVisibility(View.GONE);
                        }
                    });
                    // up FirstPictureUrl to FirebaseDatabase
                    houseForRent.setFirstPictureUrl(firstPictureUrl);
                    FirebaseUtils.getDatabase().getReference().child("news/houseForRent").child(houseForRent.getId()).setValue(houseForRent);
                }
            });
        }

        // Set usersLike
        if (houseForRent.getUsersLike().size() <= 0) {
            houseForRentHolder.tvUsersLike.setVisibility(View.GONE);
            houseForRentHolder.vDivider2.setVisibility(View.GONE);
            houseForRentHolder.tvLike.setSelected(false);
        } else {
            if (FirebaseUtils.isSignIn() && houseForRent.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                houseForRentHolder.tvLike.setSelected(true);
            } else {
                houseForRentHolder.tvLike.setSelected(false);
            }
            houseForRentHolder.tvUsersLike.setText(houseForRent.getUsersLike().size() + " lượt thích");
            houseForRentHolder.tvUsersLike.setVisibility(View.VISIBLE);
            houseForRentHolder.vDivider2.setVisibility(View.VISIBLE);
        }

        // Set call click
        houseForRentHolder.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + houseForRent.getPhone()));
                context.startActivity(intent);
            }
        });

        // Set like click
        houseForRentHolder.tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseUtils.isSignIn()) {
                    if (houseForRent.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId()))
                        FirebaseUtils.getDatabase().getReference().child("news/houseForRent/" + houseForRent.getId() + "/usersLike/" + FirebaseUtils.getCurrentUserId())
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
                        FirebaseUtils.getDatabase().getReference().child("news/houseForRent/" + houseForRent.getId() + "/usersLike/" + FirebaseUtils.getCurrentUserId())
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
        houseForRentHolder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder textShare = new StringBuilder();
                textShare.append("Địa chỉ: ").append(houseForRent.getFullAddress()).append("\n")
                        .append("Giá phòng: ").append(TextUtils.formatPrice(houseForRent.getPrice())).append("\n")
                        .append("Diện tích: ").append(TextUtils.formatStretch(houseForRent.getStretch())).append("\n")
                        .append("Người đăng: ").append(houseForRent.getUserName()).append("\n")
                        .append("Email: ").append(houseForRent.getEmail()).append("\n")
                        .append("Điện thoại: ").append(houseForRent.getPhone()).append("\n");

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(textShare));
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        // Set house click
        houseForRentHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHouseForRentDetail = new Intent(context, HouseForRentDetailActivity.class);
                intentHouseForRentDetail.putExtra(VariableGlobal.EXTRA_HOUSE_FOR_RENT, houseForRent);
                context.startActivity(intentHouseForRentDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // method more
    public ArrayList<HouseForRent> getItems() {
        return items;
    }

    public void setItems(ArrayList<HouseForRent> items) {
        this.items = items;
    }

    public void addItemHouse(HouseForRent houseForRent) {
        items.add(houseForRent);
        notifyItemInserted(items.size() - 1);
    }

    public void addItemHouse(int position, HouseForRent houseForRent) {
        items.add(position, houseForRent);
        notifyItemInserted(position);
    }

    public void addAllItemHouse(int position, ArrayList<HouseForRent> houseForRents) {
        items.addAll(position, houseForRents);
        notifyItemRangeInserted(position, position + houseForRents.size());
    }

    public void addAllItemHouse(ArrayList<HouseForRent> houseForRents) {
        items.addAll(houseForRents);
        notifyItemRangeInserted(items.size() - 1, items.size() - 1 + houseForRents.size());
    }

    public void changeItemHouse(HouseForRent houseForRent) {
        int position = items.indexOf(houseForRent);
        if (position == -1) {
            return;
        }
        HouseForRent oldHouseForRent = items.get(position);
        oldHouseForRent.copyFrom(houseForRent);
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
    public class HouseForRentHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.ivHousePicture)
        public ImageView ivHousePicture;
        @BindView(R.id.ivLoading)
        public ImageView ivLoading;
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

        public HouseForRentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.e("Test", "Constructor");
        }
    }
}