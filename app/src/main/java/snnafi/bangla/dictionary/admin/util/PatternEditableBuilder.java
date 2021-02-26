package snnafi.bangla.dictionary.admin.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import snnafi.bangla.dictionary.admin.R;

/*
     Create clickable spans within a TextView
     made easy with pattern matching!
     Created by: Nathan Esquenazi
     Usage 1: Apply spannable strings to a TextView based on pattern
        new PatternEditableBuilder().
           addPattern(Pattern.compile("\\@(\\w+)")).
           into(textView);
     Usage 2: Apply clickable spans to a TextView
         new PatternEditableBuilder().
             addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
             new PatternEditableBuilder.SpannableClickedListener() {
                @Override
                public void onSpanClicked(String text) {
                    // Do something here
                }
             }).into(textView);
     See README for more details.
 */

public class PatternEditableBuilder {
    // Records the pattern spans to apply to a TextView
    ArrayList<SpannablePatternItem> patterns;
    Context context;
    private static final String TAG = "PatternEditableBuilder";

    /* This stores a particular pattern item
           complete with pattern, span styles, and click listener */
    public class SpannablePatternItem {
        public SpannablePatternItem(Pattern pattern, SpannableStyleListener styles, SpannableClickedListener listener) {
            this.pattern = pattern;
            this.styles = styles;
            this.listener = listener;
        }

        public SpannableStyleListener styles;
        public Pattern pattern;
        public SpannableClickedListener listener;
    }

    /* This stores the style listener for a pattern item
       Used to style a particular category of spans */
    public static abstract class SpannableStyleListener {
        public int spanTextColor;

        public SpannableStyleListener() {
        }

        public SpannableStyleListener(int spanTextColor) {
            this.spanTextColor = spanTextColor;
        }

        abstract void onSpanStyled(TextPaint ds) throws NoSuchFieldException, IllegalAccessException;
    }

    /* This stores the click listener for a pattern item
       Used to handle clicks to a particular category of spans */
    public interface SpannableClickedListener {
        void onSpanClicked(String text);
    }

    /* This is the custom clickable span class used
       to handle user clicks to our pattern spans
       applying the styles and invoking click listener.
     */
    public class StyledClickableSpan extends ClickableSpan {
        SpannablePatternItem item;

        public StyledClickableSpan(SpannablePatternItem item) {
            this.item = item;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (item.styles != null) {
                try {
                    item.styles.onSpanStyled(ds);
                } catch (Exception e) {
                    Log.d(TAG, "updateDrawState: " + e.getLocalizedMessage());
                }
            }
            super.updateDrawState(ds);
            ds.setUnderlineText(true);


        }


        @Override
        public void onClick(View widget) {
            if (item.listener != null) {
                TextView tv = (TextView) widget;
                Spanned span = (Spanned) tv.getText();
                int start = span.getSpanStart(this);
                int end = span.getSpanEnd(this);
                CharSequence text = span.subSequence(start, end);
                item.listener.onSpanClicked(text.toString());
            }
            widget.invalidate();
        }
    }

    /* ----- Constructors ------- */
    public PatternEditableBuilder(Context context) {
        this.context = context;
        this.patterns = new ArrayList<>();
    }

    /* These are the `addPattern` overloaded signatures */
    // Each allows us to add a span pattern with different arguments
    public PatternEditableBuilder addPattern(Pattern pattern, SpannableStyleListener spanStyles, SpannableClickedListener listener) {
        patterns.add(new SpannablePatternItem(pattern, spanStyles, listener));
        return this;
    }

    public PatternEditableBuilder addPattern(Pattern pattern, SpannableStyleListener spanStyles) {
        addPattern(pattern, spanStyles, null);
        return this;
    }

    public PatternEditableBuilder addPattern(Pattern pattern) {
        addPattern(pattern, null, null);
        return this;
    }


    public PatternEditableBuilder addPattern(Pattern pattern, int textColor) {
        addPattern(pattern, textColor, null);
        return this;
    }

