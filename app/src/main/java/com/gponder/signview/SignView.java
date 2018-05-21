package com.gponder.signview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *   create by GPonder
 *   its used to sign your name with your hand
 */
public class SignView extends View {

    private Paint linePaint;
    private Path currentPath;
    private List<Path> paths = new ArrayList<>();
    private Paint bgPaint;

    public SignView(Context context) {
        super(context);
        init();
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(10);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),bgPaint);
        if (paths != null && paths.size() != 0) {
            for (Path path : paths) {
                canvas.drawPath(path, linePaint);
            }
        }
    }

    /**
     *   用SurfaceView, 随时可以获取Canvas.  Canvas c = lockCanvas(); 只有用SurfaceView 才能在其他方法中使用canvas
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                paths.add(currentPath);
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                currentPath.lineTo(x, y);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    /**
     *   清除书写内容
     */
    public void clear() {
        paths.clear();
        invalidate();
    }

    /**
     *   保存成功放回路径名,失败返回null
     *   需要读写存储卡权限
     */
    public String save() {
        String sdcard = Environment.getExternalStorageDirectory().getPath();
        String file = sdcard + "/SignView" + System.currentTimeMillis() + ".png";
        boolean success = false;
        try {
            FileOutputStream fos = new FileOutputStream(new File(file));
            success = getSignBitmap2().compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("SignView",e.getCause().toString());
        }
        if (success) {
            return file;
        } else {
            return null;
        }
    }

    public List<Path> getSignPth() {
        return paths;
    }

    public void setSignPath(List<Path> paths) {
        this.paths = paths;
        invalidate();
    }

    public Bitmap getSignBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap signBitmap = getDrawingCache();
        return signBitmap;
    }

    public Bitmap getSignBitmap2() {
        Bitmap signBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.RGB_565);
        draw(new Canvas(signBitmap));
        return signBitmap;
    }

}
