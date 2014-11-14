package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends Activity implements FontsFragment.OnFontSelectedListener, PaletteFragment.OnColorSelectedListener{

    private static final int REQUEST_CODE_SELECT_PICTURE = 1;

    private String[] texts = new String[]{"one", "two", "three", "four", "five", "this is a long text"};

    private int currentFontId = 0;
    private int currentColorId = 0;
    private int fileCounter = 0;

    private FrameLayout container;
    private ArrayList<ResizableLayout> resizableTextViews = new ArrayList<ResizableLayout>();
    private ResizableLayout selectedResizableTextView;
    private TextView textViewSize, textViewRotation, textViewTranslation;
    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout)findViewById(R.id.container);
        backgroundImage = (ImageView)findViewById(R.id.background_image);

        textViewSize = (TextView)findViewById(R.id.textViewSize);
        textViewRotation = (TextView)findViewById(R.id.textViewRotation);
        textViewTranslation = (TextView)findViewById(R.id.textViewTranslation);

        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
            }
        });
        backgroundImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), REQUEST_CODE_SELECT_PICTURE);
                return true;
            }
        });

        handleSendImage();

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

        switch (id){
            case R.id.action_settings:
                return true;

            case R.id.action_add:
                deselectAll();
                addResizableTextView();
                return true;

            case R.id.action_save:
                deselectAll();
                Uri imageSavedUri = saveViewToFile(container);
                if(imageSavedUri!=null){
                    Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.action_share:
                deselectAll();
                Uri imageShareUri = saveViewToFile(container);
                if (imageShareUri!=null){
                    shareImage(imageShareUri);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRandomNumber(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    private void addResizableTextView(){

        //final ResizableTextView resizableView = new ResizableTextView(this);
        final ResizableImageView resizableView = new ResizableImageView(this);
        resizableView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        container.addView(resizableView);
        selectedResizableTextView = resizableView;

        //resizableTextView.getTextView().setText(texts[getRandomNumber(0, texts.length-1)]);
//        resizableTextView.setFontId(currentFontId);
//        resizableTextView.setColorId(currentColorId);

        selectView(resizableView);

        resizableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do nothing
            }
        });


        resizableView.setOnResizableTextViewListener(new ResizableLayout.OnResizableTextViewListener() {

            @Override
            public void onTouched(ResizableLayout view) {
                deselectAll();
                selectView(view);
                selectedResizableTextView = view;
            }

            @Override
            public void onTranslationChanged(ResizableLayout view, float translationX, float translationY) {
                textViewTranslation.setText("x:" + translationX + " y:" + translationY);
            }

            @Override
            public void onSizeChanged(ResizableLayout view, float size) {
                textViewSize.setText("Size: " + size);
            }

            @Override
            public void onRotationChanged(ResizableLayout view, float rotation) {
                textViewRotation.setText("Rotation: " + rotation);
            }

            @Override
            public void onRemove(ResizableLayout view) {
                container.removeView(view);
                resizableTextViews.remove(view);
                Toast.makeText(MainActivity.this, "removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEdit(final ResizableLayout view) {
                final EditText input = new EditText(MainActivity.this);
                //input.setText(view.getTextView().getText());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Update Text")
                                //.setMessage("Message")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //view.getTextView().setText(input.getText().toString());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });

        //textViewSize.setText("Size: "+resizableTextView.getTextView().getTextSize());
        textViewRotation.setText("Rotation: " + resizableView.getRotation());
        textViewTranslation.setText("x:"+resizableView.getTranslationX()+" y:"+resizableView.getTranslationY());

        resizableTextViews.add(resizableView);
    }

    private void deselectAll(){
        for(ResizableLayout resizableTextView : resizableTextViews){
            if(resizableTextView.isEditingEnabled()){
                resizableTextView.setEditingEnabled(false);
            }
        }


        EffectsMenuFragment effectsMenuFragment = (EffectsMenuFragment) getFragmentManager().findFragmentByTag("EffectsMenuFragment");
        if (effectsMenuFragment!=null){
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(effectsMenuFragment).commit();
        }
    }

    private void selectView(ResizableLayout view){
        view.setEditingEnabled(true);

        // Create a new Fragment to be placed in the activity layout
        EffectsMenuFragment effectsMenuFragment = new EffectsMenuFragment();
        //effectsMenuFragment.setResizableItem(view);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, effectsMenuFragment, "EffectsMenuFragment").commit();
    }

    @Override
    public void onFontSelected(int fontId, Typeface typeface) {

        currentFontId = fontId;
    }

    @Override
    public void onColorSelected(int colorId) {

        currentColorId = colorId;
    }

    private void handleSendImage() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Log.d("MainActivity.handleSendImage", "action: "+action);
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) getIntent().getParcelableExtra("android.intent.extra.STREAM");
                if(imageUri != null){
                    Log.d("MainActivity.handleSendImage", "uri: "+imageUri.toString());
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        backgroundImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.e("MainActivity.handleSendImage.getBitmap", "IOException: "+e);
                    }
                }
            }
        }
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Uri saveViewToFile(View view){

        Uri returnUri = null;
        String appDirectoryName = getResources().getString(R.string.app_name);
        String path = Environment.getExternalStorageDirectory().toString()+ File.separator + appDirectoryName + File.separator;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        OutputStream fOut = null;
        String title = getResources().getString(R.string.app_name)+"_"+fileCounter;
        File file = new File(path, title+".jpg"); // the File to save to
        try {
            fOut = new FileOutputStream(file);
            Bitmap pictureBitmap = getBitmapFromView(view); // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream

//            MediaStore.Images.Media.insertImage(getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), file.getName());

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, title);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, appDirectoryName);
            values.put("_data", file.getAbsolutePath());

            ContentResolver cr = getContentResolver();
            returnUri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            fileCounter++;

            Log.i("MainActivity.saveViewToFile", "saved "+file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("MainActivity.saveViewToFile", "IOException: " + e);
        }finally {
            //
        }

        return returnUri;
    }

    private void shareImage(Uri uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "text");
        //intent.putExtra(Intent.EXTRA_TITLE, "title");
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri!=null){
                        Log.d("MainActivity.onActivityResult", "uri: "+selectedImageUri.toString());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            backgroundImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            Log.e("MainActivity.onActivityResult.getBitmap", "IOException: "+e);
                        }
                }

            }
        }
    }


}
