package it.moondroid.resizabletextview;

import android.graphics.Color;

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

    }



}
