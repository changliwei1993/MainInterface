package dream.com.mianinterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Arthur on 2016/2/25 16 : 06.
 */
public class SideImageView extends ImageView {


    private Context context;
    private String TAG="SideImageView";
    private boolean isDrag = false;
    private Vibrator mVibrator;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private Bitmap mDragBitmap;
    private int mDownX;
    private int mDownY;
    private int mPoint2ItemTop ;
    private int mPoint2ItemLeft;
    private ImageView mDragImageView;

    public SideImageView(Context context) {
        super(context);
    }



    public SideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private Handler mHandler = new Handler();
    private Runnable mLongClickRunnable = new Runnable() {

        @Override
        public void run() {
            isDrag = true;
            mVibrator.vibrate(50);
            setVisibility(View.INVISIBLE);
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };
    public void setContext(Context context) {
        this.context = context;
        mVibrator = (Vibrator)this.context.getSystemService(context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setDrawingCacheEnabled(true);
                mDragBitmap = Bitmap.createBitmap(getDrawingCache());
                destroyDrawingCache();
                mDownX = (int) event.getRawX();
                mDownY = (int) event.getRawY();

                mPoint2ItemTop = mDownY - getTop();
                mPoint2ItemLeft = mDownX - getLeft();
                mHandler.postDelayed(mLongClickRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int)event.getRawX();
                int moveY = (int) event.getRawY();
                mHandler.removeCallbacks(mLongClickRunnable);
                if (isDrag){
                    mWindowLayoutParams.x = moveX - mPoint2ItemLeft;
                    mWindowLayoutParams.y = moveY - mPoint2ItemTop ;
                    mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                Log.d(TAG,"ACTION_UP");
                break;
        }
        return true;
    }

    private void createDragImage(Bitmap bitmap, int downX , int downY){
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX- mPoint2ItemLeft;
        mWindowLayoutParams.y = downY- mPoint2ItemTop;
        mWindowLayoutParams.alpha = 0.35f;
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }


}
