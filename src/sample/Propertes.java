package sample;

public class Propertes {
    String id;
    String info;
    String title;
    String site;
    String color;
    Point point;

    public Propertes(String id, String info, String title, String site, String color, Point point) {
        this.id = id;
        this.info = info;
        this.title = title;
        this.site = site;
        this.color = color;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }

    public String getSite() {
        return site;
    }

    public String getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }
}
