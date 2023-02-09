package com.example.a210420_ex_json_volley;


import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class dd {

}
    public void clickPost(View view) {

        final String name= etName.getText().toString();
        final String msg= etMsg.getText().toString();

        //Post 방식으로 보낼 서버 주소
        String serverUrl= "http://umul.dothome.co.kr/Android/postTest.php";

        //PostTest.php로 보낼 요청 객체 생성
        //결과를 String으로 받는 객체
        StringRequest stringRequest= new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            //POST 방식으로 보낼 데이터를
            //리턴해주는 콜백 메소드

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> datas= new HashMap<>();
                datas.put("name", name);
                datas.put("msg",msg);

                return datas;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
