package dream.com.mianinterface;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity{
    private String TAG="MainActivity";
    private SideImageView img_one,img_two,img_three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

}
