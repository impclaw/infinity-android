package com.cgordon.infinityandroid.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitListFragment;

import java.util.List;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitListAdapter extends RecyclerView.Adapter <UnitListAdapter.ViewHolder> {

    private static final String TAG = UnitListAdapter.class.getSimpleName();
    private final Resources m_resources;
    private final Context m_context;

    private UnitListFragment.OnUnitSelectedListener m_listener;

    List<Unit> m_units;

    public UnitListAdapter(Context context, List<Unit> units ) {
        m_units = units;
        m_context = context;
        m_resources = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_unit_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.m_textView.setText(m_units.get(position).isc);
        holder.dbID = m_units.get(position).dbId;

        String resourceName = m_units.get(position).name;
        resourceName += "_48";

        resourceName = resourceName.toLowerCase().replace(" ", "_");

        int resourceId = m_resources.getIdentifier(resourceName, "drawable", m_context.getPackageName());
        holder.m_imageView.setImageResource(resourceId);



        holder.m_imageView.setImageResource(R.drawable.tohaa_sakiel_regiment_24);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_listener != null) {
                    m_listener.unitSelected(holder.dbID);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return m_units.size();
    }

    public void setOnUnitSelectedListener(UnitListFragment.OnUnitSelectedListener onUnitSelectedListener) {
        m_listener = onUnitSelectedListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;

        public long dbID;

        public ViewHolder(View itemView)
        {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_view);
        }


    }
}
