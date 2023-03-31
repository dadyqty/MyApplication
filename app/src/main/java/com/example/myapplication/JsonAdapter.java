package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonAdapter {
    JSONObject jsonBody;
    String name;
    String question;
    JSONArray newQuestion;
    JSONArray return_array;
    String url;

    public JsonAdapter(JSONObject jsonBody, String name, JSONArray newQuestion) {
        this.jsonBody = jsonBody;
        this.name = name;
        this.newQuestion = newQuestion;
    }

    public void name_to_json() throws JSONException {
        switch (name)
        {
            case("亚瑟·摩根"):
                jsonBody.put("model", "gpt-3.5-turbo");//text-davinci-003 gpt-3.5-turbo
                jsonBody.put("messages", newQuestion);
                jsonBody.put("max_tokens", 1200);// 回复最大的字符数
                jsonBody.put("temperature", 0.9);//值在[0,1]之间，越大表示回复越具有不确定性
                jsonBody.put("top_p", 1);
                jsonBody.put("frequency_penalty", 0.0);
                jsonBody.put("presence_penalty", 0.0);
                url = "https://api.openai.com/v1/chat/completions";
                break;

            case("约翰·马斯顿"):
                jsonBody.put("model", "text-davinci-003");//text-davinci-003 gpt-3.5-turbo
                jsonBody.put("prompt", question);
                jsonBody.put("max_tokens", 1200);// 回复最大的字符数
                jsonBody.put("temperature", 0.9);//值在[0,1]之间，越大表示回复越具有不确定性
                jsonBody.put("top_p", 1);
                jsonBody.put("frequency_penalty", 0.0);
                jsonBody.put("presence_penalty", 0.0);
                url = "https://api.openai.com/v1/completions";
                break;
            case("达奇·范德林德"):
                jsonBody.put("model", "text-davinci-edit-001");//text-davinci-003 gpt-3.5-turbo
                jsonBody.put("input", question);
                jsonBody.put("instruction", "Fix the spelling mistakes");// 回复最大的字符数
                jsonBody.put("temperature", 0.9);//值在[0,1]之间，越大表示回复越具有不确定性
                jsonBody.put("top_p", 1);
                url = "https://api.openai.com/v1/edits";
                break;
            case("何西阿·马修斯"):
                jsonBody.put("prompt", question);
                jsonBody.put("size", "1024x1024");
                url = "https://api.openai.com/v1/images/generations";
                break;
            default:break;
        }

    }
    public String get_result() throws JSONException {
        switch (name)
        {
            case ("亚瑟·摩根"):
                return return_array.getJSONObject(0).getJSONObject("message").getString("content");
            case ("约翰·马斯顿"):
                return return_array.getJSONObject(0).getString("text").trim();
            case ("达奇·范德林德"):
                return return_array.getJSONObject(0).getString("text").trim();
            case ("何西阿·马修斯"):
                return return_array.getJSONObject(0).getString("url").trim();
            default:
                return "空";
        }

    }

}
