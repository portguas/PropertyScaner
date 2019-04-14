package com.cj.zz.propertyscaner.adapt;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.zz.propertyscaner.HistoryActivity;
import com.cj.zz.propertyscaner.PropertyDetailActivity;
import com.cj.zz.propertyscaner.R;
import com.cj.zz.propertyscaner.model.NewPropertyData;

import java.util.List;

public class PropertyAdapt  extends RecyclerView.Adapter<PropertyAdapt.PropertyHolder> {

    List<NewPropertyData> list;
    Context context;

    public PropertyAdapt(Context context, List<NewPropertyData> list) {
        this.context = context;
        this.list = list;
    }

    public void addData(NewPropertyData newPropertyCode) {
        this.list.add(0, newPropertyCode);
        notifyItemInserted(0);
    }

    public void setData(List<NewPropertyData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PropertyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_item, viewGroup,false);
        PropertyHolder holder = new PropertyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PropertyHolder propertyHolder, int i) {

        final NewPropertyData data = this.list.get(i);
        propertyHolder.view.setText(data.getKey());
        propertyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = propertyHolder.getLayoutPosition();
//                Toast.makeText(v.getContext(), "sd" + pos, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PropertyDetailActivity.class);
                intent.putExtra("detail", data);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class PropertyHolder extends RecyclerView.ViewHolder {

        TextView view;

        public PropertyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tv_test);
        }
    }
}
