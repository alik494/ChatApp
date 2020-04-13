package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firebase.client.ServerValue;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final int RC_GET_IMAGE = 101;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference reference;

    private RecyclerView recyclerViewMessage;
    private MessageAdapter adapter;

    private EditText editTextMessage;
    private ImageView imageViewSend;
    private ImageView imageViewAddImage;


    private FirebaseUser user;


    private String author;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSend = findViewById(R.id.imageViewSendMessage);
        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = FirebaseFirestore.getInstance();
        adapter = new MessageAdapter(this);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessage.setAdapter(adapter);

        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
            //Toast.makeText(this, "logged", Toast.LENGTH_SHORT).show();
            if (user != null) {
                Toast.makeText(this, "welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                preferences.edit().putString("author", mAuth.getCurrentUser().getEmail()).apply();
            }
        } else {
            signOut();
        }
        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, RC_GET_IMAGE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("messages").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                    adapter.setMessages(messages);
                    if (!messages.isEmpty()) {
                        recyclerViewMessage.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    /// Блок отвечающий за выхиод из аккаунта для подкючения
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSignOut) {
            mAuth.signOut();
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClikcSendMessage(View view) {
        String textOfMessage = editTextMessage.getText().toString().trim();
        sendMessage(textOfMessage, null);

    }


    private void sendMessage(String textOfMessage, String urlOfImage) {
        Message message = null;

        String author = preferences.getString("author", "Anonim");
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            message = new Message(author, textOfMessage, null, null);
        } else if (urlOfImage != null && !urlOfImage.isEmpty()) {
            message = new Message(author, null, null, urlOfImage);
        }
        if (message != null) {
            db.collection("messages")
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            recyclerViewMessage.scrollToPosition(adapter.getItemCount() - 1);
                            //Toast.makeText(MainActivity.this, "success" + author, Toast.LENGTH_SHORT).show();
                            editTextMessage.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    final StorageReference referenceToImage = reference.child("images/" + uri.getLastPathSegment());
                    referenceToImage.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return referenceToImage.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    //Log.i("dfsdfgs", downloadUri.toString());
                                    sendMessage(null, downloadUri.toString());
                                }
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                }
            }
        }
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = mAuth.getCurrentUser();
                if (user != null) {
                    //  Toast.makeText(this, "welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    preferences.edit().putString("author", user.getEmail()).apply();
                }
                // ...
            } else {
                if (response != null) {
                    Toast.makeText(this, "Fail " + response.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });

    }
}
