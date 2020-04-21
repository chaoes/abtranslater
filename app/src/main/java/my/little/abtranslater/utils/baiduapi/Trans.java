package my.little.abtranslater.utils.baiduapi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import my.little.abtranslater.bean.FromWord;
import my.little.abtranslater.bean.ToWord;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Trans {
    String host;
    String id;
    String key;
    OkHttpClient okHttpClient;
    Response response;
    Request request;
    ToWord toWord;
    public Trans(String id,String key,String host){
        this.id = id;
        this.key = key;
        this.host = host;
        this.okHttpClient = new OkHttpClient();
    }
    public ToWord translate(FromWord fromWord) throws IOException {
        Log.d("BB",this.id+" "+this.key+" "+this.host);
        Integer salt = (int) ((Math.random()*100));
        String strasalt = salt.toString();
        String sign = MD5.md5(id+fromWord.getQuery()+strasalt+key);
        RequestBody body = new FormBody.Builder().add("q",fromWord.getQuery()).add("from",fromWord.getFrom()).add("to",fromWord.getTo()).add("appid",this.id).add("salt",strasalt).add("sign",sign).build();
        request = new Request.Builder().removeHeader("User-Agent").post(body).url(host).build();
        response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String json = null;
            try {
                json = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("BB", json);
            try {
                toWord = new ToWord();
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("error_code")) {
                    toWord.setError_code(jsonObject.getString("error_code"));
                    toWord.setError_msg(jsonObject.getString("error_msg"));
                }
                if(jsonObject.has("trans_result")) {
                    JSONArray resultarray = jsonObject.getJSONArray("trans_result");
                    toWord.setFrom(jsonObject.getString("from"));
                    toWord.setTo(jsonObject.getString("to"));
                    ArrayList<HashMap<String, String>> trans_result = new ArrayList<HashMap<String, String>>();
                    HashMap hashMap = new HashMap();
                    for (int i = 0; i < resultarray.length(); i++) {
                        JSONObject onejson = (JSONObject) resultarray.get(i);
                        hashMap.put("src", onejson.getString("src"));
                        hashMap.put("dst", onejson.getString("dst"));
                        trans_result.add(hashMap);
                    }
                    toWord.setTrans_result(trans_result);
                }
                Log.d("BB", toWord.toString());
            } catch (JSONException e) {
                Log.d("BB", e.toString());
            }
        }
        return toWord;
    }
}
