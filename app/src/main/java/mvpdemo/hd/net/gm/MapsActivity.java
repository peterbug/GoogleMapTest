package mvpdemo.hd.net.gm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("XXX", this.getClass().getSimpleName() + " onConnectionFailed: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("XXX", this.getClass().getSimpleName() + " onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("XXX", this.getClass().getSimpleName() + " onConnectionSuspended: " + i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        setUpMap();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // 1
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("XXX", this.getClass().getSimpleName() + " onResume: "
                + GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        sydney = new LatLng(31.22445747329499, 121.47586964070796);
        mMap.addMarker(new MarkerOptions().position(sydney).title("力宝广场"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
//        getLatiAndLongiTude();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("XXX", this.getClass().getSimpleName() + " onMapClick: " + latLng);
//                mMap.addMarker(new MarkerOptions().position(latLng).title("力宝广场:" + latLng));
                HotelRequest.testLocal(getApplicationContext(), latLng.latitude + "," + latLng.longitude, hotelCallBack);
//                HotelRequest.fetchHotelByGoogle(getApplicationContext());
            }
        });
//        googleMap.setIndoorEnabled(false);
        getLatiAndLongiTude();
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                                               @Override
                                               public void onMyLocationChange(Location location) {
                                                   Log.e("XXX", this.getClass().getSimpleName() + " onMyLocationChange: " + location);
                                               }
                                           }
        );
//        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
//            @Override
//            public void onPoiClick(PointOfInterest pointOfInterest) {
//                Log.e("XXX", this.getClass().getSimpleName() + " onPoiClick: " + pointOfInterest);
//            }
//        });

//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.style_json));
//
//            if (!success) {
//                Log.e("XXX", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("XXX", "Can't find style. Error: ", e);
//        }
        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.WRITE_CALL_LOG}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
    }


    private GoogleApiClient mGoogleApiClient;

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("XXX", this.getClass().getSimpleName() + " onMarkerClick: ");
//        HotelRequest.fetchHotelByGoogle();
        return false;
    }

    private Location getLatiAndLongiTude() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;
    }

    Callback hotelCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("XXX", this.getClass().getSimpleName() + " testLocal onFailure: call:" + call + " " + e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String result = response.body().string();
//            Log.e("XXX", this.getClass().getSimpleName() + " testLocal onResponse: call:" + call);
//            Log.e("XXX", this.getClass().getSimpleName() + " testLocal onResponse: response:" + result);
//            HotelBean bean = HotelBean.parse(result);
//            Log.e("XXX", this.getClass().getSimpleName() + " onResponse: " + bean);

            HotelBean bean2 = HotelBean.parse(result, "lodging");
            Log.e("XXX", this.getClass().getSimpleName() + " onResponse: filter:" + bean2);
            addMarkers(bean2.results);

        }
    };

    private void addMarkers(final List<HotelBean.ResultData> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (HotelBean.ResultData data : result) {
                    if (data.name.startsWith("**")) {
                        MarkerOptions options = new MarkerOptions().position(new LatLng(data.geometry.location.lat, data.geometry.location.lng)).title(data.name);
                        Log.e("XXX", this.getClass().getSimpleName() + " run: "+options.isVisible());
//                        GMarker gMarker;
                        mMap.addMarker(options);
                    }
                }
            }
        });
    }
}
