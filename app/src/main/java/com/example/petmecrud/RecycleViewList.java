package com.example.petmecrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RecycleViewList extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter adapter;
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_list);

        recyclerView =findViewById(R.id.recyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView=findViewById(R.id.app_bar_search);


        FirebaseRecyclerOptions<PostModel> options= new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(FirebaseDatabase.getInstance()
                        .getReference().child("Languages"),PostModel.class)
                .build();


        adapter=new PostAdapter(options,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        MenuItem menuItem =menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void searchData(String s){
        FirebaseRecyclerOptions<PostModel> options= new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(FirebaseDatabase.getInstance()
                        .getReference().child("Languages").orderByChild("courseName").startAt(s).endAt(s+"\ufaff"),PostModel.class)
                .build();


        adapter=new PostAdapter(options,this);
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }
}