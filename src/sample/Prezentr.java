package sample;

import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Prezentr {
    List<Propertes> propertesList = new ArrayList<>();

    public List<Propertes> getPropertesList() {
        return propertesList;
    }

    PolygonMVP mvp;

    public Prezentr(PolygonMVP mvp) {
        this.mvp = mvp;
    }

    public void test(int id){
        System.out.println("start Load");
        Main.getApi().getPlygon(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String resp = null;
                try {
                    resp = response.body().string();
                    System.out.println("Network: "+resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject geo = new JSONObject(resp);
                JSONArray features = geo.getJSONArray("features");

                for(int i = 0; i < features.length();i++){
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
                    System.out.println("coordinates: " + coordinates.get(0).toString());
                    JSONArray mass = new JSONArray(coordinates.get(0).toString());
                    List<Point> pointList = new ArrayList<>();
                    for(int j = 0; j<mass.length(); j++){
                        //System.out.println("test "+mass.get(j));
                        JSONArray masss = new JSONArray(mass.get(j).toString());
                        Point a = new Point(masss.getString(1),masss.getString(0));
                        //System.out.println("lat " + a.lat + " lng " + a.lng);
                        pointList.add(a);
                    }
                    JSONArray poli = new JSONArray(pointList);
                    System.out.println("poli.toString()"+poli.toString());
                    mvp.test(poli.toString());
                    //invokeJS("addPolygon(" + poli.toString() +")");
                }
                System.out.println("propertes "+ propertesList.size());


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }
}

