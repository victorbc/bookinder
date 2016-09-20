//package equipe.projetoes.utilis;
//
//import android.util.Log;
//
//import java.io.IOException;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
///**
// * Created by stenio on 9/12/2016.
// */
//public class RestDAO {
//    OkHttpClient restClient = new OkHttpClient();
//    final String ENDPOINT = "http://192.168.25.4:8000";
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    private ResponseBody get(String url) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = restClient.newCall(request).execute();
//
//        return response.body();
//    }
//
//    private ResponseBody post(String url, String json) throws IOException {
//        RequestBody body = RequestBody.create(JSON, json);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        Response response = restClient.newCall(request).execute();
//
//        return response.body();
//    }
//
//    public String getToken(String username, String password) throws IOException{
//        ResponseBody response = get(ENDPOINT + "/api-token-auth/");
//        Log.w("EROOOOO", response.string());
//
//        return response.string();
//    }
//}
