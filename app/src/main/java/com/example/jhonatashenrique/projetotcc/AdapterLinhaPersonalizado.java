package com.example.jhonatashenrique.projetotcc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class AdapterLinhaPersonalizado extends BaseAdapter{

    private final List<Linha> linhas;
    private final Activity act;

    public AdapterLinhaPersonalizado(List<Linha> linhas, Activity act) {
        this.linhas = linhas;
        this.act = act;
    }

    @Override
    public int getCount() {
        return linhas.size();
    }

    @Override
    public Object getItem(int position) {
        return linhas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return linhas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = act.getLayoutInflater().inflate(R.layout.adapter_view, parent, false);
       view.setSelected(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setContextClickable(true);
        }

        final Linha linha = linhas.get(position);
        //pegando as referências das Views
        TextView nome = (TextView)
                view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView descricao = (TextView)
                view.findViewById(R.id.lista_curso_personalizada_descricao);
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.lista_curso_personalizada_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openFile(linha.getNomeLinha()) == false) {
                    Intent i = new Intent(act, DownService.class);
                    i.putExtra("url", linha.getUrl());
                    i.putExtra("nomeLinha", linha.getNomeLinha());
                    act.startService(i);
                }
            }
        });
        //populando as Views
        nome.setText(linha.getNomeLinha());
        descricao.setText(linha.getDetalhes());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, MapsActivity.class);
                i.putExtra("linha",linha);
                act.startActivity(i);
            }
        });


        return view;
    }

    private boolean openFile(String name){
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, "" + name);
            if (file.exists()){
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(file), "application/pdf");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                act.startActivity(install);
            }else{
                return false;
            }


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
