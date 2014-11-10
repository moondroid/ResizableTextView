package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.sephiroth.android.library.widget.HListView;


public class MainActivity extends Activity implements FontsFragment.OnFontSelectedListener{

    private String[] texts = new String[]{"one", "two", "three", "four", "five", "this is a long text"};

    private int currentFontId = 0;

    private FrameLayout container;
    private ArrayList<ResizableTextView> resizableTextViews = new ArrayList<ResizableTextView>();
    private ResizableTextView selectedResizableTextView;
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
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRandomNumber(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    private void addResizableTextView(){

        final ResizableTextView resizableTextView = new ResizableTextView(this);
        resizableTextView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        container.addView(resizableTextView);
        selectedResizableTextView = resizableTextView;

        resizableTextView.getTextView().setText(texts[getRandomNumber(0, texts.length-1)]);
        resizableTextView.setFontId(currentFontId);

        selectView(resizableTextView);

        resizableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do nothing
            }
        });


        resizableTextView.setOnResizableTextViewListener(new ResizableTextView.OnResizableTextViewListener() {

            @Override
            public void onTouched(ResizableTextView view) {
                deselectAll();
                selectView(view);
                selectedResizableTextView = view;
            }

            @Override
            public void onTranslationChanged(ResizableTextView view, float translationX, float translationY) {
                textViewTranslation.setText("x:"+translationX+" y:"+translationY);
            }

            @Override
            public void onSizeChanged(ResizableTextView view, float size) {
                textViewSize.setText("Size: "+size);
            }

            @Override
            public void onRotationChanged(ResizableTextView view, float rotation) {
                textViewRotation.setText("Rotation: "+rotation);
            }

            @Override
            public void onRemove(ResizableTextView view) {
                container.removeView(view);
                resizableTextViews.remove(view);
                Toast.makeText(MainActivity.this, "removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEdit(final ResizableTextView view) {
                final EditText input = new EditText(MainActivity.this);
                input.setText(view.getTextView().getText());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Update Text")
                                //.setMessage("Message")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                view.getTextView().setText(input.getText().toString());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });

        textViewSize.setText("Size: "+resizableTextView.getTextView().getTextSize());
        textViewRotation.setText("Rotation: "+resizableTextView.getRotation());
        textViewTranslation.setText("x:"+resizableTextView.getTranslationX()+" y:"+resizableTextView.getTranslationY());

        resizableTextViews.add(resizableTextView);
    }

    private void deselectAll(){
        for(ResizableTextView resizableTextView : resizableTextViews){
            if(resizableTextView.isEditingEnabled()){
                resizableTextView.setEditingEnabled(false);
            }
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FontsFragment fontsFragment = (FontsFragment) getFragmentManager().findFragmentByTag("FontsFragment");
        if (fontsFragment!=null){
            transaction.remove(fontsFragment).commitAllowingStateLoss();
        }
    }

    private void selectView(ResizableTextView view){
        view.setEditingEnabled(true);

        // Create a new Fragment to be placed in the activity layout
        FontsFragment fontsFragment = FontsFragment.newInstance(view.getFontId());
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fontsFragment, "FontsFragment").commit();

    }

    @Override
    public void onFontSelected(int fontId, Typeface typeface) {
        if (selectedResizableTextView!=null){
            currentFontId = fontId;
            selectedResizableTextView.setFontId(currentFontId);
        }
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
}
