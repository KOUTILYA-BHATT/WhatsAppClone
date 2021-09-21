package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Models.MessagesModel;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.example.whatsappclone.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();

        final ChatAdapter adapter = new ChatAdapter(messagesModels, this);
        binding.chatRecycView.setAdapter(adapter);
        final  String senderId = FirebaseAuth.getInstance().getUid();
        binding.usersName.setText("GROUP");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                    messagesModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etMessages.getText().toString();
                final MessagesModel model = new MessagesModel(senderId,message);
                model.setTimeStamp(new Date().getTime());
                binding.etMessages.setText("");

                database.getReference().child("Group Chat").push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
            }
        });

    }
}