package com.example.mastekapp.Admin;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mastekapp.R;
import com.example.mastekapp.Admin.RequestModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    Context context;
    List<RequestModel> requestList;

    public RequestAdapter(Context context, List<RequestModel> requestList) {

        this.context = context;
        this.requestList = requestList;
    }


    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_row, parent, false);

        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int position) {

        holder.tvname.setText(requestList.get(position).getName());
        holder.tvFlatNo.setText(requestList.get(position).getFlatNumber());
        Picasso.get().load(requestList.get(position).getImage().get(0)).into(holder.ivimage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RequestDetails.class);
                i.putExtra("id",requestList.get(position).getReqId());
                i.putExtra("name", requestList.get(position).getName());
                i.putExtra("email",requestList.get(position).getEmail());
                i.putExtra("flatNumber", requestList.get(position).getFlatNumber());
                i.putExtra("purposeOfVisit",requestList.get(position).getPurposeOfVisit());
                i.putExtra("referral",requestList.get(position).getReferral());
                i.putExtra("userCategory",requestList.get(position).getUserCategory());
                i.putExtra("image",requestList.get(position).getImage().get(0));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                changeIntent(i);
                context.startActivity(i);
            }
        });
    }

//    /* Interface for change availability, edit and delete menu items */
//    public interface OnProductClickListener {
//        void onAvailableItemClick(ProductModel productItem, int position);
//        void onEditItemClick(String itemName);
//        void onDeleteItemClick(String itemName);
//    }


//    public void changeIntent(Intent i){ context.startActivity(i); }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView tvname, tvFlatNo, tvqty;
        ImageView ivimage;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);


            tvname = itemView.findViewById(R.id.tvname);
            tvFlatNo = itemView.findViewById(R.id.tvFlatNo);
            ivimage = itemView.findViewById(R.id.ivimage);

//            btnAvailable.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onProductClickListener.onAvailableItemClick(productList.get(getAdapterPosition()), getAdapterPosition());
//                }
//            });
//
//            ivEditItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onMenuClickListener.onEditItemClick(productList.get(getAdapterPosition()).getName());
//                }
//            });
//
//            ivDeleteItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onMenuClickListener.onDeleteItemClick(productList.get(getAdapterPosition()).getName());
        }
    }
}


