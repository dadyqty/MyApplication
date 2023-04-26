package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TalkingActivity extends BasicActivity {
        private MsgRevDao msgRevDao;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private String apiKey;
    private AlertDialog.Builder dialog;
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(0, 1, 0, "保存到相册");
            menu.add(0, 2, 0, "取消");
        }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                if(saveImage29(ChatAdapter.bitmap))
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,"取消",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    private List<Message> messages = new ArrayList<>();
    private Session mySession;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private String name;
    private String sex;
    private int chara_photo;
    private String Personality;
    public Bitmap bitmap = null;
    private MyHandle myHandle = null;
    private String question;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private Button saveButton = null;
    private boolean isRecording = false;
    JsonAdapter jsonAdapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);
        write_permission();
        apiKey = Load_String(UrlApiKey);
        if(apiKey.length()==0)
        {
            dialog = new AlertDialog.Builder(TalkingActivity.this);
            dialog.setIcon(R.drawable.option);
            dialog.setTitle("请设置您的apikey");
            View view1 = LayoutInflater.from(TalkingActivity.this).inflate(R.layout.option, null);
            dialog.setView(view1);
            EditText editText = (EditText)view1.findViewById(R.id.set_apikey);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    apiKey =  editText.getText().toString();
                    if(apiKey.length()==0)
                    {
                        Toast.makeText(TalkingActivity.this,"apikey不能为空",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        SaveString(apiKey,BasicActivity.UrlApiKey);
                    }
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(TalkingActivity.this,"必须设置apikey才能聊天",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            dialog.show();
        }
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sex = intent.getStringExtra("sex");
        chara_photo = intent.getIntExtra("chara_photo",R.drawable.athor);
        Personality = intent.getStringExtra("Personality");
        TextView textView = (TextView)findViewById(R.id.chat_name);
        EditText send_message = (EditText) findViewById(R.id.send_message);
        ImageView imageView = (ImageView) findViewById(R.id.background2);
        saveButton = findViewById(R.id.save_button);
        myHandle = new MyHandle();
        msgRevDao = new MsgRevDao(TalkingActivity.this);
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = outRect.bottom - outRect.top;


        textView.setText("与"+name+"交谈");
        init_messages(name);
        //setup session
        String character_desc = "你是"+name+",一个由RockStar Games创建的虚拟人物，来自荒野大镖客2, 你旨在回答并解决人们的任何问题，并且可以使用多种语言与人交流。";
        int conversation_max_tokens = Integer.parseInt("1000");
        mySession = new Session(tokenizerFromPretrained(), conversation_max_tokens, character_desc);

        recyclerView = findViewById(R.id.chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(messages,sex,name,chara_photo,lc,this.getResources());
        adapter.userphoto = Load_String(UrlUserPhoto);
        adapter.activity = this;
        adapter.button_send = findViewById(R.id.send_button);
        fileName = "data/data/com.example.myapplication/";
        fileName += "recorded_audio.mp3";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            setupMediaRecorder();
        }
        if(name.equals("迈卡·贝尔"))
        {
            adapter.button_send.setText("按住说话");
        adapter.button_send.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecording();
                        messages.add(new Message(fileName,true,false));
                        recyclerView.scrollToPosition(messages.size()-1);
                        byte[] fileBytes = new byte[0];
                        try {
                            fileBytes = Files.readAllBytes(Paths.get(fileName));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String fileContent = new String(fileBytes);
                        System.out.println(fileContent);
                        try {
                            callAPI(fileContent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });}
        recyclerView.setAdapter(adapter);
        Button send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question = send_message.getText().toString().trim();
                messages.add(new Message(question,true,false));
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TalkingActivity.this,SaveChatActivity.class);
                intent1.putExtra("name",name);
                intent1.putExtra("sex",sex);
                intent1.putExtra("chara_photo",chara_photo);
                startActivity(intent1);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.userphotobitmap = adapter.stringToBitmap(adapter.userphoto);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }



    private void init_messages(String name)
    {
        messages.add(new Message("你好，我是无所不知的"+name+"\n我是"+Personality+"开发"+"\n 请提出你的问题或想法", false,false));
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
                messages.add(new Message(message, sentByme,false));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
            }
        });
    }

    void addimgToChat(Bitmap bitmap, Boolean sentByme) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(question, sentByme,true);
                message.setBitmap(bitmap);
                messages.add(message);
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
            messages.add(new Message("清除完毕",false,false));
            return;
        }
        //okhttp
        messages.add(new Message("请稍后!"+name+getString(R.string.Typing), false,false));
        JSONArray newQuestion = mySession.buildSessionQuery(question);
        JSONObject jsonBody = new JSONObject();
        jsonAdapter = new JsonAdapter(jsonBody,name,newQuestion);
        jsonAdapter.question = question;
        try {
            jsonAdapter.name_to_json();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonAdapter.jsonBody.toString(), JSON);
        Request request;
            request = new Request.Builder()
                    .url(jsonAdapter.url)///chat
                    .header("Authorization", "Bearer " + apiKey)
                    .post(body)
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //messages.add(new Message(e.getMessage(),false));
                addResponse("返回失败"+e.getMessage());
