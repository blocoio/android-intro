package io.bloco.introduction;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

  public static final String EVENT_TITLE_EXTRA = "event_title";
  private static final int CAMERA_REQUEST_CODE = 101;
  public static final String FILES_AUTHORITY = "io.bloco.introduction.fileprovider";
  public static final String IMAGES_FOLDER = "images";
  public static final String IMAGE_FILE = "image.jpg";

  private ImageButton imageButton;
  private EditText messageText;
  private Button shareButton;
  private Uri photoUri;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share);

    imageButton = (ImageButton) findViewById(R.id.share_image);
    messageText = (EditText) findViewById(R.id.share_message);
    shareButton = (Button) findViewById(R.id.share_button);

    String eventTitle = getIntent().getStringExtra(EVENT_TITLE_EXTRA);
    setTitle(eventTitle);

    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getCameraPhoto();
      }
    });
    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        share();
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      setImageButtonUri();
    }
  }

  private void setImageButtonUri() {
    imageButton.setBackground(null);
    imageButton.setImageURI(photoUri);
    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
  }

  private void getCameraPhoto() {
    createPhotoUri();

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      grantAdditionalPermissions(takePictureIntent);
      startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    }
  }

  private void grantAdditionalPermissions(Intent takePictureIntent) {
    List<ResolveInfo>
        resInfoList = getPackageManager()
        .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
    for (ResolveInfo resolveInfo : resInfoList) {
      String packageName = resolveInfo.activityInfo.packageName;
      grantUriPermission(packageName, photoUri,
          Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
  }

  private void createPhotoUri() {
    File imageFolder = new File(getFilesDir(), IMAGES_FOLDER);
    imageFolder.mkdirs();

    File imageFile = new File(imageFolder, IMAGE_FILE);
    try {
      imageFile.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    photoUri = FileProvider.getUriForFile(this, FILES_AUTHORITY, imageFile);
  }

  private void share() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, messageText.getText().toString());
    if (photoUri != null) {
      intent.putExtra(Intent.EXTRA_STREAM, photoUri);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setDataAndType(photoUri, getContentResolver().getType(photoUri));
    } else {
      intent.setType("text/plain");
    }
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    }
  }
}
