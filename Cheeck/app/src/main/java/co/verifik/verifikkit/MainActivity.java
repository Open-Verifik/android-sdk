package co.verifik.verifikkit;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import co.mat.verifikkit.VerifikActivity;
import co.verifik.verifikkit.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;

import co.mat.verifikkit.Verifik;

public class MainActivity extends VerifikActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Verifik verifik;
    private Boolean initVerifik = false;
    private String refId = "VerifikSample"+ UUID.randomUUID().toString();
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJZCI6IjYxNTc3MTU2OTBmMDEwOGNmMmRjNjI4MSIsImRvY3VtZW50VHlwZSI6IkNDIiwiZG9jdW1lbnROdW1iZXIiOiIxNjM1MzczMzY3NDY3NDMiLCJ2IjoxLCJyb2xlIjoiY2xpZW50IiwiZXhwaXJlc0F0IjoiMjAyMi0xMi0wNCAxOTozNjo1NSIsImlhdCI6MTY2NzU5MDYxNX0.QvyQyTXoQCzXlGGfBs2brK15_9AvoveFWTAgprHvRDc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        verifik = new Verifik(this, token);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", view1 -> {
                            if(initVerifik) {
                                //verifik.enroll(refId);
                                verifik.photoIDScan();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void initializationSuccesful() {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, "Que exitoso!", Toast.LENGTH_SHORT).show();
        });

        initVerifik = true;
    }

    @Override
    public void configError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void sessionError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void enrollmentError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void authError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void photoIDMatchError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void photoIDScanError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }
}