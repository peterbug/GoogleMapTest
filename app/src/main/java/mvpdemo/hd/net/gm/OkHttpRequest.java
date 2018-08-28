package mvpdemo.hd.net.gm;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRequest {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
//    static OkHttpClient client = new OkHttpClient();

    public static void post(final Context context, final String url, final String json, Callback callback) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .addHeader("User-Agent", getUserAgent(context))
//                .addHeader("Content-Type", "application/json")
                        .url(url)
                        .post(body)
                        .build();

//        client.newCall(request).enqueue(callback);
                try {
                    Log.e("XXX", " post: " + request);
                    Response r = client.newCall(request).execute();
                    Log.e("XXX", " post: " + r.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void post(final Context context, String url, JSONObject json, Callback callback) {
        if (json != null) {
            post(context, url, json.toString(), callback);
        } else {
            callback.onFailure(null, new IOException("Json can't be null"));
        }
    }

    public static void get(final Context context, final String url, String json, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
//                super.run();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("User-Agent", getUserAgent(context))
//                .addHeader("Content-Type", "application/json")
                        .url(url)
                        .build();

//                try {
                Log.e("XXX", " get: " + url);
//                    Response r = client.newCall(request).execute();
//                    Log.e("XXX", " get r: " + r.body().string());
                client.newCall(request).enqueue(callback);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }.start();
    }

    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
