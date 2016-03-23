package eventbus.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.samples.R;

/**
 * Created by relicemxd on 16/3/2.
 */
public class EventBusSendAct extends Activity {
  public static final String SZ_TAG = "sz_tag";
    @OnClick(R.id.send_btn)
    public void sendMsg() {

        Random random = new Random();
        int i = random.nextInt(11);
        String str = sendTv.getText().toString();

        //RxBus 发送数据
        RxBus.get().post(str+" 随机数:"+i );
        RxBus.get().post(SZ_TAG,"深圳发来贺卡" );
        Toast.makeText(this, "发送数据:"+str+"  随机数:"+i , Toast.LENGTH_SHORT).show();
    }

    @Bind(R.id.send_tv)
    TextView sendTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_send_layout);
        ButterKnife.bind(this);
    }
}
