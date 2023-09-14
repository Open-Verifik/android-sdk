package co.verifik.verifikkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import co.verifik.verifikkit.databinding.ActivityMainBinding;
import co.verifik.verifikkit.models.ClickServiceInterface;
import co.verifik.verifikkit.models.VerifikService;

public class MainActivity extends AppCompatActivity implements ClickServiceInterface {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerifikService[] services = VerifikService.getAllVerifikServices(this);
        VerifikServiceAdapter adapter = new VerifikServiceAdapter(this, services);

        RecyclerView recyclerView = binding.recyclerviewVerifikservices;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void clickOnService(VerifikService service) {

        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra("verifik_service", service);
        startActivity(i);
    }
}