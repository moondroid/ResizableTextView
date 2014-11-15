package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by marco.granatiero on 10/11/2014.
 */
public class Assets {

    public static enum Effect {
        FONT (R.drawable.ic_text_format, R.string.effect_font, FontsFragment.class),
        COLOR (R.drawable.ic_image_palette, R.string.effect_color, PaletteFragment.class);

        public final int iconId;
        public final int labelId;
        public final Class fragmentClass;

        Effect(int iconId, int labelId, Class fragmentClass) {
            this.iconId = iconId;
            this.labelId = labelId;
            this.fragmentClass = fragmentClass;
        }
    };

    public static ArrayList<Effect> effects = new ArrayList<Effect>();
    public static ArrayList<String> fonts = new ArrayList<String>();
    public static ArrayList<Integer> colors = new ArrayList<Integer>();
    public static ArrayList<String> stickers = new ArrayList<String>();

    static {
        effects.add(Effect.FONT);
        effects.add(Effect.COLOR);

        fonts.add("fonts/simple_001.ttf");
        fonts.add("fonts/simple_002.ttf");
        fonts.add("fonts/simple_006.ttf");
        fonts.add("fonts/simple_007.ttf");
        fonts.add("fonts/simple_008.ttf");
        fonts.add("fonts/simple_010.ttf");
        fonts.add("fonts/simple_011.ttf");
        fonts.add("fonts/simple_012.ttf");
        fonts.add("fonts/simple_013.ttf");
        fonts.add("fonts/simple_015.ttf");
        fonts.add("fonts/simple_016.ttf");
        fonts.add("fonts/simple_018.ttf");
        fonts.add("fonts/simple_019.ttf");
        fonts.add("fonts/simple_020.ttf");
        fonts.add("fonts/simple_029.ttf");

        colors.add(Color.RED);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.LTGRAY);
        colors.add(Color.GRAY);
        colors.add(Color.DKGRAY);
        colors.add(Color.BLACK);

        //stickers.add(R.drawable.sticker_0009);


        for( int i = 0; i < 24; i++ ) {
            String stickerPath = "stickers/animal_"+String.format("%04d", i)+".png";
            Log.d("Assets", stickerPath);
            stickers.add( stickerPath );
        }
    }

    public static Bitmap getBitmapFromAsset(Context context, String file) {
        Bitmap bitmap;

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;

        try {
            InputStream is = context.getAssets().open(file);
            bitmap = BitmapFactory.decodeStream(is, null, op);

            return bitmap;

        }catch (IOException ex){
            Log.e("Assets.getBitmapFromAsset", "IOException: "+ex);
        }

        return null;
    }


}
