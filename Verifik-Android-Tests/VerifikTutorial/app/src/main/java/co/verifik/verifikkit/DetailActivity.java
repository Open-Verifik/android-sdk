package co.verifik.verifikkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import co.verifik.verifikkit.databinding.ActivityDetailBinding;
import co.verifik.verifikkit.databinding.ActivityMainBinding;
import co.verifik.verifikkit.models.VerifikService;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerifikService service;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            service = getIntent().getSerializableExtra("verifik_service", VerifikService.class);
        }
        else{
            service = (VerifikService) getIntent().getSerializableExtra("verifik_service");
        }
        setTitle(service.getTitle());
        binding.textviewDescription.setText(service.getDescription());
        binding.textviewParams.setText(service.getParameters());
        binding.textviewCriteria.setText(service.getSuccessCriteria());

        binding.continueButton.setOnClickListener(v -> {
            Intent i = new Intent(this,StartActivity.class);
            i.putExtra("verifik_service", service);
            startActivity(i);
        });

    }
}