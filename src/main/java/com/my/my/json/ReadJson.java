package com.my.my.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ReadJson {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "src/main/config/test.json"));// 读取原始json文件
            String s = null, ws = null;
            String jsonStr = "";
            while ((s = br.readLine()) != null) {
                // System.out.println(s);
                jsonStr = jsonStr+ s;
            }
            br.close();
            try {
                JSONObject dataJson = new JSONObject(jsonStr);// 创建一个包含原始json串的json对象
                JSONArray features = dataJson.getJSONArray("features");// 找到features的json数组
                for (int i = 0; i < features.length(); i++) {
                    JSONObject info = features.getJSONObject(i);// 获取features数组的第i个json对象
                    JSONObject properties = info.getJSONObject("properties");// 找到properties的json对象
                    String name = properties.getString("name");// 读取properties对象里的name字段值
                    System.out.println(name);
                }
                ws = dataJson.toString();
                System.out.println(ws);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
