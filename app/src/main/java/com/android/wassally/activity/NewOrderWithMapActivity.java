package com.android.wassally.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.BuildConfig;
import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.networkUtils.services.FetchAddressIntentService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NewOrderWithMapActivity extends Activity implements OnMapReadyCallback {

    //location
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLAstKnownLocation;
    private LocationCallback locationCallback;

    //places
    private PlacesClient placesClient;
    List<AutocompletePrediction> predictionList;

    //views
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button subimtLocationButton;
    private RippleBackground rippleBg;
    private TextView mDisplayAddressTextView;
    private ProgressBar mProgressBar;
    ImageView mSmallPinIv;

    //variables
    private String addressOutput;
    private int addressResultCode;
    private boolean isSupportedArea;

    private final float DEFAULT_ZOOM = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_with_map);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NewOrderWithMapActivity.this);
        Places.initialize(NewOrderWithMapActivity.this, BuildConfig.ApiKey);
        placesClient = Places.createClient(NewOrderWithMapActivity.this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        materialSearchBar = findViewById(R.id.searchBar);
        subimtLocationButton = findViewById(R.id.submit_location_button);
        rippleBg = findViewById(R.id.ripple_bg);
        mDisplayAddressTextView = findViewById(R.id.tv_display_marker_location);
        mProgressBar = findViewById(R.id.progress_bar);
        mSmallPinIv = findViewById(R.id.small_pin);


        Intent intent = getIntent();
        if (intent.hasExtra(Constants.LOCATION_REQUEST_KEY)) {
            int locationRequestCode = intent.getIntExtra(Constants.LOCATION_REQUEST_KEY, 1);
            if (locationRequestCode == Constants.PICKUP_LOCATION_REQUEST) {
                subimtLocationButton.setText("Confirm pickup");
            } else {
                subimtLocationButton.setText("Confirm destination");
            }
        }


        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opining or closing the navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                    materialSearchBar.clearSuggestions();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("eg")
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!materialSearchBar.isSuggestionsVisible()) {
                                            materialSearchBar.showSuggestionsList();
                                        }
                                    }
                                }, 1000);

                            }

                        } else {
                            Log.i("myTag", "prediction fetching task unSuccessful");
                        }

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                }

                String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "place found " + place.getName() + place.getAddress());
                        LatLng latLng = place.getLatLng();
                        if (latLng != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                        }

                        rippleBg.startRippleAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rippleBg.stopRippleAnimation();
                            }
                        }, 2000);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    apiException.printStackTrace();
                                    int statusCode = apiException.getStatusCode();
                                    Log.i("mytag", "place not found" + e.getMessage());
                                    Log.i("mytag", "status code : " + statusCode);
                                }

                            }
                        });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        subimtLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressResultCode == Constants.FAILURE_RESULT || !isSupportedArea) {
                    Toast.makeText(NewOrderWithMapActivity.this, "failed to select location please try again", Toast.LENGTH_SHORT).show();
                } else {

                    Intent data = new Intent();
                    data.putExtra("address", addressOutput);
                    setResult(Constants.RESULT_OK, data);
                    finish();
                }

            }
        });
    }

    @SuppressLint("MissingPermission")

    /**
     * is triggered when the map is loaded and ready to display
     * @param GoogleMap
     *
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        //enable location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //move location button to the required position and adjust params such margin
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 60, 500);
        }


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(NewOrderWithMapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //if task is successful means the gps is enabled so go and get device location amd move the camera to that location
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        //if task failed means gps is disabled so ask user to enable gps
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(NewOrderWithMapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible()) {
                    materialSearchBar.clearSuggestions();
                }
                if (materialSearchBar.isSearchEnabled()) {
                    materialSearchBar.disableSearch();
                }
                return false;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mSmallPinIv.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                mDisplayAddressTextView.setVisibility(View.INVISIBLE);
                Log.i("mytag", "changing address");
//                ToDo : you can use retrofit for this network call instead of using services
                //hint: services is just for doing background tasks when the app is closed no need to use services to update ui
                //best way to do network calls and then update user ui is Retrofit .. consider it 
                startIntentService();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    /**
     * is triggered whenever we want to fetch device location
     * in order to get device's location we use FusedLocationProviderClient object that gives us the last location
     * if the task of getting last location is successful and not equal to null ,
     * apply this location to mLastLocation instance and move the camera to this location
     * if the task is not successful create new LocationRequest and LocationCallback instances and update lastKnownLocation with location result
     */
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLAstKnownLocation = task.getResult();
                            if (mLAstKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLAstKnownLocation.getLatitude(), mLAstKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(1000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLAstKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLAstKnownLocation.getLatitude(), mLAstKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        //remove location updates in order not to continues check location unnecessarily
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, null);

                            }
                        } else {
                            Toast.makeText(NewOrderWithMapActivity.this, "Unable to get last location ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    protected void startIntentService() {
        //vars
        LatLng currentMarkerPosition = mMap.getCameraPosition().target;
        AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler());

        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_LAT_EXTRA, currentMarkerPosition.latitude);
        intent.putExtra(Constants.LOCATION_LNG_EXTRA, currentMarkerPosition.longitude);

        startService(intent);
    }

    private void updateUi() {
        mDisplayAddressTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mMap.clear();
        if (addressResultCode == Constants.SUCCESS_RESULT && !addressOutput.contains("Egypt")) {
            mSmallPinIv.setVisibility(View.GONE);
            isSupportedArea = false;
            mDisplayAddressTextView.setText(getString(R.string.wassally_not_support_area));
        } else if (addressResultCode == Constants.FAILURE_RESULT) {
            mSmallPinIv.setVisibility(View.GONE);
            mDisplayAddressTextView.setText(addressOutput);
        } else {
            mSmallPinIv.setVisibility(View.VISIBLE);
            isSupportedArea = true;
            mDisplayAddressTextView.setText(addressOutput);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            addressResultCode = resultCode;

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (addressOutput == null) {
                addressOutput = "";
            }
            updateUi();

        }
    }
}

