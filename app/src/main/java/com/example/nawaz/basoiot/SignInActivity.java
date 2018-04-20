package com.example.nawaz.basoiot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton mSignInbtn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9051;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mRootDatabaseRef;

    private FirebaseAuth mAuth;
  private EditText eTextpass;
 private EditText eTextuser;
    private ProgressDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        eTextuser = findViewById(R.id.eTextuser);
        eTextpass = findViewById(R.id.eTextpass);
        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnsignup = findViewById(R.id.btnsignup);
        mSignInbtn = (SignInButton) findViewById(R.id.Signinbtn);

        mSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            registerUser();
                                        }
                                    }
        );


    }

    private void SignIn(){
        // ProgressDialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Signing In");
        mProgressDialog.setMessage("Almost there!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                //Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final FirebaseUser current_User = FirebaseAuth.getInstance().getCurrentUser();
                            final String current_user_uid = current_User.getUid();
                            mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
                            mRootDatabaseRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.hasChild(current_user_uid)){


                                        Map userMap = new HashMap<>();
                                        userMap.put("Users/" + current_user_uid + "/Profile/name",current_User.getDisplayName());
                                        userMap.put("Users/" + current_user_uid + "/Profile/email",current_User.getEmail());
                                        userMap.put("Users/" + current_user_uid + "/Profile/photo_url",current_User.getPhotoUrl().toString());

                                        mRootDatabaseRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                mProgressDialog.dismiss();
                                                sendToHome();
                                            }
                                        });

                                    } else {
                                        mProgressDialog.dismiss();
                                        sendToHome();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    }
                });
    }

    private void sendToHome(){
        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        finish();
    }


    private void login() {
        showDialog("registring...");
        String email = eTextuser.getText().toString();
        String password = eTextpass.getText().toString();
        if (password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideDialog();
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                updateUI(user);

                            } else {
                                String err = task.getException().getMessage();
                                Toast.makeText(SignInActivity.this, err, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void registerUser() {
        String email = eTextuser.getText().toString();
        String password = eTextpass.getText().toString();
        if (password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show();
        }else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                updateUI(user);
                                Toast.makeText(SignInActivity.this, "success", Toast.LENGTH_SHORT).show();
                            } else {
                                String err = task.getException().getMessage();
                                Toast.makeText(SignInActivity.this, err, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null){
            startActivity(new Intent( this,HomeActivity.class));
            finish();
        }
    }

    public void showDialog(String msg){
        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.show();
    }
    public void hideDialog(){
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

