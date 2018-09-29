package com.example.jhonatashenrique.projetotcc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txthorario;
    ListView listalinha;
    FloatingActionButton fab;
    ProgressBar indeterminateProgress;
    ArrayList<String> listatoedit = new ArrayList<>();
    WebView wv;
    WebView wvhorarios;
    Linha linha;
    LinhaDAO daoLinha;
    List<Linha> listLin;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission");
            } else {

                Log.e("Permission error", "You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        indeterminateProgress = (ProgressBar) findViewById(R.id.indeterminateProgress2);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        listalinha = (ListView) findViewById(R.id.listaLinhas);
        listalinha.setClickable(true);

        wv = (WebView) findViewById(R.id.wb);
        wvhorarios = (WebView) findViewById(R.id.wvhorarios);

        Intent intent = getIntent();
        listatoedit = (ArrayList<String>) intent.getSerializableExtra("lista");

        if (listatoedit != null){
            String hor="";
            String aux="";
            for (String cada: listatoedit) {
                hor = ""+aux +""+cada;
                aux = hor;
            }
            wv.setVisibility(View.VISIBLE);

            WebSettings ws = wv.getSettings();
            ws.setJavaScriptEnabled(true);
            ws.setSupportZoom(true);
            wv.loadData(hor, "text/html", "UTF-8");

            fab.setEnabled(false);

        }

        listalinha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                linha = (Linha) adapter.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra("linha",linha);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        MapsActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        indeterminateProgress.setVisibility(View.VISIBLE);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                carregarLinhas();
            }
        }, 2500);

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Variavel auxiliar
    Integer auxbthorario =0;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            if(auxbthorario == 0){

                if (listatoedit != null)
                wv.setVisibility(View.INVISIBLE);

                wvhorarios.setVisibility(View.VISIBLE);
                auxbthorario ++;
            }else{

                if (listatoedit != null)
                wv.setVisibility(View.VISIBLE);

                wvhorarios.setVisibility(View.GONE);
                auxbthorario = 0;
            }

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        indeterminateProgress.setVisibility(View.VISIBLE);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                carregarLinhas();
            }
        }, 2500);
        super.onResume();
    }

    public void carregarLinhas(){
        daoLinha = new LinhaDAO ();
        listLin = daoLinha.buscarTodasLinha();
        if (listLin != null){
            AdapterLinhaPersonalizado adapter =
                    new AdapterLinhaPersonalizado(listLin, this);
            listalinha.setAdapter(adapter);

            indeterminateProgress.setVisibility(View.GONE);
        }else{
            Toast.makeText(MainActivity.this, "Não foi possível listar as Linhas!", Toast.LENGTH_SHORT).show();
            indeterminateProgress.setVisibility(View.GONE);
        }
    }

}
