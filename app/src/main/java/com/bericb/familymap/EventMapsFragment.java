package com.bericb.familymap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import org.w3c.dom.Text;

import java.util.Map;

import model.Event;
import model.Person;

public class EventMapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{

    private Event currEvent;
    private GoogleMap map;

    private TextView eventSum;

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
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currEvent.getLatitude(), currEvent.getLongitude()), 10.0f));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event_maps, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String currEventID = getArguments().getString("EVENT");
        currEvent = DataCache.getInstance().getEventByID(currEventID);

        eventSum = view.findViewById(R.id.mapTextView);

        DataCache data = DataCache.getInstance();
        Person person = data.getPersonByID(currEvent.getPersonID());

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

        String type = currEvent.getEventType();

        String city = currEvent.getCity();

        String year = Integer.toString(currEvent.getYear());
        String country = currEvent.getCountry();

        eventSum.setText(name + '\n' + type + " : " + city + ", " + country + " (" + year + ")");
        eventSum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currEvent == null) {
                    Toast.makeText(getActivity(), "Please pick an event.", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("HELLO!" + currEvent.getEventID());
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra(String.valueOf("PERSON"), currEvent.getPersonID());

                    startActivity(intent);
                }
            }
        });

        return view;
    }


    @Override
    public void onMapLoaded() {

    }
}