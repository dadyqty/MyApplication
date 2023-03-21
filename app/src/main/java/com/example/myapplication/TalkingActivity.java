package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.content.res.AssetManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tokenizers.Constants;
import com.example.tokenizers.GPT2Tokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TalkingActivity extends BasicActivity {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private String apiKey;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
            .readTimeout(60, TimeUnit.SECONDS)          //设置读超时
            .writeTimeout(60, TimeUnit.SECONDS)          //设置写超时
            .build();                                   //构建OkHttpClient对象
    private final View.OnLongClickListener lc = v -> {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", ((TextView) v).getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
        return false;
    };
    private List<Message> messages = new ArrayList<>();
    private Session mySession;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private String name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String sex = intent.getStringExtra("sex");
        int chara_photo = intent.getIntExtra("chara_photo",R.drawable.athor);
        TextView textView = (TextView)findViewById(R.id.chat_name);
        EditText send_message = (EditText) findViewById(R.id.send_message);
        ImageView imageView = (ImageView) findViewById(R.id.background2);

        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = outRect.bottom - outRect.top;

        textView.setText("与"+name+"交谈");
        init_messages(name);
        //setup session
        apiKey = getString(R.string.apiKey);
        String character_desc = "你是"+name+",一个由RockStar Games创建的虚拟人物，来自荒野大镖客2, 你旨在回答并解决人们的任何问题，并且可以使用多种语言与人交流。";
        int conversation_max_tokens = Integer.parseInt("1000");
        mySession = new Session(tokenizerFromPretrained(), conversation_max_tokens, character_desc);

        recyclerView = findViewById(R.id.chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(messages,sex,chara_photo, lc);
        recyclerView.setAdapter(adapter);

        Button send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = send_message.getText().toString().trim();
                messages.add(new Message(question,true));
                try {
                    callAPI(question);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                adapter.notifyItemInserted(messages.size()-1);
                send_message.setText("");
                recyclerView.scrollToPosition(messages.size()-1);
                //messages.add(new Message("我还没学会怎么交谈",false));
                //adapter.notifyItemInserted(messages.size()-1);
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //隐藏键盘
                hideSoftInput(TalkingActivity.this, send_message);
                return false;
            }
        });
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }



    private void init_messages(String name)
    {
        messages.add(new Message("你好，我是无所不知的"+name+"\n 请提出你的问题或想法", false));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private GPT2Tokenizer tokenizerFromPretrained() {
        InputStream encoderInputStream;
        InputStream bpeInputStream;
        try {
            AssetManager assetManager = getAssets();
            String path = "tokenizers/gpt2";
            encoderInputStream = assetManager.open(path + "/" + Constants.ENCODER_FILE_NAME);
            bpeInputStream = assetManager.open(path + "/" + Constants.VOCAB_FILE_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return GPT2Tokenizer.fromPretrained(encoderInputStream, bpeInputStream);
    }

    void addToChat(String message, Boolean sentByme) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(new Message(message, sentByme));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
            }
        });
    }

    void addResponse(String response) {
        messages.remove(messages.size() - 1);
        addToChat(response, false);
    }

    void callAPI(String question) throws JSONException {
        if (question.compareToIgnoreCase("#清除记忆") == 0) {
            mySession.clearSession();
            messages.add(new Message("清除完毕",false));
            return;
        }
        //okhttp
        messages.add(new Message("请稍后!"+name+getString(R.string.Typing), false));
        JSONArray newQuestion = mySession.buildSessionQuery(question);

        JSONObject jsonBody = new JSONObject();
        try {
            //String newQuery = newQuestion.toString();
            jsonBody.put("model", "gpt-3.5-turbo");//text-davinci-003
            jsonBody.put("messages", newQuestion);
            jsonBody.put("max_tokens", 1200);// 回复最大的字符数
            jsonBody.put("temperature", 0.9);//值在[0,1]之间，越大表示回复越具有不确定性
            jsonBody.put("top_p", 1);
            jsonBody.put("frequency_penalty", 0.0);
            jsonBody.put("presence_penalty", 0.0);
            //           jsonBody.put("stop", "\n\n\n");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                messages.add(new Message(e.getMessage(),false));
               // addResponse(getString(R.string.failed_load_response) + e.getMessage());
//                mySession.clearSession();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                        Log.i(TAG, jsonArray.toString());
                        JSONObject jsonTokens = jsonObject.getJSONObject("usage");
                        int total_tokens = jsonTokens.getInt("total_tokens");
                        int completion_tokens = jsonTokens.getInt("completion_tokens");
                        if (completion_tokens > 0) {
                            String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");//text
                            addResponse(result);
                            mySession.saveSession(total_tokens, result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    messages.add(new Message("返回内容失败，原因是"+ response.body(),false));
                    //addResponse(getString(R.string.failed_load_response) + response.body().toString());
//                    mySession.clearSession();
                }
            }
        });


    }
}