package tajcse37.createpdf.model;

import android.net.Uri;

import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;

public class TextToPDFOptions extends PDFOptions {
    private final Uri mInFileUri;
    private final int mFontSize;
    private final Font.FontFamily mFontFamily;

    public TextToPDFOptions(String mFileName, Rectangle mPageSize, Uri mInFileUri, int mFontSize,
                            Font.FontFamily mFontFamily) {
        super(mFileName, mPageSize);
        this.mInFileUri = mInFileUri;
        this.mFontSize = mFontSize;
        this.mFontFamily = mFontFamily;
    }

    public Uri getInFileUri() {
        return mInFileUri;
    }

    public int getFontSize() {
        return mFontSize;
    }

    public Font.FontFamily getFontFamily() {
        return mFontFamily;
    }
}
