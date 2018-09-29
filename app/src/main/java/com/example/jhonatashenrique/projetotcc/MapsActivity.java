package com.example.jhonatashenrique.projetotcc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    Double latPoint;
    Double lngPoint;
    long connec = 10;
    long ok = 0;
    public static int overview;
    LatLng locali,minhaloc, latlng;
    public static GoogleMap mMap;
    ToggleButton toggle;
    TextView txtRota =null;
    WebView wv;
    Marker markerteste;
    String minhaloc2, locali2;
    ArrayList<String> lista = new ArrayList<>();
    FloatingActionButton btmain;
    Pontos pontos;
    Linha linha;
    PontosDAO daoPontos;
    ArrayList<Pontos> listPon;
    Coord coord;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Voltar");     //Titulo para ser exibido na sua Action Bar em frente à seta

        pb = (ProgressBar) findViewById(R.id.indeterminateBar);

        Intent intent = getIntent();
        linha = (Linha) intent.getSerializableExtra("linha");

        try{
            daoPontos = new PontosDAO();
            listPon = daoPontos.buscarTodasPontos(linha.getId());

        }catch (Exception e){
            e.printStackTrace();

        }

        txtRota = (TextView) findViewById(R.id.txtRota);
        btmain = (FloatingActionButton) findViewById(R.id.btmain);
        btmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                i.putExtra("lista", lista);
                startActivity(i);
            }
        });

        toggle = (ToggleButton) findViewById(R.id.route);
        toggle.setChecked(false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }

    //Criando o metodo para executar o chamado e as ações relacionada a Directions API em uma outro processo inBackground
    private class Tarefa extends AsyncTask<String, Integer, DirectionsResult>{

        @Override
        protected void onPreExecute(){
            Toast.makeText(MapsActivity.this,"Carregando Rota", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected DirectionsResult doInBackground(String... string) {
            DirectionsResult results = null;
            results = getDirectionsDetails(""+string[0],""+string[1],TravelMode.DRIVING);
            if (results != null) {
                Log.i("Diretions: ", "" + results.routes[overview].legs[overview].distance
                +" |lat: "+results.routes[overview].legs[overview].startLocation.lat);
            }
            return results;
        }
        @Override
        protected void onPostExecute(DirectionsResult result){
            if (result != null){
                addPolyline(result, mMap);
                addMarkersToMap(result, mMap);
                Log.i("AsyncTask", "Exibindo Thread: " + Thread.currentThread().getName());
            }else{
                Log.i("AsyncTask", "Tirando ProgressDialog da tela Thread: " + Thread.currentThread().getName());

            }
        }

    }
    //Metodo para a Directions API
    private void chamarAsyncTask(String eu, String buss){
        Tarefa exe = new Tarefa();
        Log.i("AsyncTask", "AsyncTask senado chamado Thread: " + Thread.currentThread().getName());
        exe.execute(new String []{eu, buss});
    }


    //Confiuranco o chamado para a Directions API
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);


    }

    //Faz a requisição para a Directions API
    private DirectionsResult getDirectionsDetails(String origin,String destination,TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .language("PT-BR")
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //recebe informações da Directions API e seta algumas variaveis
    private void addMarkersToMap(DirectionsResult results, GoogleMap googleMap) {
        mMap = googleMap;
        txtRota.setText(""+results.routes[overview].legs[overview].endAddress +" | "+getEndLocationTitle(results));
        txtRota.setVisibility(View.VISIBLE);
        ArrayList<String> list = new ArrayList<>();
        int j = results.routes[overview].legs[overview].steps.length;
        int f= j-1;

        for (int i=0; i<j;i++ ){
            if (i==0) {
                list.add("<html>" +
                        "<body>" +
                        "<h1>Rota</h1>" +
                        "<font size=5>" +
                        "<ul>" +
                        "<fieldset><li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions +"</fieldset>");
            }else if (i == f){
                list.add("<br><fieldset><li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions +"</fieldset>");
                list.add("<ul>" +
                        "</font>" +
                        "<body>" +
                        "<html>");
            }else{
                list.add("<br><fieldset><li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions +"</fieldset>");
            }
        }
        lista = list;
    }

    //Metodo que retorna as info da Diretions API
    private String getEndLocationTitle(DirectionsResult results){
        return  "Tempo: "+ results.routes[overview].legs[overview].duration.humanReadable
                + " Distancia: " + results.routes[overview].legs[overview].distance.humanReadable;
    }

    //Metodo para desenha as polyline
    private void addPolyline(DirectionsResult results, GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));

    }


    //Criando o mapa
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }

        //se a minha localizacao e a do objetivo for diferente de nulo ele chama a acao a Diretion API
        if (minhaloc != null && locali != null) {
            chamarAsyncTask(minhaloc2, locali2);
        }

        //Botao para rotas
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (minhaloc != null) {
                        btmain.setVisibility(View.VISIBLE);
                        GoogleMap mMap;
                        mMap = googleMap;
                        onMapReady(mMap);
                    } else {
                        Toast.makeText(MapsActivity.this, "Localização atual sendo encontrada", Toast.LENGTH_LONG).show();
                        toggle.setChecked(false);
                    }

                } else {
                    mMap.clear();
                    btmain.setVisibility(View.GONE);
                    txtRota.setVisibility(View.GONE);
                    locali = null;
                    onMapReady(mMap);
                }
            }
        });

        //Só atualiza a camera se nao tiver minha localizacao gps, basicamente ao iniciar o app
        if (minhaloc2 == null) {
            Pontos pontos = listPon.get(0);
            latlng = new LatLng(pontos.getLatitude(), pontos.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        try {
            for (Pontos pontos : listPon) {
                LatLng latLng = new LatLng(pontos.getLatitude(), pontos.getLongitude());
                final Marker marke = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("" + pontos.getNome())
                        .snippet("" + pontos.getDetalhesPonto())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
                marke.setTag(0);

            }
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception e2) {
            e2.printStackTrace();
        }

        final Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(-18.870544, -5.939916)).title("Onde estou?"));

        final Marker marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(-18.870544, -5.939916)).title("Onibus")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bussweb)));

        //Criando um Listner para o click dos marcadores
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        pb.setVisibility(View.VISIBLE);

            //Buscando e setando localizacao atual e do buss
            try {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        minhaloc = new LatLng(location.getLatitude(), location.getLongitude());
                        minhaloc2 = "" + location.getLatitude() + "," + location.getLongitude();
                        marker.setPosition(minhaloc);


                        if (connec == 10) {
                            CoordDAO dao = new CoordDAO();
                            coord = dao.buscarNseriePorId(1);
                            if (coord != null) {
                                pb.setVisibility(View.GONE);
                                LatLng latlng2 = new LatLng(coord.getCoordenadas(), coord.getSegundo());
                                marker2.setPosition(latlng2);
                            } else {
                                pb.setVisibility(View.GONE);
                                if (ok == 0) {
                                    Toast.makeText(MapsActivity.this, "Não foi possível encontrar a localização do Ônibus", Toast.LENGTH_SHORT).show();
                                    ok++;
                                    }
                                }
                            connec = 0;

                        }
                        connec++;
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            catch (SecurityException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            catch (NullPointerException e1){
                e1.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }


    //Metodo para o Listner dos marcadores
    public boolean onMarkerClick(final Marker marker3) {

        locali = marker3.getPosition();
        locali2 = ""+locali.latitude +","+locali.longitude;

        toggle.setVisibility(View.VISIBLE);
        toggle.setEnabled(true);

        return false;
    }

    //Metodo para capturar o toque na tela
    @Override
    public void onMapClick(LatLng latLng) {

    }

    //Pedindo permissão para acessar a localização atual
    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
            configurarServico();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //Pegando localizacao
    public void configurarServico() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {                }

                public void onProviderEnabled(String provider) {                }

                public void onProviderDisabled(String provider) {                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Atualizando localizacao
    public void atualizar(Location location) {
        latPoint = location.getLatitude();
        lngPoint = location.getLongitude();
    }

}
