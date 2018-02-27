package api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Network {
    @FormUrlEncoded
    @POST("/web/court_new/district_s.php")
    Call<ResponseBody> getPlygon(@Field("id") int id);
}
