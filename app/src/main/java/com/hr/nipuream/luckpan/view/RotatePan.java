package com.hr.nipuream.luckpan.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hr.nipuream.luckpan.R;
import com.hr.nipuream.luckpan.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：Nipuream
 * 时间: 2016-08-16 10:18
 * 邮箱：571829491@qq.com
 */
public class RotatePan extends View {

    private final int destValue;
    private Context context;

    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //    private int InitAngle = 30;
    private int InitAngle;
    private int radius = 0;

    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private int[] images = new int[]{R.mipmap.huawei, R.mipmap.image_one, R.mipmap.iphone, R.mipmap.macbook, R.mipmap.meizu, R.mipmap.xiaomi};
    private String[] strs = {"波依定","依姆多","黛力新","喜辽妥","康哲药业","波依定","依姆多","黛力新","喜辽妥","康哲药业"};
    private List<Bitmap> bitmaps = new ArrayList<>();
    private GestureDetectorCompat mDetector;
    private ScrollerCompat scroller;

    private int unitAngle;
    private int totalSize;

    //旋转一圈所需要的时间
    private static long ONE_WHEEL_TIME = 500;

    public RotatePan(Context context) {
        this(context, null);
    }

    public RotatePan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatePan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        mDetector = new GestureDetectorCompat(context, new RotatePanGestureListener());
        scroller = ScrollerCompat.create(context);

        dPaint.setColor(Color.rgb(255, 133, 132));
        sPaint.setColor(Color.rgb(254, 104, 105));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Util.dip2px(context, 30));
        setClickable(true);

//        添加默认图片
//        for(int i=0;i<6;i++){
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[i]);
//            bitmaps.add(bitmap);
//        }

        totalSize = 8;
        unitAngle = 360 / totalSize;
        InitAngle = unitAngle / 2;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels - getStatusHeight(context);
        int screenHeight = displayMetrics.heightPixels;
        destValue = Math.min(screenHeight, screenWidth) - Util.dip2px(context, 160);
    }

    private int getStatusHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果是wrap——content，则默认值为300dp
        int mHeight = destValue;
        int mWidth = destValue;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int MinValue = Math.min(width, height);
        radius = MinValue / 2;
        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), width, height);
//        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), MinValue, MinValue);

        int angle = InitAngle;
        for (int i = 0; i < totalSize; i++) {
//            if (i % 2 == 0) {
//                canvas.drawArc(rectF, angle, unitAngle, true, dPaint);
//            } else {
//                canvas.drawArc(rectF, angle, unitAngle, true, sPaint);
//            }
            setPaintColor(i,dPaint);
            canvas.drawArc(rectF, angle, unitAngle, true, dPaint);
            angle += unitAngle;
        }

