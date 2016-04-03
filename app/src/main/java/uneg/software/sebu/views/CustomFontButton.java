package uneg.software.sebu.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import uneg.software.sebu.R;

public class CustomFontButton extends Button {

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontButton(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
            String fontName = a.getString(R.styleable.CustomFontTextView_fontName);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

}