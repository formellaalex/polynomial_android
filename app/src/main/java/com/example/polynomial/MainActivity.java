package com.example.polynomial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button submitButton;

    @BindView(R.id.textView5)
    TextView titleTextView;

    @BindView(R.id.editText1)
    EditText aEditText;

    @BindView(R.id.editText2)
    EditText bEditText;

    @BindView(R.id.editText3)
    EditText cEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    void submit() {

        final Intent graphActivity = new Intent(this, GraphActivity.class);
        final int a = Integer.parseInt(aEditText.getText().toString());
        final int b = Integer.parseInt(bEditText.getText().toString());
        final int c = Integer.parseInt(cEditText.getText().toString());

        graphActivity.putExtra(getResources().getString(R.string.a_factor_title), a);
        graphActivity.putExtra(getResources().getString(R.string.b_factor_title), b);
        graphActivity.putExtra(getResources().getString(R.string.c_factor_title), c);

        PackageManager packageManager = MainActivity.this.getPackageManager();
        if (graphActivity.resolveActivity(packageManager) != null) {
            startActivityForResult(graphActivity, 2);
        } else {
            Log.d("MAIN_ACTIVITY", "Cannot open the activity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            final String roots = data.getStringExtra(getResources().getString(R.string.intent_roots_param));
            titleTextView.setText(String.format("Found roots: %s", roots));
        }
    }
}