package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class OutlineTextView extends AppCompatTextView {
    private boolean hasStroke = false;
    private float mStrokeWidth = 0.0f;

   /* public OutlineTextView(Context context) {
        super(context);
    }*/

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /*
    public OutlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }*/

    public OutlineTextView(Context context, boolean textStroke, float textStrokeWidth) {
        super(context);
        hasStroke = textStroke;
        mStrokeWidth = textStrokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("test", "내용 : " + getText() + "좌표 : " + getX() + " " + getY() + " " +
                " " + getX()+getWidth() + " " + getY()+getHeight());
        if (hasStroke) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mStrokeWidth);
            canvas.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(), paint);

            /* 텍스트 뷰가 아니라 텍스트가 인식됨
            getPaint().setColor(Color.BLACK);
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setStrokeWidth(mStrokeWidth);
            */
            super.onDraw(canvas);
        }
        else {
            super.onDraw(canvas);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView);
        hasStroke = typeArray.getBoolean(R.styleable.OutlineTextView_textStroke, false);
        mStrokeWidth = typeArray.getFloat(R.styleable.OutlineTextView_textStrokeWidth, 0.0f);
    }

    public void setHasStroke(boolean textStroke){
        hasStroke = textStroke;
    }

    public void setmStrokeWidth(float textStrokeWidth){
        mStrokeWidth = textStrokeWidth;
    }
}
