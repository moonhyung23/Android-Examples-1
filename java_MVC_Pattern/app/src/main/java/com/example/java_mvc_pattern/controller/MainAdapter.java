package com.example.java_mvc_pattern.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java_mvc_pattern.R;
import com.example.java_mvc_pattern.model.Person;
import com.example.java_mvc_pattern.view.MainViewHolder;

import java.util.ArrayList;
import java.util.List;

// MainAdapter는 View & Controller & Adapter 역할
// View : 리사이클러뷰에 추가될 item 뷰를 세팅
// Controller 역할 : item 뷰에 들어갈 Model(여기서는 Person)을 받아 처리
// Adapter 역할 : 해당 아이템의 View/Controller를 리사이클러뷰가 셋팅되어있는 View/Controller인 액티비티와 상호 접근이 가능하도록 하는 Adapter
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private List<Person> items = new ArrayList<>();
    private MainViewHolder.HolderClickListener holderClickListener;

    public MainAdapter(MainViewHolder.HolderClickListener holderClickListener) {
        this.holderClickListener = holderClickListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_main, viewGroup, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int position) {
        mainViewHolder.setPerson(items.get(position));
        mainViewHolder.setOnHolderClickListener(holderClickListener);
    }

    public void setItems(List<Person> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Person> getItems() {
        return items;
    }
}
