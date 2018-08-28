package mvpdemo.hd.net.gm;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelBean {
    class Location {
        double lat;
        double lng;
    }

    class Viewport {
        Location northeast;
        Location southwest;
    }

    class Geometry {
        Location location;
        Viewport viewport;
    }

    public static class ResultData {
        Geometry geometry;
        String icon;
        String id;
        String name;
        String place_id;
        String vicinity;
        String[] types;


    }

    String next_page_token;
    List<ResultData> results;
    int type_count;

    public static HotelBean parse(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {
            Gson gson = new Gson();
            HotelBean resultData = gson.fromJson(jsonString, HotelBean.class);
//            Log.e("XXX", " parse: " + resultData);
            return resultData;
        }

        return null;
    }

    public static HotelBean parse(String jsonString, String filterType) {
        HotelBean bean = parse(jsonString);
        ArrayList<ResultData> resultData = new ArrayList<>();
        bean.type_count = 0;
        for (ResultData data : bean.results) {
            if (Arrays.toString(data.types).contains(filterType)) {
//                resultData.add(data);
                data.name = "**" + data.name;
                bean.type_count++;
            }
            resultData.add(data);
        }
        bean.results = resultData;
        return bean;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\n[HotelBean:size=" + results.size() + " type_count:" + type_count);
        for (ResultData data : results) {
            builder.append("\n(" + data.name + ", " + data.vicinity + ", " + Arrays.toString(data.types) + "),");
        }
        builder.append("\n]");

        return builder.toString();
    }
}
