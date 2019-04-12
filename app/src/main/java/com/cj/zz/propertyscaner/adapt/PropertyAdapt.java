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

import java.util.List;

public class PropertyAdapt  extends RecyclerView.Adapter<PropertyAdapt.PropertyHolder> {

    List<String> list;
    Context context;

    public PropertyAdapt(Context context) {
        this.context = context;
//        this.list = list;
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

        propertyHolder.view.setText("sd");
        propertyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = propertyHolder.getLayoutPosition();
                Toast.makeText(v.getContext(), "sd", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PropertyDetailActivity.class);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class PropertyHolder extends RecyclerView.ViewHolder {

        TextView view;

        public PropertyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tv_test);
        }
    }
}
