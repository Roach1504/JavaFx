package sample;

/**
 * Created by Android on 26.02.2018.
 */
public class Point {
    String lat;
    String lng;

    public Point(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
