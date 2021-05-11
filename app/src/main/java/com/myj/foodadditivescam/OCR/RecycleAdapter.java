package com.myj.foodadditivescam.OCR;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myj.foodadditivescam.R;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnInfoClickListener {
    private String[] localDataSet;  //받아온 데이터
    OnInfoClickListener listener;

    @Override
    public void onInfoClilck(ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onInfoClilck(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textInfo;   //설명
        public TextView textName;   //원재료 명
        public Button showBtn;      //보기 버튼

        public ViewHolder(View view, final OnInfoClickListener listener) {
            super(view);
            // Define click listener for the ViewHolder's View

            textInfo = (TextView) view.findViewById(R.id.info);
            textName = (TextView) view.findViewById(R.id.itemName);
            showBtn = (Button) view.findViewById(R.id.showBtn);

            showBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textInfo.getVisibility()==View.GONE){
                        textInfo.setVisibility(View.VISIBLE);
                    }else{
                        textInfo.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public RecyclerAdapter(String[] dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info, parent, false);

        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder thisHolder = (ViewHolder) holder;
        thisHolder.textName.setText(localDataSet[position]);
    }

    @Override
    public int getItemCount() {
        try{
            return localDataSet.length;
        }catch (Exception e){
            return 0;
        }
    }
}