//        for (int i = 0; i < 6; i++) {
//            drawIcon(width / 2, height / 2, radius, InitAngle, i, canvas);
//            InitAngle += 60;
//        }

        for (int i = 0; i < totalSize; i++) {
            drawText(InitAngle, strs[i], 2 * radius, textPaint, canvas, rectF);
            InitAngle += unitAngle;
        }

    }

    private void setPaintColor(int i,Paint paint) {
        switch (i % 6) {
            case 0:
                paint.setColor(this.getContext().getResources().getColor(R.color.purple_color));
                break;
            case 1:
                paint.setColor(this.getContext().getResources().getColor(R.color.yellow_color));
                break;
            case 2:
                paint.setColor(this.getContext().getResources().getColor(R.color.blue_color));
                break;
            case 3:
                paint.setColor(this.getContext().getResources().getColor(R.color.red_color));
                break;
            case 4:
                paint.setColor(this.getContext().getResources().getColor(R.color.dark_blue_color));
                break;
            case 5:
                paint.setColor(this.getContext().getResources().getColor(R.color.green_color));
                break;
        }
    }

    private void drawText(float startAngle, String string, int mRadius, Paint mTextPaint, Canvas mCanvas, RectF mRange) {
        Path path = new Path();
        path.addArc(mRange, startAngle, unitAngle);
        float textWidth = mTextPaint.measureText(string);

//        float hOffset = (float) (mRadius * Math.PI / 6 / 2 - textWidth / 2);
        float hOffset = (float) (mRadius * Math.PI * unitAngle / 720 - textWidth / 2);
        float vOffset = mRadius / 2 / 4;
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

//    private void drawIcon(int xx, int yy, int mRadius, float startAngle, int i, Canvas mCanvas) {
//
//        int imgWidth = mRadius / 4;
//
//        float angle = (float) Math.toRadians(60 + startAngle);
//
//        float x = (float) (xx + mRadius / 2 * Math.cos(angle));
//        float y = (float) (yy + mRadius / 2 * Math.sin(angle));
//
//        // 确定绘制图片的位置
//        RectF rect = new RectF(x - imgWidth * 3 / 4, y - imgWidth * 3 / 4, x + imgWidth
//                * 3 / 4, y + imgWidth * 3 / 4);
//
//
//        //画出图标
//        if (bitmaps != null && bitmaps.size() > 0) {
//            Bitmap bitmap = bitmaps.get(i);
//            mCanvas.drawBitmap(bitmap, null, rect, null);
//        }
//    }

    /**
     * 设置转盘的格数
     *
     * @param count
     */
    public void setItemCount(int count) {
        this.totalSize = count;
        this.unitAngle = 360 / totalSize;
        InitAngle = unitAngle / 2;
        this.invalidate();
    }

    /**
     * 给每个项目设置图标
     *
     * @param bitmaps
     */
    public void setImages(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        this.invalidate();
    }

    public void setStr(String... strs) {
        this.strs = strs;
        this.invalidate();
    }

    public void setPearCircleTime(long time) {
        this.ONE_WHEEL_TIME = time;
        this.invalidate();
    }

    /**
     * 开始转动
     *
     * @param pos 如果 pos = -1 则随机，如果指定某个值，则转到某个指定区域
     */
    public void startRotate(int pos) {
        int lap = (int) (Math.random() * 12) + 4;

        int angle = 0;
        if (pos < 0) {
            angle = (int) (Math.random() * 360);
        }
        //
        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        int DesRotate = increaseDegree + InitAngle;

        //TODO 为了每次都能旋转到转盘的中间位置
//        int offRotate = DesRotate % 360 % 60;
//        DesRotate -= offRotate;
//        DesRotate += 30;
        int offRotate = DesRotate % 360 % unitAngle;
        DesRotate -= offRotate;
        DesRotate += (unitAngle / 2);

        ValueAnimator animtor = ValueAnimator.ofInt(InitAngle, DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updateValue = (int) animation.getAnimatedValue();
                InitAngle = (updateValue % 360 + 360) % 360;
                ViewCompat.postInvalidateOnAnimation(RotatePan.this);
            }
        });

        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (l != null)
                    l.endAnimation(queryPosition());
            }
        });
        animtor.start();
    }


    private int queryPosition() {
        InitAngle = (InitAngle % 360 + 360) % 360;
        int pos = InitAngle / 60;
        return calcumAngle(pos);
    }

    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= 3) {
            pos = 3 - pos;
        } else {
            pos = (6 - pos) + 3;
        }
        return pos;
    }


    public interface AnimationEndListener {
        void endAnimation(int position);
    }

    private AnimationEndListener l;

    public void setAnimationEndListener(AnimationEndListener l) {
        this.l = l;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }


    // TODO ==================================== 手势处理 ===============================================================

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean consume = mDetector.onTouchEvent(event);
        if (consume) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }

        return super.onTouchEvent(event);
    }


    public void setRotate(int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        InitAngle = rotation;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            setRotate(scroller.getCurrY());
        }

        super.computeScroll();
    }

    private class RotatePanGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float centerX = (RotatePan.this.getLeft() + RotatePan.this.getRight()) * 0.5f;
            float centerY = (RotatePan.this.getTop() + RotatePan.this.getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(distanceX, distanceY, e2.getX() - centerX, e2.getY() -
                    centerY);
            int rotate = InitAngle -
                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE;

            setRotate(rotate);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float centerX = (RotatePan.this.getLeft() + RotatePan.this.getRight()) * 0.5f;
            float centerY = (RotatePan.this.getTop() + RotatePan.this.getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(velocityX, velocityY, e2.getX() - centerX, e2.getY() -
                    centerY);

            scroller.abortAnimation();
            scroller.fling(0, InitAngle, 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                    0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return true;
        }
    }

    //TODO 判断滑动的方向
    private float vectorToScalarScroll(float dx, float dy, float x, float y) {

        float l = (float) Math.sqrt(dx * dx + dy * dy);

        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }


}
