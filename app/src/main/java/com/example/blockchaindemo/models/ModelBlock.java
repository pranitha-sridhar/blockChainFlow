package com.example.blockchaindemo.models;

import com.example.blockchaindemo.managers.BlockChainManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ModelBlock {
    private  int index,nonce;
    private long timeStamp;
    private String hash;
    private String previousHash;
    private String data;

    public ModelBlock(int index, long timeStamp, String previousHash, String data) {
        this.index = index;
        this.timeStamp = timeStamp;
        this.previousHash = previousHash;
        this.data = data;
        nonce=0;
        hash=ModelBlock.calculateHash(this);
    }

    public static String calculateHash(ModelBlock modelBlock) {
        if(modelBlock!=null){
            MessageDigest messageDigest;
            try {
                messageDigest=MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
            String text=modelBlock.str();
            final byte[] bytes=messageDigest.digest(text.getBytes());
            final StringBuilder builder=new StringBuilder();
            for(final byte b:bytes){
                String hex=Integer.toHexString(0xff & b);
                if(hex.length()==1)builder.append('0');
                builder.append(hex);
            }
            return builder.toString();
        }
        return null;
    }

    private String str(){
        return index+timeStamp+previousHash+data+nonce;
    }

    public void mineBlock(int difficulty){
        nonce=0;
        while (!getHash().substring(0,difficulty).equals(addZeroes(difficulty))){
            nonce++;
            hash= ModelBlock.calculateHash(this);
        }
    }

    private String addZeroes(int difficulty) {
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<difficulty;i++)builder.append('0');
        return builder.toString();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
