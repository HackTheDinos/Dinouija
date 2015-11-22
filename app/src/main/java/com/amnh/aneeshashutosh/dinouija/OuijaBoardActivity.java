package com.amnh.aneeshashutosh.dinouija;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

import me.egorand.scrollableimageview.Orientation;
import me.egorand.scrollableimageview.ScrollableImageView;

public class OuijaBoardActivity extends AppCompatActivity implements Orientation.Listener {
    private Orientation mOrientation;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        mOrientation = new Orientation((SensorManager) getSystemService(Activity.SENSOR_SERVICE),
                getWindow().getWindowManager());
        new BitmapLoaderTask().execute();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientation.startListening(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientation.stopListening();
    }

    private void setImageBitmap(Bitmap bmp) {
        imageView = new ScrollableImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(bmp.getWidth(), bmp.getHeight()));
        imageView.setImageBitmap(bmp);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.addView(imageView, 0);
    }

    @Override
    public void onOrientationChanged(float pitch, float yaw) {
        if (imageView != null ) {
            Log.d("PITCH: ", "" + pitch);
            Log.d("YAW: ", "" + yaw);

            if (pitch > 120 && pitch < 180 || pitch < 360 && pitch > 300) {

            }
            else if (pitch < 120 && pitch > 0) {
                ((ScrollableImageView) imageView).translate("DOWN");
            }
            else if (pitch < 300 && pitch > 180) {
                ((ScrollableImageView) imageView).translate("UP");
            }
//            if (pitch < 270 || pitch > 90) {
//                ((ScrollableImageView) imageView).translate("DOWN");
//            }
//            if (pitch > 270 || pitch < 90) {
//                ((ScrollableImageView) imageView).translate("UP");
//            }
        }
    }

    private class BitmapLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private ProgressBar progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = (ProgressBar) findViewById(android.R.id.progress);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            try {
                InputStream bitmap = getAssets().open("ouijaboard.png");
                bmp = BitmapFactory.decodeStream(bitmap);
            } catch (IOException e) {
                Log.e("DEBUG", e.getMessage(), e);
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);
            setImageBitmap(result);
        }
    }
}
