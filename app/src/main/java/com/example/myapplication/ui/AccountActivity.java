package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class AccountActivity extends NavigationActivity {

    private static final String TAG = "AccountActivity";

    private TextView emailText;
    private Button changePasswordButton;
    private Button deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        emailText = findViewById(R.id.emailText);
        changePasswordButton = findViewById(R.id.changePasswordButtton);
        deleteAccountButton = findViewById(R.id.deleteAccountButtton);

        emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference userPlants = db.collection("plants");
        Query query = userPlants.whereEqualTo("user_id", userId);

        CollectionReference userWatering = db.collection("watering");
        Query query1 = userWatering.whereEqualTo("user_id", userId);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordET = new EditText(v.getContext());
                passwordET.setHint("New password");
                passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                AlertDialog.Builder changePasswordDialog = new AlertDialog.Builder(v.getContext());
                changePasswordDialog.setTitle("Change password");
                changePasswordDialog.setView(passwordET);

                changePasswordDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = passwordET.getText().toString();
                        if (newPassword.length() < 8) {
                            Toast.makeText(getApplicationContext(), "Password is too short!", Toast.LENGTH_LONG).show();
                        } else {
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Password changed!", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Password not changed!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });

                changePasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                changePasswordDialog.create().show();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAccountDialog = new AlertDialog.Builder(v.getContext());
                deleteAccountDialog.setTitle("Delete Account");
                deleteAccountDialog.setMessage("Do you wanna delete account?");

                deleteAccountDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        userPlants.document(document.getId()).delete();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        userWatering.document(document.getId()).delete();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                        FirebaseAuth.getInstance().getCurrentUser().delete();
                        Toast.makeText(getApplicationContext(), "Account deleted!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AccountActivity.this, StartActivity.class));
                    }
                });

                deleteAccountDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                deleteAccountDialog.create().show();
            }
        });
    }
}