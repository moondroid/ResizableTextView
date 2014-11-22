package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import it.moondroid.resizabletextview.drawables.CircleDrawable;
import it.moondroid.resizabletextview.drawables.RectDrawable;
import it.moondroid.resizabletextview.drawables.StarDrawable;

/**
 * Created by marco.granatiero on 10/11/2014.
 */
public class Assets {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public static enum Effect {
        FONT (R.drawable.ic_text_format, R.string.effect_font, FontsFragment.class),
        COLOR (R.drawable.ic_image_palette, R.string.effect_color, PaletteFragment.class),
        SHAPE (R.drawable.ic_drawables, R.string.effect_color, DrawablesFragment.class);

        public final int iconId;
        public final int labelId;
        public final Class fragmentClass;

        Effect(int iconId, int labelId, Class fragmentClass) {
            this.iconId = iconId;
            this.labelId = labelId;
            this.fragmentClass = fragmentClass;
        }
    };

    public static enum ItemType {
        BACKGROUND (new Effect[]{Effect.COLOR}),
        RESIZABLE_TEXTVIEW (new Effect[]{Effect.FONT, Effect.COLOR}),
        RESIZABLE_DRAWABLE (new Effect[]{Effect.SHAPE, Effect.COLOR});

        public final Effect[] effects;

        ItemType (Effect[] effects) {
            this.effects = effects;
        }
    }

    public static ArrayList<Effect> effects = new ArrayList<Effect>();
    public static ArrayList<String> fonts = new ArrayList<String>();
    public static ArrayList<Integer> colors = new ArrayList<Integer>();
    public static ArrayList<String> stickers = new ArrayList<String>();
    public static ArrayList<Class<? extends Drawable>> drawables = new ArrayList<Class<? extends Drawable>>();

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

        colors.add(R.color.red_500);
        colors.add(R.color.pink_500);
        colors.add(R.color.purple_500);
        colors.add(R.color.dark_purple_500);
        colors.add(R.color.blue_500);
        colors.add(R.color.light_blue_500);
        colors.add(R.color.cyan_500);
        colors.add(R.color.teal_500);
        colors.add(R.color.green_500);
        colors.add(R.color.light_green_500);
        colors.add(R.color.lime_500);
        colors.add(R.color.yellow_500);
        colors.add(R.color.amber_500);
        colors.add(R.color.orange_500);
        colors.add(R.color.deep_orange_500);
        colors.add(R.color.brown_500);
        colors.add(R.color.grey_500);
        colors.add(R.color.blue_grey_500);
        colors.add(R.color.black);
        colors.add(R.color.white);

        for( int i = 0; i < 24; i++ ) {
            String stickerPath = "stickers/animal_"+String.format("%04d", i)+".png";
            Log.d("Assets", stickerPath);
            stickers.add( stickerPath );
        }

        drawables.add(RectDrawable.class);
        drawables.add(CircleDrawable.class);
        drawables.add(StarDrawable.class);
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

    public static Bitmap addShadow(Bitmap originalBitmap){

        Bitmap shadowBitmap = getShadow(originalBitmap);
        Canvas c = new Canvas(shadowBitmap);
        c.drawBitmap(originalBitmap, 0, 0, null);

        return shadowBitmap;
    }

    public static Bitmap getShadow(Bitmap originalBitmap){
        BlurMaskFilter blurFilter = new BlurMaskFilter(4, BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowImage = originalBitmap.extractAlpha(shadowPaint, offsetXY);
        Bitmap shadowImage32 = shadowImage.copy(Bitmap.Config.ARGB_8888, true);

        Canvas c = new Canvas(shadowImage32);
        int opacity = 125;
        int colour = (opacity & 0xFF) << 24;
        c.drawColor(colour, PorterDuff.Mode.DST_IN);
        //c.drawBitmap(shadowImage32, 0, 0, new Paint(Color.GRAY));
        //c.drawBitmap(originalBitmap, -offsetXY[0], -offsetXY[1], null);

        return shadowImage32;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable, int defaultWidth, int defaultHeight) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            int width = drawable.getIntrinsicWidth();
            width = width > 0 ? width : defaultWidth;
            int height = drawable.getIntrinsicHeight();
            height = height > 0 ? height : defaultHeight;
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

//            if (drawable instanceof ColorDrawable) {
//                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
//            } else {
//                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
//            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Drawable getDrawable(int drawableId, int width, int height){
        Drawable drawable = null;
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawable = drawables.get(drawableId).getConstructor(Bitmap.class).newInstance(bitmap);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return drawable;
    }
}
