package dream.com.mianinterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Arthur on 2016/2/25 16 : 06.
 */
public class SideImageView extends ImageView {


    private Context context;
    private String TAG = "SideImageView";
    private boolean isDrag = false;

    private Vibrator mVibrator;

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams mWindowLayoutParams;

    private Bitmap mDragBitmap;

    private int locate_X;
    private int locate_Y;

    private float press_relate_X;
    private float press_relate_Y;

    private int card_one_locate_X;
    private int card_one_locate_Y;

    private int card_one_width;
    private int card_one_height;

    private ImageView mDragImageView;

    private int statusBarHeight;

    private ImageView sigleImageView;
    private LinearLayout dynamic_box1;


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
            createDragImage(mDragBitmap, locate_X, locate_Y);
        }
    };

    public void setContext(Context context) {
        this.context = context;
        mVibrator = (Vibrator) this.context.getSystemService(context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);

        sigleImageView=(ImageView) ((MainActivity)context).findViewById(R.id.singleView);
        dynamic_box1=(LinearLayout) ((MainActivity)context).findViewById(R.id.dynamic_box1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //得到当前View的Bitmap
                setDrawingCacheEnabled(true);
                mDragBitmap = Bitmap.createBitmap(getDrawingCache());
                destroyDrawingCache();

                //得到卡片在屏幕中的宽高
                final ViewTreeObserver vto = dynamic_box1.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        vto.removeOnPreDrawListener(this);
                        card_one_height = dynamic_box1.getMeasuredHeight();
                        card_one_width= dynamic_box1.getMeasuredWidth();

                        return true;
                    }
                });


                //得到卡片在屏幕中的位置
                int[] location = new int[2];
                dynamic_box1.getLocationOnScreen(location);
                card_one_locate_X = location[0];
                card_one_locate_Y= location[1];


                //得到状态栏的高度
                Rect frame = new Rect();
                ((MainActivity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                statusBarHeight = frame.top;

                //按下点的相对位置
                press_relate_X=event.getX();
                press_relate_Y=event.getY();

                //长按后，镜像处于的位置
                locate_X = (int) (event.getRawX()-press_relate_X);
                locate_Y = (int) (event.getRawY()-press_relate_Y-statusBarHeight);

                //抛出线程，1秒后执行长按事件
                mHandler.postDelayed(mLongClickRunnable, 700);

                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = (int) event.getRawX();
                int moveY = (int) event.getRawY();
                if (isDrag) {
                    //长按后移动位置
                    mWindowLayoutParams.x = (int) (moveX-press_relate_X);
                    mWindowLayoutParams.y = (int) (moveY-press_relate_Y-statusBarHeight);
                    mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams);
                    if (moveX>=card_one_locate_X&&moveX<=card_one_locate_X+card_one_width&&moveY>=card_one_locate_Y&&moveY<=card_one_locate_Y+card_one_height){

                        sigleImageView.setVisibility(VISIBLE);
                        sigleImageView.setImageBitmap(Bitmap.createBitmap(getDrawingCache()));
                        mWindowManager.removeView(mDragImageView);
                        isDrag=false;
                        setVisibility(VISIBLE);

                    }

                }else {
                    //没有长按就移动位置
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //长按后抬起
                    mWindowManager.removeView(mDragImageView);
                    setVisibility(VISIBLE);
                }else {
                    //没有长按就抬起
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                isDrag = false;
                break;
        }
        return true;
    }

    private void createDragImage(Bitmap bitmap, int locate_X, int locate_Y) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = locate_X;
        mWindowLayoutParams.y = locate_Y;
        mWindowLayoutParams.alpha = 0.35f;
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }


}
