package com.giangdinh.returnnotfound.findhouse.UI.ProfileActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.giangdinh.returnnotfound.findhouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @BindView(R.id.etYourPhone)
    EditText etYourPhone;
    @BindView(R.id.btnSendCode)
    Button btnSendCode;
    @BindView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @BindView(R.id.btnAuth)
    Button btnAuth;

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        auth.setLanguageCode("fr");

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("Test", "onVerificationCompleted");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("Test", "onVerificationFailed");
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d("Test", "onCodeSent:" + verificationId);
                code = verificationId;
            }
        };
    }

    @OnClick(R.id.btnSendCode)
    public void sentClick() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "0964360061",
                10,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    @OnClick(R.id.btnAuth)
    public void authClick() {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(code, etVerifyCode.getText().toString());
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Test", "signInWithCredential:success");
                        } else {
                            Log.w("Test", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }
}
