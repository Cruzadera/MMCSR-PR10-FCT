package es.iessaladillo.maria.mmcsr_pr10_fct.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.iessaladillo.maria.mmcsr_pr10_fct.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
