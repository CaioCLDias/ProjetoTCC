package com.example.jhonatashenrique.projetotcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txthorario;
    ImageView img;
    FloatingActionButton fab;

    ArrayList<String> listatoedit = new ArrayList<>();
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        wv = (WebView) findViewById(R.id.wb);

        Intent intent = getIntent();
        listatoedit = (ArrayList<String>) intent.getSerializableExtra("lista");
        if (listatoedit != null){
            wv.setVisibility(View.VISIBLE);

            WebSettings ws = wv.getSettings();
            ws.setJavaScriptEnabled(true);
            ws.setSupportZoom(true);
            wv.loadData(listatoedit.toString(), "text/html", "UTF-8");

            fab.setEnabled(false);

        }

        img = (ImageView) findViewById(R.id.imgEasier);

        txthorario = (TextView) findViewById(R.id.txthorario);
        txthorario.setText("Horarios dos Onibus de Assis e Candido Mota - Dias Uteis" +
                "\nAssis x CM - 6:00  | CM x Assis - 6:00" +
                "\nAssis x CM - 6:30  | CM x Assis - 6:30" +
                "\nAssis x CM - 7:00  | CM x Assis - 7:00" +
                "\nAssis x CM - 7:30  | CM x Assis - 7:30" +
                "\nAssis x CM - 8:00  | CM x Assis - 8:00" +
                "\nAssis x CM - 8:40  | CM x Assis - 8:40" +
                "\nAssis x CM - 9:20  | CM x Assis - 9:20" +
                "\nAssis x CM - 10:00 | CM x Assis - 10:00" +
                "\nAssis x CM - 10:40 | CM x Assis - 10:40" +
                "\nAssis x CM - 11:20 | CM x Assis - 11:20" +
                "\nAssis x CM - 12:00 | CM x Assis - 12:00" +
                "\nAssis x CM - 12:40 | CM x Assis - 12:40" +
                "\nAssis x CM - 13:20 | CM x Assis - 13:20" +
                "\nAssis x CM - 14:00 | CM x Assis - 14:00" +
                "\nAssis x CM - 14:40 | CM x Assis - 14:40" +
                "\nAssis x CM - 15:20 | CM x Assis - 15:20" +
                "\nAssis x CM - 16:00 | CM x Assis - 16:00" +
                "\nAssis x CM - 16:40 | CM x Assis - 16:40" +
                "\nAssis x CM - 17:20 | CM x Assis - 17:20" +
                "\nAssis x CM - 18:00 | CM x Assis - 18:00" +
                "\nAssis x CM - 18:40 | CM x Assis - 18:40" +
                "\nAssis x CM - 19:20 | CM x Assis - 19:20" +
                "\nAssis x CM - 20:00 | CM x Assis - 20:40" +
                "\nAssis x CM - 21:20 | CM x Assis - 22:00" +
                "\nAssis x CM - 23:00 | CM x Assis - 23:30" +
                "\nAssis x CM - 00:00 | ------------------");


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
                img.setVisibility(View.INVISIBLE);
                if (listatoedit != null)
                wv.setVisibility(View.INVISIBLE);

                txthorario.setVisibility(View.VISIBLE);
                txthorario.setEnabled(true);
                auxbthorario ++;
            }else{
                img.setVisibility(View.VISIBLE);
                if (listatoedit != null)
                wv.setVisibility(View.VISIBLE);

                txthorario.setVisibility(View.GONE);
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
}
