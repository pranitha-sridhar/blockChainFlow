package com.example.blockchaindemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchaindemo.R;
import com.example.blockchaindemo.models.ModelBlock;

import java.util.Date;
import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.RecyclerViewHolder> {
    private List<ModelBlock> list;
    private Context context;
    private int lastPosition=-1;

    public BlockAdapter(@Nullable List<ModelBlock> list, @Nullable Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ModelBlock block=list.get(position);
        holder.index.setText(String.format(context.getString(R.string.title_block_number),block.getIndex()));
        holder.previousHash.setText(block.getPreviousHash()!=null?block.getPreviousHash():"Null");
        holder.timeStamp.setText(String.valueOf(new Date(block.getTimeStamp())));
        holder.data.setText(block.getData());
        holder.hash.setText(block.getHash());
        setAnimation(holder.itemView,position);
    }

    private void setAnimation(View itemView, int position) {
        if(position>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
            itemView.startAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0: list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_block;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView index,previousHash,timeStamp,data,hash;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            index=itemView.findViewById(R.id.blockId);
            previousHash=itemView.findViewById(R.id.previousHash);
            timeStamp=itemView.findViewById(R.id.timeStamp);
            data=itemView.findViewById(R.id.data);
            hash=itemView.findViewById(R.id.hash);
        }
    }
}
