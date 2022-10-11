package com.bilcodes.g2c;

import static android.view.View.GONE;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilcodes.g2c.adapters.CheckListAdapter;
import com.bilcodes.g2c.models.CheckListModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton fabButton;
    private DatabaseReference mDatabase;
    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    ArrayList<CheckListModel> checkItemsList;
    ArrayList<String> keyList;
    ProgressBar progressIndicator;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        fabButton = findViewById(R.id.add_fab);
        progressIndicator = findViewById(R.id.progress_indicator);
        emptyText = findViewById(R.id.empty_text);
        emptyText.setVisibility(GONE);
        Dialog dialog = new Dialog(MainActivity.this);

        mDatabase =  FirebaseDatabase.getInstance().getReference("CheckLists");

        recyclerView = findViewById(R.id.check_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkItemsList = new ArrayList<>();
        keyList = new ArrayList<>();

        checkListAdapter = new CheckListAdapter(this, checkItemsList, mDatabase, keyList, emptyText);
        recyclerView.setAdapter(checkListAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CheckListModel item = dataSnapshot.getValue(CheckListModel.class);

                    if(!keyList.contains(dataSnapshot.getKey())){
                        keyList.add(dataSnapshot.getKey());
                        checkItemsList.add(item);
                    }

                }

                if(checkItemsList.isEmpty()){
                    emptyText.setVisibility(View.VISIBLE);
                }else{
                    emptyText.setVisibility(GONE);
                }
                progressIndicator.setVisibility(GONE);
                checkListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        fabButton.setOnClickListener(v -> {
            dialog.setContentView(R.layout.add_item_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);


            TextInputEditText inputEditText = dialog.findViewById(R.id.check_item_input);
            Button addItemButton = dialog.findViewById(R.id.add_item_button);

            addItemButton.setOnClickListener(v1 -> {
                Map<String, String> taskData = new HashMap<>();

                taskData.put("task", Objects.requireNonNull(inputEditText.getText()).toString());
                mDatabase.push().setValue(taskData).addOnCompleteListener(task -> dialog.dismiss());

            });
            dialog.show();
        });
    }
}