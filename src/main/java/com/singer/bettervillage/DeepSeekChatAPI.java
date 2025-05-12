package com.singer.bettervillage;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;

public class DeepSeekChatAPI {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "sk-b582ae9f765a414886c29404f7ed001b";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static String chat(String userPrompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model","deepseek-chat");

        JsonArray messages = new JsonArray();
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role","user");
        userMsg.addProperty("content",userPrompt);
        messages.add(userMsg);

        requestBody.add("messages",messages);

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization","Bear "+ API_KEY)
                .header("Content-Type","application/json")
                .post(RequestBody.create(gson.toJson(requestBody),MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()){
                return "[请求失败] HTTP" + response.code();
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return "[请求失败：响应体为空]";
            }

            String json = responseBody.string();

            JsonObject jsonObj = gson.fromJson(json, JsonObject.class);

            return jsonObj.getAsJsonArray("choices")
                    .get(0).getAsJsonObject().getAsJsonObject("message")
                    .get("content").getAsString();
        } catch (IOException e) {
            return "[请求异常] " + e.getMessage();
        }
    }
}
