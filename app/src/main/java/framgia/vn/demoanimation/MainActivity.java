package framgia.vn.demoanimation;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import framgia.vn.demoanimation.library.Constants;
import framgia.vn.demoanimation.library.Toaster;
import framgia.vn.demoanimation.library.UriToUrl;

public class MainActivity extends AppCompatActivity {
    private Animation animation;
    private RelativeLayout top_holder;
    private RelativeLayout bottom_holder;
    private RelativeLayout step_number;
    private Uri imageUri;
    private boolean click_status = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        top_holder = (RelativeLayout) findViewById(R.id.top_holder);
        bottom_holder = (RelativeLayout) findViewById(R.id.bottom_holder);
        step_number = (RelativeLayout) findViewById(R.id.step_number);
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
    protected void onStart() {
        overridePendingTransition(0, 0);
        flyIn();
        super.onStart();
    }

    @Override
    protected void onStop() {
        overridePendingTransition(0, 0);
        super.onStop();
    }

    public void startGallery(View view) {
        flyOut("displayGallery");
    }

    public void startCamera(View view) {
        flyOut("displayCamera");
    }

    @SuppressWarnings("unused")
    private void displayGallery() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Constants.REQUEST_GALLERY);
        } else {
            Toaster.make(getApplicationContext(), R.string.no_media);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CAMERA) {
            try{
                if (resultCode == RESULT_OK) {
                    displayPhotoActivity(1);
                } else {
                    UriToUrl.deleteUri(getApplicationContext(), imageUri);
                }
            } catch (Exception e) {
                Toaster.make(getApplicationContext(), R.string.error_img_not_found);
            }
        } else if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_GALLERY) {
            try {
                imageUri = data.getData();
                displayPhotoActivity(2);
            } catch (Exception e) {
                Toaster.make(getApplicationContext(), R.string.error_img_not_found);
            }
        }
    }

    @SuppressWarnings("unused")
    private void displayCamera() {
        imageUri = getOutputMediaFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private Uri getOutputMediaFile(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
        values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void displayPhotoActivity(int source_id) {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IMAGE_SOURCE, source_id);
        intent.setData(imageUri);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void flyOut(final String method_name) {
        if (click_status) {
            click_status = false;

            animation = AnimationUtils.loadAnimation(this, R.anim.step_number_back);
            step_number.startAnimation(animation);

            animation = AnimationUtils.loadAnimation(this, R.anim.holder_top_back);
            top_holder.startAnimation(animation);

            animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom_back);
            bottom_holder.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    callMethod(method_name);
                }
            });
        }
    }

    private void callMethod(String method_name) {
        if (method_name.equals("finish")) {
            overridePendingTransition(0, 0);
            finish();
        } else {
            try {
                Method method = getClass().getDeclaredMethod(method_name);
                method.invoke(this, new Object[] {});
            } catch (Exception e) {}
        }
    }

    @Override
    public void onBackPressed() {
        flyOut("finish");
        super.onBackPressed();
    }

    private void flyIn() {
        click_status = true;

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_top);
        top_holder.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom);
        bottom_holder.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.step_number);
        step_number.startAnimation(animation);
    }
}
