package co.verifik.verifikkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import co.verifik.verifikkit.databinding.ActivityStartBinding;
import co.verifik.verifikkit.models.VerifikService;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerifikService service;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            service = getIntent().getSerializableExtra("verifik_service", VerifikService.class);
        }
        else{
            service = (VerifikService) getIntent().getSerializableExtra("verifik_service");
        }
        setTitle(service.getTitle());

        binding.textviewStep1.setText(service.getStep1());
        binding.textviewStep2.setText(service.getStep2());

        binding.continueButton.setOnClickListener(v -> {
            Intent i = new Intent(this,VerifikActivity.class);
            i.putExtra("verifik_service", service);
            startActivity(i);
        });
    }
}