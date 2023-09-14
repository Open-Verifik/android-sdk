package co.verifik.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.mat.verifikkit.Verifik;
import co.mat.verifikkit.VerifikCallback;
import co.mat.verifikkit.VerifikDocType;
import co.verifik.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements VerifikCallback {
    ActivityMainBinding binding;
    boolean initVerifik = false;
    Verifik verifik;
    //Complete this to use this app
    final private String tokenKYC = "";
    final private String projectID = "";
    final private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        verifik = new Verifik(this,tokenKYC, this);
        binding.buttonRegister.setOnClickListener(view -> {
            if(initVerifik) {
                //verifik.authenticate("0001");
                verifik.appRegistrationKYC(projectID,"",phone);
            }
        });
    }

    @Override
    public void initVerifikSuccess() {
        initVerifik = true;
    }

    @Override
    public void appRegisterSuccessful(String s) {
        Toast.makeText(this, "Register: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appLoginSuccessful(String s) {
        Toast.makeText(this, "Login: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appPhotoIDScanSuccessful(String s) {
        Toast.makeText(this, "AppPhotoIDScanSuccessful: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void configError(String s) {
        Toast.makeText(this, "Config Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionError(String s) {
        Toast.makeText(this, "Session Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void enrollmentError(String s) {
        Toast.makeText(this, "Enrollment Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void authError(String s) {
        Toast.makeText(this, "Auth Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void photoIDMatchError(String s) {
        Toast.makeText(this, "Photo ID Match Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void photoIDScanError(String s) {
        Toast.makeText(this, "Photo ID Scan Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appRegisterError(String s) {
        Toast.makeText(this, "App Register Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appLoginError(String s) {
        Toast.makeText(this, "App Login Error: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void appPhotoIDScanError(String s) {
        Toast.makeText(this, "App Photo ID Scan Error: "+ s, Toast.LENGTH_SHORT).show();
    }
}