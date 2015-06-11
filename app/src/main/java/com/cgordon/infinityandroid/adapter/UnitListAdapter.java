package com.cgordon.infinityandroid.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitListAdapter extends RecyclerView.Adapter <UnitListAdapter.ViewHolder> {

    private static final String TAG = UnitListAdapter.class.getSimpleName();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.m_textView.setText("Test");
        holder.m_imageView.setImageResource(R.drawable.pano_main);

        holder.m_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, v.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;
        public CardView m_cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_view);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
