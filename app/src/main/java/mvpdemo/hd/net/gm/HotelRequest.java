package mvpdemo.hd.net.gm;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HotelRequest {
    public static void fetchHotelByGoogle(final Context context) {
        //https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796&radius=500&types=hotel&language=zh-CN
        // https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796&radius=500&types=hotel&language=zh-CN
        Log.e("XXX", " fetchHotelByGoogle: ");
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", "Mobile");
            obj.put("sensor", true);
            obj.put("key", "AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4");
            obj.put("location", "31.22445747329499,121.47586964070796");
            obj.put("radius", "500");
            obj.put("types", "hotel");
            obj.put("language", "zh-CN");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Callback hotelCallBack = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("XXX", this.getClass().getSimpleName() + " fetchHotelByGoogle onFailure: call:" + call + " " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("XXX", this.getClass().getSimpleName() + " fetchHotelByGoogle onResponse: call:" + call);
                Log.e("XXX", this.getClass().getSimpleName() + " fetchHotelByGoogle onResponse: response:" + response);
            }
        };
        OkHttpRequest.post(context, "https://maps.googleapis.com/maps/api/place/search/json", obj, hotelCallBack);
    }

    public static void testLocal(Context context, String location,Callback hotelCallBack) {
//        https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796&radius=500&types=hotel&language=zh-CN
        // https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796&radius=500&types=hotel&language=zh-CN
        String url ="https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796&radius=500&types=hotel&language=zh-CN";
        Log.e("XXX", " testLocal: " + location);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", "Mobile");
//            obj.put("sensor", true);
            obj.put("location", location);
            obj.put("radius", "500");
            obj.put("types", "lodging");
            obj.put("language", "zh-CN");
            obj.put("key", "AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4");
        } catch (JSONException e) {
            e.printStackTrace();
        }


         url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=500&types=hotel&language=zh-CN";
//        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
//        url = url + "?" + obj.toString();
//        url = "http://172.16.102.74:8077/test/okhttp.json";
        //&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4&location=31.22445747329499,121.47586964070796
//        url = url + "&sensor=true";
        url = url + "&key=" + "AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4";
        url = url + "&location=" + location;
//        url = url + "&location=" + location;
//        url = url + "&location=" + location;
//        url = url + "&location=" + location;
        OkHttpRequest.get(context, url, obj.toString(), hotelCallBack);
    }
}
