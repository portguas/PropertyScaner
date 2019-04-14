package com.cj.zz.propertyscaner.adapt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cj.zz.propertyscaner.R;
import com.cj.zz.propertyscaner.model.HistoryData;
import com.cj.zz.propertyscaner.model.NewPropertyData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    List<HistoryData> data;
    Context context;

    public HistoryAdapter(Context context, List<HistoryData> list) {
        this.context = context;
        this.data = list;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup,false);
        HistoryHolder holder = new HistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryHolder propertyHolder, int i) {
        HistoryData curData = this.data.get(i);
        propertyHolder.beginTime.setText(curData.beginTime);
        propertyHolder.endTime.setText("");
        propertyHolder.scanNumber.setText(curData.scanNumber);
        propertyHolder.isFinished.setText(curData.isFinished ? "已经结束" : "盘点中");
        propertyHolder.operation.setText(curData.isFinished ? "查看结果" : "继续盘点");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        TextView beginTime;
        TextView endTime;
        TextView scanNumber;
        TextView isFinished;
        TextView operation;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            beginTime = itemView.findViewById(R.id.history_beginTime);
            endTime = itemView.findViewById(R.id.history_endTime);
            scanNumber = itemView.findViewById(R.id.scanNumber);
            isFinished = itemView.findViewById(R.id.history_status);
            operation = itemView.findViewById(R.id.history_Operation);
        }
    }
}
