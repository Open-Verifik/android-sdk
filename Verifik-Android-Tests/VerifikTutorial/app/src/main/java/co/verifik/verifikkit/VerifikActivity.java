package co.verifik.verifikkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import co.mat.verifikkit.Verifik;
import co.mat.verifikkit.VerifikCallback;
import co.verifik.verifikkit.databinding.ActivityVerifikBinding;
import co.verifik.verifikkit.models.VerifikService;

public class VerifikActivity extends AppCompatActivity implements VerifikCallback {
    private ActivityVerifikBinding binding;
    private Verifik verifik;
    private String tok = "";
    private boolean initVerifik = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifikBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerifikService service;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            service = getIntent().getSerializableExtra("verifik_service", VerifikService.class);
        }
        else{
            service = (VerifikService) getIntent().getSerializableExtra("verifik_service");
        }

        verifik = new Verifik(this,tok, this);
        setTitle(service.getTitle());


        switch (service.getVerifikType()) {
            case LIVENESS:
                break;
            case REGISTER:
                binding.startButton.setOnClickListener(v -> {
                    if(initVerifik) {
                        verifik.enroll("0001");
                    }
                });
                break;
            case LOGIN:
                binding.startButton.setOnClickListener(v -> {
                    if(initVerifik) {
                        verifik.authenticate("0001");
                    }
                });
                break;
            case MATCHID:
                binding.startButton.setOnClickListener(v -> {
                    if(initVerifik) {
                        verifik.matchIDScan("0001");
                    }
                });
                break;
            case OCR:
                binding.startButton.setOnClickListener(v -> {
                    if(initVerifik) {
                        verifik.photoIDScan();
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    public void initVerifikSuccess() {
        initVerifik = true;
    }

    @Override
    public void appRegisterSuccessful(String s) {

    }

    @Override
    public void appLoginSuccessful(String s) {

    }

    @Override
    public void appPhotoIDScanSuccessful(String s) {

    }

    @Override
    public void configError(String s) {
        Log.d("Config Error",s);
    }

    @Override
    public void sessionError(String s) {
        Log.d("Session Error",s);
    }

    @Override
    public void enrollmentError(String s) {

    }

    @Override
    public void authError(String s) {

    }

    @Override
    public void photoIDMatchError(String s) {

    }

    @Override
    public void photoIDScanError(String s) {

    }

    @Override
    public void appRegisterError(String s) {

    }

    @Override
    public void appLoginError(String s) {

    }

    @Override
    public void appPhotoIDScanError(String s) {

    }
}