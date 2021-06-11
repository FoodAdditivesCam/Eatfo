package com.myj.foodadditivescam.result;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myj.foodadditivescam.R;

import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnInfoClickListener {
    private List<String> NameSet;  //받아온 데이터
    private List<String> TagSet;  //받아온 데이터
    private List<String> InfoSet;  //받아온 데이터
    OnInfoClickListener listener;

    @Override
    public void onInfoClilck(ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onInfoClilck(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;   //원재료 명
        public TextView textTag;        //태그
        public TextView textInfo;   //설명
        public Button showBtn;      //보기 버튼

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textName = (TextView) view.findViewById(R.id.itemName);
            textTag = (TextView) view.findViewById(R.id.tag);
            textInfo = (TextView) view.findViewById(R.id.info);
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

    public RecyclerAdapter(List<String> nameSet, List<String> tagset, List<String> infoSet){
        this.NameSet = nameSet;
        this.TagSet = tagset;
        this.InfoSet = infoSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.info, parent, false) ;
        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder thisHolder = (ViewHolder) holder;
        thisHolder.textName.setText(NameSet.get(position));
        thisHolder.textTag.setText(TagSet.get(position));
        thisHolder.textInfo.setText(InfoSet.get(position));
    }

    @Override
    public int getItemCount() {
        try{
            return NameSet.size();
        }catch (Exception e){
            return 0;
        }
    }
}
