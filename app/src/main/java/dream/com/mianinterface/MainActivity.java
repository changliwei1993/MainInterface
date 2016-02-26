package dream.com.mianinterface;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{
    private String TAG="MainActivity";
    private SideImageView img_one,img_two,img_three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        img_one=(SideImageView)findViewById(R.id.img_one);
        img_two=(SideImageView)findViewById(R.id.img_two);
        img_three=(SideImageView)findViewById(R.id.img_three);
        img_one.setContext(this);
        img_two.setContext(this);
        img_three.setContext(this);
    }



/*    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.img_one:
                Log.d(TAG,"img_one");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG,"img_oneACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG,"img_oneACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"img_oneACTION_UP");
                        break;
                }


                break;

            case R.id.img_two:
                Log.d(TAG,"img_one");
                break;

            case R.id.img_three:
                Log.d(TAG,"img_one");
                break;
            default:
                break;
        }
        return true;
    }*/
}
