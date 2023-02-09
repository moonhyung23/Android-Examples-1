package com.example.java_mvc_pattern.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.java_mvc_pattern.R;
import com.example.java_mvc_pattern.model.Database;
import com.example.java_mvc_pattern.model.Person;
import com.example.java_mvc_pattern.view.MainViewHolder;

import java.util.Random;

// MainActivity는 View & Controller 역할
// View : onCreate 내부에 View에 속한 내용들이 작성
// Controller 역할 : item 뷰에 들어갈 Model(여기서는 Person)을 받아 처리
public class MainActivity extends AppCompatActivity implements MainViewHolder.HolderClickListener {
    //    public static final String TAG = MainActivity.class.getSimpleName();
    public final String TAG = this.getClass().getSimpleName();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainAdapter adapter;
    Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰 선언 & 세팅
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);

        //리사이클러뷰에 아이템을 입력
        adapter.setItems(database.getPersonList());

        //Model의 데이터가 변하면 리사이클러뷰를 갱신 시킴
        database.setOnDatabaseListener(new Database.DatabaseListener() {
            @Override
            public void onChanged() {
                adapter.setItems(database.getPersonList());
            }
        });


    }

    //옵션메뉴 인터페이스1
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add");
        return super.onCreateOptionsMenu(menu);
    }

    //옵션메뉴 인터페이스2
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        database.add(new Person(System.currentTimeMillis(), String.format("New Charles %d", new Random().nextInt(1000))));
        return super.onOptionsItemSelected(item);
    }

    //삭제버튼 클릭 인터페이스
    @Override
    public void onDeleteClick(Person person) {
        database.remove(person);
    }
}