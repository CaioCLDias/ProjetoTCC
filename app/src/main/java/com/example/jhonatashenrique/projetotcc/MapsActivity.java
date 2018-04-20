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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    Double latPoint;
    Double lngPoint;
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

    private static final LatLng ponto1 = new LatLng(-22.743136, -50.388386);
    private static final LatLng ponto2 = new LatLng(-22.745568, -50.389934);
    private static final LatLng ponto3 = new LatLng(-22.747181, -50.387345);
    private static final LatLng ponto4 = new LatLng(-22.750274, -50.382367);
    private static final LatLng ponto5 = new LatLng(-22.754154, -50.376184);
    private static final LatLng ponto6 = new LatLng(-22.752909, -50.374630);
    private static final LatLng ponto7 = new LatLng(-22.751183, -50.377419);
    private static final LatLng ponto8 = new LatLng(-22.747253, -50.382051);
    private static final LatLng ponto9 = new LatLng(-22.743232, -50.386774);
    private static final LatLng ponto10 = new LatLng(-22.735991, -50.390014);
    private static final LatLng ponto11 = new LatLng(-22.745568, -50.389934);
    private static final LatLng ponto12 = new LatLng(-22.730952, -50.390224);

    private Marker mPonto1;
    private Marker mPonto2;
    private Marker mPonto3;
    private Marker mPonto4;
    private Marker mPonto5;
    private Marker mPonto6;
    private Marker mPonto7;
    private Marker mPonto8;
    private Marker mPonto9;
    private Marker mPonto10;
    private Marker mPonto11;
    private Marker mPonto12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
                list.add("<!DOCTYPE html>" +
                        "<html>" +
                        "<body>" +
                        "<h1>Rota</h1>" +
                        "<font size=5>" +
                        "<ul>" +
                        "<li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions);
            }else if (i == f){
                list.add("<br><li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions);
                list.add("<ul>" +
                        "</font>" +
                        "<body>" +
                        "<html>");
            }else{
                list.add("<br><li>" + results.routes[overview].legs[overview].steps[i].htmlInstructions);
            }
        }
        Log.i("Info",""+list);

        lista = list;

        //mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].startLocation.lat,
        // results.routes[overview].legs[overview].startLocation.lng)).title(results.routes[overview].legs[overview].startAddress));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].endLocation.lat,
        // results.routes[overview].legs[overview].endLocation.lng)).title(results.routes[overview].legs[overview].startAddress)
        // .snippet(getEndLocationTitle(results)).icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
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
                    }else{
                        Toast.makeText(MapsActivity.this,"Localização atual sendo encontrada", Toast.LENGTH_LONG).show();
                        toggle.setChecked(false);
                    }

                } else {
                    mMap.clear();
                    btmain.setVisibility(View.GONE);
                    txtRota.setVisibility(View.GONE);
                    locali=null;
                    onMapReady(mMap);
                }
            }
        });

        //Só atualiza a camera se nao tiver minha localizacao gps, basicamente ao iniciar o app
        if (minhaloc2 == null){
            latlng = new LatLng(-22.743136, -50.388386);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Adicionando marcadores e um objeto para cada um
        mPonto1 = mMap.addMarker(new MarkerOptions()
                .position(ponto1)
                .title("Ponto 1")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto1.setTag(0);

        mPonto2 = mMap.addMarker(new MarkerOptions()
                .position(ponto2)
                    .title("Ponto 2")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto2.setTag(0);

        mPonto3 = mMap.addMarker(new MarkerOptions()
                .position(ponto3)
                .title("Ponto 3")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto3.setTag(0);

        mPonto4 = mMap.addMarker(new MarkerOptions()
                .position(ponto4)
                .title("Ponto 4")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto4.setTag(0);

        mPonto5 = mMap.addMarker(new MarkerOptions()
                .position(ponto5)
                .title("Ponto 5")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto5.setTag(0);

        mPonto6 = mMap.addMarker(new MarkerOptions()
                .position(ponto6)
                .title("Ponto 6")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto6.setTag(0);

        mPonto7 = mMap.addMarker(new MarkerOptions()
                .position(ponto7)
                .title("Ponto 7")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto7.setTag(0);

        mPonto8 = mMap.addMarker(new MarkerOptions()
                .position(ponto8)
                .title("Ponto 8")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto8.setTag(0);

        mPonto9 = mMap.addMarker(new MarkerOptions()
                .position(ponto9)
                .title("Ponto 9")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto9.setTag(0);

        mPonto10 = mMap.addMarker(new MarkerOptions()
                .position(ponto10)
                .title("Ponto 10")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto10.setTag(0);

        mPonto11 = mMap.addMarker(new MarkerOptions()
                .position(ponto11)
                .title("Ponto 11")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto11.setTag(0);

        mPonto12 = mMap.addMarker(new MarkerOptions()
                .position(ponto12)
                .title("Ponto 12")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.banco)));
        mPonto12.setTag(0);

        final Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title("Onde estou?"));

        final Marker marker2 = mMap.addMarker(new MarkerOptions().position(latlng).title("Onibus posicionado")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bussweb)));

        //Criando um Listner para o click dos marcadores
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        //Buscando e setando localizacao atual e do buss
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    minhaloc = new LatLng(location.getLatitude(), location.getLongitude());
                    minhaloc2 = ""+location.getLatitude() +","+location.getLongitude();
                    marker.setPosition(minhaloc);
                    CoordDAO dao = new CoordDAO();
                     Coord coord = dao.buscarNseriePorId(1);
//                    Log.i("Teste: ", "" + coord.getCoordenadas() + ", " + coord.getSegundo());
                     LatLng latlng2 = new LatLng(coord.getCoordenadas(), coord.getSegundo());
                     marker2.setPosition(latlng2);

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
