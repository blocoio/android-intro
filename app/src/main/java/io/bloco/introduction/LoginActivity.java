package io.bloco.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

  private Button nextButton;
  private EditText nameText;
  private Preferences preferences;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    preferences = new Preferences(this);
    nextButton = (Button) findViewById(R.id.login_next);
    nameText = (EditText) findViewById(R.id.login_name);

    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String name = nameText.getText().toString();
        openNextActivity(name);
      }
    });
  }

  private void openNextActivity(String name) {
    if (name.trim().isEmpty()) {
      Toast.makeText(this, R.string.name_missing, Toast.LENGTH_LONG).show();
    } else {

      preferences.saveName(name);
      Intent intent = new Intent(this, EventsActivity.class);
      startActivity(intent);
      finish();
    }
  }
}
