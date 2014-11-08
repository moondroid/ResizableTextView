package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity {

    private String[] texts = new String[]{"one", "two", "three", "four", "five", "this is a long text"};
    private String[] fonts = new String[]{"fonts/fontastique.ttf", "fonts/comeback_of_the_damned.otf",
            "fonts/dead_font_walking.otf", "fonts/wolfganger.otf"};


    private int index = 0;

    private FrameLayout container;
    private ArrayList<ResizableTextView> resizableTextViews = new ArrayList<ResizableTextView>();
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
                for(ResizableTextView resizableTextView : resizableTextViews){
                    if(resizableTextView.isEditingEnabled()){
                        resizableTextView.setEditingEnabled(false);
                    }
                }
            }
        });
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

        resizableTextView.getTextView().setText(texts[getRandomNumber(0, texts.length-1)]);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fonts[getRandomNumber(0, fonts.length-1)]);
        resizableTextView.getTextView().setTypeface(typeface);

        resizableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                index++;
//                if (index >= texts.length) {
//                    index = 0;
//                }
//
//                resizableTextView.getTextView().setText(texts[index]);
//                Typeface typeface = Typeface.createFromAsset(getAssets(), fonts[getRandomNumber(0, fonts.length - 1)]);
//                resizableTextView.getTextView().setTypeface(typeface);
            }
        });


        resizableTextView.setOnResizableTextViewListener(new ResizableTextView.OnResizableTextViewListener() {
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
                Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT).show();
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


}
