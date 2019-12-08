package halla.icsw.book;

public class MakerItem {
    double lat;
    double lon;
    String library;

    public MakerItem(double lat, double lon, String library) {
        this.lat = lat;
        this.lon = lon;
        this.library = library;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }
}
