package sample;


import api.Network;
import cn.javaer.retrofit2.converter.jaxb.JaxbConverterFactory;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main extends Application implements PolygonMVP{

    private boolean ready;
    WebEngine webEngine;
    WebView browser;


    private static Network umoriliApi;
    private Retrofit retrofit;

    public static Network getApi() {
        return umoriliApi;
    }
    String response = null;

    public void test1(String test) {
        List<Propertes> propertesList = new ArrayList<>();
        JSONObject geo = new JSONObject(test);
        JSONArray features = geo.getJSONArray("features");


        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = new JSONObject(features.get(i));
            JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
            Point point = new Point(properties.getString("y"), properties.getString("x"));
            Propertes propertes = new Propertes(
                    properties.getString("id"),
                    properties.getString("info"),
                    properties.getString("title"),
                    properties.getString("site"),
                    properties.getString("color"),
                    point);
            propertesList.add(propertes);

            JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONArray mass = new JSONArray(coordinates.get(0).toString());
            List<Point> pointList = new ArrayList<>();
            for (int j = 0; j < mass.length(); j++) {
                //System.out.println("test "+mass.get(j));
                JSONArray masss = new JSONArray(mass.get(j).toString());
                Point a = new Point(masss.getString(1), masss.getString(0));
                //System.out.println("lat " + a.lat + " lng " + a.lng);
                pointList.add(a);
            }
            JSONArray poly = new JSONArray(pointList);
                invokeJS("addPolygon(" +poly + "," + "\'" + propertesList.get(i).getColor() + "\'" + ")");
         }
    }
    @Override
    public void start(Stage stage) throws Exception{
        retrofit = new Retrofit.Builder()
                .baseUrl("http://109.120.189.141:81")
                .addConverterFactory(JaxbConverterFactory.create())
                .build();
        umoriliApi = retrofit.create(Network.class);


        browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("map.html").toExternalForm());

        VBox root = new VBox();
        root.getChildren().addAll(browser);
        Scene scene = new Scene(root);
        stage.setTitle("JavaFX WebView");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.show();

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                final Worker.State oldState,
                                final Worker.State newState)
            {
                if (newState == Worker.State.SUCCEEDED)
                {
                    ready = true;
                }
            }
        });
        initCommunication();

    }
    private JSObject doc;

    private void initCommunication() {
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                final Worker.State oldState,
                                final Worker.State newState)
            {
                if (newState == Worker.State.SUCCEEDED)
                {
                    doc = (JSObject) webEngine.executeScript("window");
                    doc.setMember("App", Main.this);
                }
            }
        });

        addPolygon();
    }

    public void showJSLog(String message){
        System.out.println("JS ERROR: "+message);
    }

    public void showPolygon(String polygon){
      //  System.out.println("Polygon return: " +polygon);
        JSONObject polygons = new JSONObject(polygon);
        System.out.println("Polygon return: "+polygons.toString());
    }

    public void addPolygon(){
        for(int i=1; i<=20; i++) {
            invokeJS("reqestJS(" + i + ")");
        }
//        List<Point> polygon = new ArrayList<>();
//        Point p1 = new Point("-84.77593", "204.53278" );
//        Point p2 = new Point("-84.767822265625", "204.527099609375");
//        Point p3 = new Point("-84.773193359375", "204.50634765625");
//        polygon.add(p1);
//        polygon.add(p2);
//        polygon.add(p3);
//        JSONArray jsonArray = new JSONArray(polygon);
//        System.out.println("test poligon cread: "+jsonArray.toString());
 //       invokeJS("addPolygon(" + jsonArray.toString() +")");

    }

    public void handle(double lat, double lng) {
        System.out.println("WIN "+lat + " "+ lng);
        setMarkerPosition(lat,lng);
    }



    private void invokeJS( String str) {
        if(ready) {
            doc.eval(str);
        }
        else {
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
            {
                @Override
                public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                    final Worker.State oldState,
                                    final Worker.State newState)
                {
                    if (newState == Worker.State.SUCCEEDED)
                    {
                        doc.eval(str);
                    }
                }
            });
        }
    }

    public void setMarkerPosition(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        System.out.println("WIN java "+sLat + ", "+ sLng);
//        invokeJS("setMarkerPosition(" + sLat + ", " + sLng + ")");
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void test(String poligon) {
        System.out.println("MVP Start");
        response = poligon;

    }
}
