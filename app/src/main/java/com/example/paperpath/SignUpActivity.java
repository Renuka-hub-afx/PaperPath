package com.example.paperpath;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

@SuppressWarnings("SpellCheckingInspection")
public class SignUpActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputLayout tilName;
    private TextInputLayout tilEmailSignUp;
    private TextInputLayout tilPasswordSignUp;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText etName;
    private TextInputEditText etEmailSignUp;
    private TextInputEditText etPasswordSignUp;
    private TextInputEditText etConfirmPassword;
    private MaterialCheckBox cbTerms;
    private MaterialButton btnSignUp;
    private TextView tvSignInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollViewSignUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tilName = findViewById(R.id.tilName);
        tilEmailSignUp = findViewById(R.id.tilEmailSignUp);
        tilPasswordSignUp = findViewById(R.id.tilPasswordSignUp);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etName = findViewById(R.id.etName);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        cbTerms = findViewById(R.id.cbTerms);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignInLink = findViewById(R.id.tvSignInLink);
    }

    private void setupListeners() {
        // Top Back Button
        btnBack.setOnClickListener(v -> finish());

        // Already have an account -> Sign In link
        tvSignInLink.setOnClickListener(v -> finish());

        // Sign Up Button Click
        btnSignUp.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        // Reset previous errors
        tilName.setError(null);
        tilEmailSignUp.setError(null);
        tilPasswordSignUp.setError(null);
        tilConfirmPassword.setError(null);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmailSignUp.getText() != null ? etEmailSignUp.getText().toString().trim() : "";
        String password = etPasswordSignUp.getText() != null ? etPasswordSignUp.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.err_empty_name));
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            tilEmailSignUp.setError(getString(R.string.err_empty_email));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmailSignUp.setError(getString(R.string.err_invalid_email));
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPasswordSignUp.setError(getString(R.string.err_empty_password));
            isValid = false;
        } else if (password.length() < 6) {
            tilPasswordSignUp.setError(getString(R.string.err_short_password));
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.err_empty_password));
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            tilConfirmPassword.setError(getString(R.string.err_password_mismatch));
            isValid = false;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, getString(R.string.err_accept_terms), Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, getString(R.string.success_sign_up), Toast.LENGTH_SHORT).show();
            // Proceed to Topic/Domain Selection to collect research interests
            Intent intent = new Intent(SignUpActivity.this, TopicSelectionActivity.class);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_NAME", name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
