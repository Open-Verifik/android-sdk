package co.verifik.verifikkit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import co.mat.verifikkit.Verifik;
import co.mat.verifikkit.VerifikCallback;
import co.mat.verifikkit.VerifikDocType;
import co.verifik.verifikkit.databinding.ActivitySampleBinding;

public class SampleActivity extends AppCompatActivity implements VerifikCallback {
    private ActivitySampleBinding binding;
    private Verifik verifik;
    private Verifik verifikKYC;
    private Boolean initVerifik = false;
    final private String refId = "VerifikSample"+ UUID.randomUUID().toString();
    //Complete this with your data
    final private String token = "";
    final private String tokenKYC = "";
    final private String projectID = "";
    final private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        verifik = new Verifik(this, token, this);
        verifikKYC = new Verifik(this, tokenKYC, this);

        binding.registerButton.setOnClickListener(view -> {
            if(initVerifik){
                verifik.enroll(refId);
            }
        });
        binding.authButton.setOnClickListener(view -> {
            if(initVerifik){
                verifik.authenticate(refId);
            }
        });
        binding.documentScannerButton.setOnClickListener(view -> {
            if(initVerifik){
                verifik.matchIDScan(refId);
            }
        });
        binding.ocrButton.setOnClickListener(view -> {
            if(initVerifik){
                verifik.photoIDScan(refId);
            }
        });
        binding.appRegisterButton.setOnClickListener(view -> {
            if (initVerifik) {
                verifikKYC.appRegistrationKYC(projectID,"",phone);
            }
        });
        binding.appLoginButton.setOnClickListener(view -> {
            if (initVerifik) {
                verifikKYC.appLoginKYC(projectID,"",phone);
            }
        });
        binding.appPhotoIdScanButton.setOnClickListener(view -> {
            if (initVerifik) {
                verifikKYC.appPhotoIDScanKYC(projectID, VerifikDocType.AUTOMATIC_DETECTION);
            }
        });
    }

    @Override
    public void initVerifikSuccess() {
        initVerifik = true;
    }

    @Override
    public void appRegisterSuccessful(String token) {
        Log.d("Verifik","App register successful: " +  token);
    }

    @Override
    public void appLoginSuccessful(String token) {
        Log.d("Verifik","App login successful: " + token);
    }

    @Override
    public void appPhotoIDScanSuccessful(String token) {
        Log.d("Verifik", "Register token succesful: " + token);
    }

    @Override
    public void configError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void sessionError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void enrollmentError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void authError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void photoIDMatchError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void photoIDScanError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void appRegisterError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void appLoginError(String error) {
        Log.d("Verifik",error);
    }

    @Override
    public void appPhotoIDScanError(String error) {
        Log.d("Verifik",error);
    }
}