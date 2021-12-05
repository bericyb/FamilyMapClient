package com.bericb.familymap;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

    private Event currEvent;
    private TextView eventSum;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

        setHasOptionsMenu(true);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventSum = view.findViewById(R.id.mapTextView);
        eventSum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currEvent == null) {
                    Toast.makeText(getActivity(), "Please pick an event.", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("HELLO!" + currEvent.getEventID());
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("PERSON", currEvent.getPersonID());
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.main_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search: {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        DataCache data = DataCache.getInstance();
        Map<String, Event> events = data.getEvents();
        float googleColor = BitmapDescriptorFactory.HUE_RED;
        for (Object entry : events.values()) {
            Event event = (Event) entry;

            String type = event.getEventType();
            switch (type) {
                case "birth":
                    googleColor = BitmapDescriptorFactory.HUE_BLUE;
                    break;
                case "graduation":
                    googleColor = BitmapDescriptorFactory.HUE_RED;
                    break;
                case "marriage":
                    googleColor = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case "death":
                    googleColor = BitmapDescriptorFactory.HUE_VIOLET;
                    break;
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(googleColor))
                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .title(event.getEventType()));
            marker.setTag(event);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    Event clickedEvent = (Event) marker.getTag();
                    DataCache data = DataCache.getInstance();
                    Person person = data.getPersonByID(clickedEvent.getPersonID());

                    String gender = person.getGender();

                    switch (gender) {
                        case "m" : eventSum.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.male, 0,0,0);
                        break;
                        case "f" :
                            eventSum.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.female, 0, 0, 0);
                            break;
                    }

                    String name = person.getFirstName() + " " + person.getLastName();

                    String type = clickedEvent.getEventType();

                    String city = clickedEvent.getCity();

                    String year = Integer.toString(clickedEvent.getYear());
                    String country = clickedEvent.getCountry();

                    currEvent = clickedEvent;
                    eventSum.setText(name + '\n' + type + " : " + city + ", " + country + " (" + year + ")");
                    return false;
                }
            });
        }
   }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }
}
