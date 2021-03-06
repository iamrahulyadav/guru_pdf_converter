package tajcse37.createpdf.util;

import android.content.Context;

import com.itextpdf.text.Font;

import java.util.ArrayList;

import tajcse37.createpdf.R;
import tajcse37.createpdf.model.EnhancementOptionsEntity;

public class TextEnhancementOptionsUtils {

    public static ArrayList<EnhancementOptionsEntity> getEnhancementOptions(Context context,
                                                                            String fontTitle,
                                                                            Font.FontFamily fontFamily) {
        ArrayList<EnhancementOptionsEntity> options = new ArrayList<>();
        options.add(new EnhancementOptionsEntity(
                context.getResources().getDrawable(R.drawable.ic_font_black_24dp),
                fontTitle));
        options.add(new EnhancementOptionsEntity(
                context.getResources().getDrawable(R.drawable.ic_font_family_24dp),
                String.format(context.getString(R.string.default_font_family_text), fontFamily.name())));
        options.add(new EnhancementOptionsEntity(
                context.getResources().getDrawable(R.drawable.ic_page_size_24dp),
                context.getString(R.string.set_page_size_text)));
        return options;
    }
}
