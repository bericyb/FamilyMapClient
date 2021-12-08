package com.bericb.familymap;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.MAGENTA;
import static android.graphics.Color.blue;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

    private Event currEvent;
    private TextView eventSum;
    private List<Polyline> lines = new ArrayList<Polyline>();
    private Map<String, Event> events;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

        if (getArguments() != null) {

            String isEventActivity = getArguments().getString("EVENT", null);
            if (isEventActivity != null) {
                currEvent = DataCache.getInstance().getEventByID(isEventActivity);
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

            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventSum = view.findViewById(R.id.mapTextView);
        eventSum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currEvent == null) {
                    Toast.makeText(getActivity(), "Please pick an event.", Toast.LENGTH_SHORT).show();
                } else {
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
    public void onResume() {

        super.onResume();
        if (events != DataCache.getInstance().getEvents() && map != null) {
            map.clear();
            onMapReady(map);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        if (currEvent != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currEvent.getLatitude(), currEvent.getLongitude()), 10.0f));
        }

        DataCache data = DataCache.getInstance();
        events = data.getEvents();
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
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onMarkerClick(Marker marker) {

                    //Remove previous lines after click.
                    for (Polyline line :
                            lines) {
                        line.remove();
                    }
                    lines.clear();

                    map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    Event clickedEvent = (Event) marker.getTag();
                    DataCache data = DataCache.getInstance();
                    Person person = data.getPersonByID(clickedEvent.getPersonID());

                    String gender = person.getGender();

                    switch (gender) {
                        case "m":
                            eventSum.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.male, 0, 0, 0);
                            break;
                        case "f":
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

                    drawLines(currEvent, person, marker);

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawLines(Event currEvent, Person person, Marker marker) {

        //Spouse Lines
        if (DataCache.getInstance().getSettings().isSpouseLines()) {
            List<LatLng> locations = FilterHelper.drawSpouseLines(person);
            for (Object entry :
                    locations) {
                LatLng loc = (LatLng) entry;
                Polyline spouseLine = map.addPolyline(new PolylineOptions()
                        .clickable(false)
                        .add(
                                marker.getPosition(),
                                loc));
                lines.add(spouseLine);
            }
        }

        //Family tree lines
        if (DataCache.getInstance().getSettings().isFamilyTree()) {
            List<LatLngData> points = FilterHelper.familyTreeHelper(person.getFatherID(), person.getMotherID(), marker.getPosition());
            for (Object entry :
                    points) {
                LatLngData data = (LatLngData) entry;
                Polyline familyLine = map.addPolyline(new PolylineOptions().clickable(false)
                .width(10 - data.getLevel()).color(BLUE).add(data.getCurrLoc(), data.getLatLng()));
                lines.add(familyLine);
            }
        }

        //LifeStory Lines
        if (DataCache.getInstance().getSettings().isLifeStory()) {
            List<LatLng> locations = FilterHelper.drawLifeStory(person.getPersonID());
            for (Object entry :
                    locations) {
                LatLng loc = (LatLng) entry;
                Polyline spouseLine = map.addPolyline(new PolylineOptions()
                        .clickable(false)
                        .color(GREEN)
                        .add(
                                marker.getPosition(),
                                loc));
                lines.add(spouseLine);
            }
        }
    }
}
