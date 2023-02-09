package com.example.a210426_ex_papago_api;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a210426_ex_papago_api.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView toLanguageTV;
    TextView fromLanguageTV;
    TextView changedTextTV;

    ImageButton languageChangeIB;
            
    EditText whatTranslateET;

    // 기본 언어 한글
    String language = "Korean";

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        init();
        buttonClickListenr();


        whatTranslateET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable editable) {

                new Thread(){
                    @Override
                    public void run() {
                        String word = whatTranslateET.getText().toString();
                        // Papago는 3번에서 만든 자바 코드이다.
                        Papago papago = new Papago();
                        String resultWord;

                        if(language.equals("Korean")){
                            resultWord= papago.getTranslation(word,"ko","en");
                        }else{
                            resultWord= papago.getTranslation(word,"en","ko");
                        }

                        Bundle papagoBundle = new Bundle();
                        papagoBundle.putString("resultWord",resultWord);

                        Log.d(TAG, "resultWord" + resultWord);
                        //핸들러에 데이터를 보내서 해당 데이터를 이용해서 ui를 변경
                        Message msg = papago_handler.obtainMessage();
                        msg.setData(papagoBundle);
                        papago_handler.sendMessage(msg);

                        Log.d(TAG, "msg" + msg);
                    }
                }.start();

            }
        });
    }

    private void init(){
        toLanguageTV = findViewById(R.id.toLanguageTV);
        fromLanguageTV = findViewById(R.id.fromLanguageTV);
        languageChangeIB = findViewById(R.id.languageChangeIB);
        whatTranslateET = findViewById(R.id.whatTranslateET);
        changedTextTV = findViewById(R.id.changedTextTV);
    }

//    핸들러를 사용해서 ui 변경
    @SuppressLint("HandlerLeak")
    Handler papago_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String resultWord = bundle.getString("resultWord");
            changedTextTV.setText(resultWord);
        }
    };

    private void buttonClickListenr(){
        languageChangeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "languageChangeIB onClick");

//                한국어, 영어 선택
                if(language.equals("Korean")){
                    language= "English";

                    toLanguageTV.setText("영어");
                    fromLanguageTV.setText("한국어");

                }else{
                    language= "Korean";

                    toLanguageTV.setText("한국어");
                    fromLanguageTV.setText("영어");
                }
            }
        });
    }


    public class Papago {
//        번역해주는 메서드
        public String getTranslation(String word, String source, String target) {

            String clientId = "YzFM3tY68DwcKKhBFCad";
            String clientSecret = "giKs0ProbL";

            try {
                String wordSource, wordTarget;
                String text = URLEncoder.encode(word, "UTF-8");             //word
                wordSource = URLEncoder.encode(source, "UTF-8");
                wordTarget = URLEncoder.encode(target, "UTF-8");

                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                //네이버 파파고 웹사이트 연결
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//               post방식으로 전달
                con.setRequestMethod("POST");
//              서버에 id, select 전달
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source="+wordSource+"&target="+wordTarget+"&text=" + text;
                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
//                서버에서 받아온 데이터를 읽는다.
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                //System.out.println(response.toString());
                String s = response.toString();
                s = s.split("\"")[27];
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "0";
        }
    }
}