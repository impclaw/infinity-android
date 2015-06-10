package com.cgordon.infinityandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Sectorial;
import com.cgordon.infinityandroid.storage.SectorialData;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cgordon on 5/25/2015.
 */
public class ArmyAdapter extends RecyclerView.Adapter <ArmyAdapter.ViewHolder> {

    private final static String TAG = ArmyAdapter.class.getSimpleName();

    ArrayList<Sectorial> m_sectorials;
    Context m_context;

    public int[][] m_data = {
            {R.string.pano_main, R.drawable.pano_main, R.color.pano},
            {R.string.yujing_main, R.drawable.yujing_main, R.color.yujing},
            {R.string.ariadna_main, R.drawable.ariadna_main, R.color.ariadna},
            {R.string.haqqislam_main, R.drawable.haqqislam_main, R.color.haqqislam},
            {R.string.nomads_main, R.drawable.nomads_main, R.color.nomads},
            {R.string.ca_main, R.drawable.ca_main, R.color.ca},
            {R.string.aleph_main, R.drawable.aleph_main, R.color.aleph},
            {R.string.tohaa_main, R.drawable.tohaa_main, R.color.tohaa},
            {R.string.merc_main, R.drawable.merc_main, R.color.mercs}
    };

    public ArmyAdapter(Context context) {
        Log.d(TAG, "ArmyAdapter Constructor");

        m_context = context;

        SectorialData sectorialData = new SectorialData(m_context);
        Date start = new Date();
        sectorialData.open();
        m_sectorials = sectorialData.getAllSectorials();
        if (m_sectorials == null) {
            Log.e(TAG, "No sectorials!  Very bad");
        }
        sectorialData.close();
        Date end = new Date();
        Log.d(TAG, "ArmyAdapter load data time: " + (end.getTime() - start.getTime()) + " ms");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_army, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < m_data.length) {
            holder.m_textView.setText(m_data[position][0]);
            holder.m_imageView.setImageResource(m_data[position][1]);
        } else {
            holder.m_textView.setText(m_sectorials.get(position-m_data.length).name);
            holder.m_imageView.setImageResource(R.drawable.pano_main);
        }

        holder.m_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(m_context, holder.m_textView.getText().toString(), Toast.LENGTH_SHORT).show();

//                ((Activity) v.getContext()).findViewById(R.id.toolbar).setBackgroundResource(m_data[position][2]);
            }
        });
    }


    @Override
    public int getItemCount() {
        return m_data.length + m_sectorials.size();
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
