/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package com.nepalpolice.circle073.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalpolice.circle073.R;
import com.nepalpolice.circle073.model.helper.specialIConstant;
import com.nepalpolice.circle073.model.pojo.specialI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public class specialIAdapter extends RecyclerView.Adapter<specialIAdapter.Holder> {

    private static final String TAG = specialIAdapter.class.getSimpleName();
    private final FlowerClickListener mListener;
    private List<specialI> mFlowers;

    public specialIAdapter(FlowerClickListener listener) {
        mFlowers = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, null, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        specialI currFlower = mFlowers.get(position);

      holder.mName.setText(currFlower.getName());
        holder.mEmail.setText(currFlower.getCategory());
        holder.mLocation.setText(currFlower.getPrice());
        holder.mPhone.setText(currFlower.getInstructions());



        if (currFlower.isFromDatabase()) {
            holder.mPhoto.setImageBitmap(currFlower.getPicture());
        } else {
            Picasso.with(holder.itemView.getContext()).load(specialIConstant.HTTP.BASE_URL + "/photos/" + currFlower.getPhoto()).into(holder.mPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mFlowers.size();
    }

    public void addFlower(specialI flower) {
        mFlowers.add(flower);
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    public specialI getSelectedFlower(int position) {
        return mFlowers.get(position);
    }

    public void reset() {
        mFlowers.clear();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhoto;
        private TextView mName, mEmail,mLocation,mPhone;

        public Holder(View itemView) {
            super(itemView);
            mPhoto = (ImageView) itemView.findViewById(R.id.flowerPhoto);
            mName = (TextView) itemView.findViewById(R.id.flowerName);
            mEmail = (TextView) itemView.findViewById(R.id.contact_email);
            mLocation = (TextView) itemView.findViewById(R.id.contact_location);
            mPhone = (TextView) itemView.findViewById(R.id.contact_phone);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(getLayoutPosition());


        }
    }

    public interface FlowerClickListener {

        void onClick(int position);
    }
}
