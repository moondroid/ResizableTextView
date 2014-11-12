package it.moondroid.resizabletextview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;

/**
 * Created by marco.granatiero on 12/11/2014.
 */
public class CropImageActivity extends Activity {

    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;

    private Bitmap croppedImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop);

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);

        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        final Button cropButton = (Button) findViewById(R.id.button_crop);
        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
//                ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
//                croppedImageView.setImageBitmap(croppedImage);
            }
        });
    }
}
