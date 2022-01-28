package com.example.blockchaindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blockchaindemo.databinding.ActivityMainBinding;
import com.example.blockchaindemo.databinding.ContentMainBinding;
import com.example.blockchaindemo.fragment.PowFragment;
import com.example.blockchaindemo.managers.BlockChainManager;
import com.example.blockchaindemo.managers.SharedPreferancesManager;
import com.example.blockchaindemo.utils.CipherUtils;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity{
    ContentMainBinding contentMainBinding;
    ProgressDialog progressDialog;
    SharedPreferancesManager preferancesManager;
    BlockChainManager blockChainManager;
    boolean isEncryptionActivated;
    final String TAG_POW_DIALOG="proof_of_word_dialog";

    ImageButton send;
    RecyclerView recyclerView;
    TextInputEditText editText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferancesManager=new SharedPreferancesManager(this);
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        contentMainBinding=ContentMainBinding.bind(activityMainBinding.contentMain.getRoot());
        setContentView(R.layout.activity_main);

        send=findViewById(R.id.sendDataButton);
        recyclerView=findViewById(R.id.recyclerView);
        editText=findViewById(R.id.editData);
        progressBar=findViewById(R.id.progressBar);
        isEncryptionActivated=preferancesManager.getEncryptionStatus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        progressBar.setVisibility(View.VISIBLE);
        new Thread(()->runOnUiThread(()->{
            blockChainManager=new BlockChainManager(preferancesManager.getPowValue(),this);
            recyclerView.setAdapter(blockChainManager.blockAdapter);
            progressBar.setVisibility(View.GONE);
        })).start();



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "sendButton Triggered", Toast.LENGTH_SHORT).show();
                startBlockChain();
            }
        });
    }

    private void cancelProgressDialog(ProgressDialog progressDialog) {
        if(progressDialog!=null)progressDialog.cancel();
    }

    private void showProgressDialog(String loadingString) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(loadingString);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startBlockChain(){

        showProgressDialog(getResources().getString(R.string.text_mining_blocks));
        runOnUiThread(()->{
            if(blockChainManager!=null && editText.getText()!=null && recyclerView.getAdapter()!=null){
                progressBar.setVisibility(View.VISIBLE);
                String message=editText.getText().toString();
                if(!message.isEmpty()){
                    if(!isEncryptionActivated){
                        blockChainManager.addBlock(blockChainManager.newBlock(message));
                        if(blockChainManager.isBlockChainValid()){

                            recyclerView.setAdapter(blockChainManager.blockAdapter);
                            editText.setText("");
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(this, "BlockChain Corrupted", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        try {
                            blockChainManager.addBlock(blockChainManager.newBlock(CipherUtils.encryptIt(message).trim()));
                            if(blockChainManager.isBlockChainValid()){

                                recyclerView.setAdapter(blockChainManager.blockAdapter);
                                editText.setText("");
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(this, "BlockChain Corrupted", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    editText.requestFocus();
                    editText.setError("Message Field should not be empty");
                }
            }
            cancelProgressDialog(progressDialog);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkEncrypt=menu.findItem(R.id.encryption);
        checkEncrypt.setCheckable(isEncryptionActivated);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pow:
                PowFragment powFragment=PowFragment.newInstance();
                powFragment.show(this.getSupportFragmentManager(),TAG_POW_DIALOG);
                break;
            case R.id.encryption:
                isEncryptionActivated=!item.isChecked();
                item.setChecked(isEncryptionActivated);
                if(item.isChecked()) Toast.makeText(this, "Encryption is ON", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Encryption is OFF", Toast.LENGTH_SHORT).show();
                preferancesManager.setEncryptionStatus(isEncryptionActivated);
                return true;
            case R.id.exit:finish();
                            break;
            default:return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}