//                mySession.clearSession();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if((!name.equals("何西阿·马修斯"))&&(!name.equals("迈卡·贝尔"))){
                            jsonAdapter.return_array = jsonObject.getJSONArray("choices");
                            JSONObject jsonTokens = jsonObject.getJSONObject("usage");
                            int total_tokens = jsonTokens.getInt("total_tokens");
                            int completion_tokens = jsonTokens.getInt("completion_tokens");
                            if (completion_tokens > 0) {
                                String result = jsonAdapter.get_result();//text
                                //String result = jsonArray.getJSONObject(0).getString("text").trim();//text
                                addResponse(result);
                                mySession.saveSession(total_tokens, result);
                            }

                        }
                        else if(name.equals("何西阿·马修斯")){
                            jsonAdapter.return_array = jsonObject.getJSONArray("data");
                            String result = jsonAdapter.get_result();//text
                            addResponse(result);
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        String st = result;
                                        bitmap = adapter.getBitmap(st);
                                        if(bitmap!=null) {
                                            android.os.Message msg = new android.os.Message();
                                            msg.what = 1;
                                            myHandle.sendMessage(msg);
                                        }
                                        else{
                                            android.os.Message msg = new android.os.Message();
                                            msg.what = 2;
                                            myHandle.sendMessage(msg);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                        else
                        {
                            jsonAdapter.return_array = jsonObject.getJSONArray("text");
                            String result = jsonAdapter.get_result();//text
                            addResponse(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MsgInfo msgInfo = new MsgInfo(name,question,sex,0,chara_photo,true);
                    msgRevDao.add(msgInfo);
                    try {
                        msgInfo = new MsgInfo(name,jsonAdapter.get_result(),sex,0,chara_photo,false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    msgRevDao.add(msgInfo);
                } else {
                    addResponse("返回内容失败，原因是"+ response.body());
                    //addResponse(getString(R.string.failed_load_response) + response.body().toString());
//                    mySession.clearSession();
                }
            }
        });


    }
    private class MyHandle extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    messages.remove(messages.size() - 1);
                    addimgToChat(bitmap, false);
                    break;
                case 2:
            }
        }
    }
    private void setupMediaRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(fileName);
    }

    private void startRecording() {
        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
            Toast.makeText(this, "Recording saved to " + fileName, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMediaRecorder();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void write_permission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (TalkingActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    TalkingActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                } else {
                    //这里就是权限打开之后自己要操作的逻辑
                }
            }
        }
    }

    /**
     * API29 中的最新保存图片到相册的方法
     */
    private boolean saveImage29(Bitmap toBitmap) {
        //开始一个新的进程执行保存图片的操作
        Uri insertUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //使用use可以自动关闭流
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(insertUri, "rw");
            if (toBitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                Log.e("保存成功", "success");
                return true;
            } else {
                Log.e("保存失败", "fail");
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
