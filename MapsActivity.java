package halla.icsw.book;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tv_marker = findViewById(R.id.tv_marker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pos=new LatLng(37.344648, 127.945745);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);

        setCustomMarkerView();
        getSampleMarkerItems();

    }

    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }

    private void getSampleMarkerItems() {
        ArrayList<MakerItem> sampleList = new ArrayList();


        sampleList.add(new MakerItem(37.330213, 127.949089, "북새통(단구점)"));
        sampleList.add(new MakerItem(37.337553, 127.928532, "북새통(무실점)"));
        sampleList.add(new MakerItem(37.347364, 127.926760, "북스타문고(단계동)"));
        sampleList.add(new MakerItem(37.341900, 127.940078, "하나로문고(단계동)"));
        sampleList.add(new MakerItem(37.344323, 127.943592, "한길서점(원동)"));
        sampleList.add(new MakerItem(37.306159, 127.921592, "대학서점화방(흥업면)"));
        sampleList.add(new MakerItem(37.323425, 127.950428, "피카소BOOK(단구동)"));
        sampleList.add(new MakerItem(37.330168, 127.949948, "신세계도서할인매장\n(단구동)"));
        sampleList.add(new MakerItem(37.353847, 127.934583, "동그라미(단계동)"));
        sampleList.add(new MakerItem(37.349567, 127.948571, "동아서관(중앙동)"));
        sampleList.add(new MakerItem(37.346703, 127.953356, "대성서점(인동)"));
        sampleList.add(new MakerItem(37.341605, 127.953783, "동화사서점(명륜동)"));
        sampleList.add(new MakerItem(37.340954, 127.956071, "책바라기(개운동)"));
        sampleList.add(new MakerItem(37.337870, 127.958346, "원주 CC서적(개운동)"));
        sampleList.add(new MakerItem(37.369600, 127.937037, "양지서점(우산동)"));
        sampleList.add(new MakerItem(37.368199, 127.934401, "북소리(우산동)"));


        for (MakerItem markerItem : sampleList) {
            addMarker(markerItem, false);
        }
    }


    private Marker addMarker(MakerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String library = markerItem.getLibrary();

        tv_marker.setText(library);

        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.marker666);
            tv_marker.setWidth(10);
            tv_marker.setHeight(10);
            tv_marker.setTextColor(Color.WHITE);

        }


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(library);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));


        return mMap.addMarker(markerOptions);

    }

    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {

        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String library = String.valueOf(marker.getPosition());
        MakerItem temp = new MakerItem(lat, lon, library);
        return addMarker(temp, isSelectedMarker);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
//        changeSelectedMarker(marker);
        return true;
    }

    private void changeSelectedMarker(Marker marker) {
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();

        }
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }

    }
    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }

}
