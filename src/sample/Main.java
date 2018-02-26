package sample;


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
import org.json.JSONArray;


import java.util.ArrayList;
import java.util.List;



public class Main extends Application {

    private boolean ready;
    WebEngine webEngine;
    WebView browser;

    @Override
    public void start(Stage stage) throws Exception{

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
        addPoligone();
    }

    public void showJSLog(String message){
        System.out.println("JS ERROR: "+message);
    }


    public void addPoligone(){
        List<Point> poligon = new ArrayList<>();
        Point p1 = new Point(-84.77593, 204.53278 );
        Point p2 = new Point(-84.767822265625, 204.527099609375);
        Point p3 = new Point(-84.773193359375, 204.50634765625);
        poligon.add(p1);
        poligon.add(p2);
        poligon.add(p3);
        JSONArray jsonArray = new JSONArray(poligon);
        System.out.println(jsonArray.toString());
        //invokeJS("addPoligon(" + jsonArray.toString() + ")");

    }

    public void handle(double lat, double lng) {
        System.out.println("WIN "+lat + " "+ lng);
        setMarkerPosition(lat,lng);
    }



    private void invokeJS(final String str) {
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

//    public void setMapCenter(double lat, double lng) {
//        String sLat = Double.toString(lat);
//        String sLng = Double.toString(lng);
//        invokeJS("setMapCenter(" + sLat + ", " + sLng + ")");
//    }
//
//    public void switchSatellite() {
//        invokeJS("switchSatellite()");
//    }
//
//    public void switchRoadmap() {
//        invokeJS("switchRoadmap()");
//    }
//
//    public void switchHybrid() {
//        invokeJS("switchHybrid()");
//    }
//
//    public void switchTerrain() {
//        invokeJS("switchTerrain()");
//    }
//
//    public void startJumping() {
//        invokeJS("startJumping()");
//    }
//
//    public void stopJumping() {
//        invokeJS("stopJumping()");
//    }
//
//    public void setHeight(double h) {
//        browser.setPrefHeight(h);
//    }
//
//    public void setWidth(double w) {
//        browser.setPrefWidth(w);
//    }
//
//    public ReadOnlyDoubleProperty widthProperty() {
//        return browser.widthProperty();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