    public PatternEditableBuilder addPattern(Pattern pattern, int textColor, SpannableClickedListener listener) {
        SpannableStyleListener styles = new SpannableStyleListener(textColor) {
            @Override
            public void onSpanStyled(TextPaint ds) throws NoSuchFieldException {
                ds.linkColor = this.spanTextColor;

            }
        };
        addPattern(pattern, styles, listener);
        return this;
    }

    public PatternEditableBuilder addPattern(Pattern pattern, int textColor, int underlineColor, SpannableClickedListener listener) {
        SpannableStyleListener styles = new SpannableStyleListener(textColor) {
            @Override
            public void onSpanStyled(TextPaint ds) throws NoSuchFieldException, IllegalAccessException {
                ds.linkColor = this.spanTextColor;
                Field field = TextPaint.class.getDeclaredField("underlineColor");
                field.setAccessible(true);
                field.setInt(ds, underlineColor);


            }
        };
        addPattern(pattern, styles, listener);
        return this;
    }

    public PatternEditableBuilder addPattern(Pattern pattern, SpannableClickedListener listener) {
        addPattern(pattern, context.getResources().getColor(R.color.text_color), context.getResources().getColor(R.color.link_text), listener);
        return this;
    }

    /* BUILDER METHODS */

    // This builds the pattern span and applies to a TextView
    public void into(TextView textView) {
        SpannableStringBuilder result = build(textView.getText());
        replaceAll(result, Pattern.compile("\\{"), "");
        replaceAll(result, Pattern.compile("\\}"), "");
        replaceAll(result, Pattern.compile("\\["), "");
        replaceAll(result, Pattern.compile("\\]"), "");
        replaceAll(result, Pattern.compile("\\#"), "");
        replaceAll(result, Pattern.compile("\\@"), "");
        textView.setText(result);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // This builds the pattern span into a `SpannableStringBuilder`
    // Requires a CharSequence to be passed in to be applied to
    public SpannableStringBuilder build(CharSequence editable) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(editable);
        for (int i = 0; i < patterns.size(); i++) {
            if (i == 0) {
                Matcher matcher = patterns.get(i).pattern.matcher(ssb);
                while (matcher.find()) {
                    int start = matcher.start() + 1;
                    int end = matcher.end() - 1;
                    ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
                }
            } else if (i == 1) {
                Matcher matcher = patterns.get(i).pattern.matcher(ssb);
                while (matcher.find()) {
                    int start = matcher.start() + 1;
                    int end = matcher.end() - 1;
                    ssb.setSpan(new FontSpan(Typeface.createFromAsset(context.getAssets(), Constant.INSTANCE.getAPP_FONT_ITALIC()), FontSpan.FontStyle.ITALIC), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            } else if (i == 2) {
                Matcher matcher = patterns.get(i).pattern.matcher(ssb);
                while (matcher.find()) {
                    int start = matcher.start() + 1;
                    int end = matcher.end() - 1;
                    ssb.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new RoundedBackgroundSpan(context), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new FontSpan(Typeface.createFromAsset(context.getAssets(), Constant.INSTANCE.getAPP_FONT()), FontSpan.FontStyle.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

            } else if (i == 3) {
                Matcher matcher = patterns.get(i).pattern.matcher(ssb);
                while (matcher.find()) {
                    int start = matcher.start() + 1;
                    int end = matcher.end() - 1;
                    ssb.setSpan(new StyledClickableSpan(patterns.get(i)), start, end, 0);


                }

            }
        }
        return ssb;
    }

    private static void replaceAll(SpannableStringBuilder sb, Pattern pattern, String replacement) {
        Matcher m = pattern.matcher(sb);
        int extra = 0;
        while (m.find()) {
            sb.replace(m.start() - extra, m.end() - extra, replacement);
            ++extra;

        }
    }

}

class RoundedBackgroundSpan extends ReplacementSpan {

    private int backgroundColor = 0;
    private int textColor = 0;

    public RoundedBackgroundSpan(Context context) {
        super();
        backgroundColor = context.getResources().getColor(R.color.highlight);
        textColor = context.getResources().getColor(R.color.white);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(x, top, x + measureText(paint, text, start, end) + 20, bottom);
        paint.setColor(backgroundColor);
        int CORNER_RADIUS = 5;
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x + 10, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}

