package com.example.jhonatashenrique.projetotcc;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class DownService extends IntentService {

    String URL;
    String nomeLinha;
    DownloadManager downloadManager;

    public DownService(){
        super ("DownService");
    }

    public DownService(String name) {
        super(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        URL = (String) intent.getSerializableExtra("url");
        nomeLinha = (String) intent.getSerializableExtra("nomeLinha");

        iniciarDownload(URL, nomeLinha);
        Log.i("teste","Após handle iniciardown");
        openFile(nomeLinha);

    }

    //BroadcastReceiver que será invocado ao terminar o download
    final BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction() )){
                openFile(nomeLinha);
                Log.i("teste","Passou pelo OnReceive");
            }
        }
    };



    @Override
    public void onDestroy() {
        unregisterReceiver(onComplete);
        super.onDestroy();
    }
    /*
     *Abre o arquivo que realizamos o download
     */
    private void openFile(String name){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, ""+name);

        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(file), "application/pdf");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(install);

    }

    public void iniciarDownload(String url, String name){
        Uri uri = Uri.parse(""+url);

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        downloadManager.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downlodad")
                .setDescription("Realizando o download.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        ""+name));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Baixando [...]", Toast.LENGTH_SHORT).show();
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        // registramos nosso BroadcastReceiver
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return super.onStartCommand(intent, flags, startId);
    }

}
