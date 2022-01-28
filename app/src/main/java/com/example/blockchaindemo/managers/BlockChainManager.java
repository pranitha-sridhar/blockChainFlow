package com.example.blockchaindemo.managers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blockchaindemo.adapters.BlockAdapter;
import com.example.blockchaindemo.models.ModelBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockChainManager {
    private  int difficulty;
    private List<ModelBlock> list;
    public BlockAdapter blockAdapter;
    Context context;

    public BlockChainManager(int difficulty, @NonNull Context mcontext) {
        this.difficulty = difficulty;
        list=new ArrayList<>();
        ModelBlock block=new ModelBlock(0,System.currentTimeMillis(),null,"Genesis Block");
        block.mineBlock(difficulty);
        list.add(block);
        context=mcontext;
        blockAdapter=new BlockAdapter(list,context);
    }
    public ModelBlock newBlock(String data){
        ModelBlock latestBlock=lastestBlock();
        return new ModelBlock(latestBlock.getIndex()+1,System.currentTimeMillis(), latestBlock.getHash(), data);
    }
    private ModelBlock lastestBlock(){
        return list.get(list.size()-1);
    }

    public void addBlock(ModelBlock block){
        if(block!=null){
            block.mineBlock(difficulty);
            list.add(block);
            Toast.makeText(context, ""+blockAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
            blockAdapter.notifyDataSetChanged();
        }
    }
    private boolean isFirstBlockValid(){
        ModelBlock firstBlock= list.get(0);
        if(firstBlock.getIndex()!=0)return false;
        if(firstBlock.getPreviousHash()!=null)return  false;
        return firstBlock.getHash()!=null && ModelBlock.calculateHash(firstBlock).equals(firstBlock.getHash());
    }
    private boolean isValidNewBlock(@Nullable ModelBlock newBlock, @Nullable ModelBlock previousBlock){
        if(newBlock!=null && previousBlock!=null){
            if(previousBlock.getIndex()+1!= newBlock.getIndex())return false;
            if(newBlock.getPreviousHash()==null || !newBlock.getPreviousHash().equals(previousBlock.getHash())) return false;
            //if(newBlock.getPreviousHash()==null || !newBlock.getPreviousHash().equals(newBlock.getData()))return false;
            return newBlock.getHash()!=null && ModelBlock.calculateHash(newBlock).equals(newBlock.getHash());
        }
        return false;
    }
    public boolean isBlockChainValid(){
        if(!isFirstBlockValid())return false;
        for(int i=1;i< list.size();i++){
            if(!isValidNewBlock(list.get(i), list.get(i-1) ))return false;
        }
        return true;
    }
}
