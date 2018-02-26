package sample;

/**
 * Created by Android on 26.02.2018.
 */
public class Point {
    double lat;
    double lng;

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
