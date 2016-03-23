package eventbus.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.samples.R;

/**
 * Created by relicemxd on 16/3/2.
 */
public class EventBusReceiverAct extends Activity {

    @OnClick(R.id.receiver_btn)
    public void goToSendAct() {
        Intent intent = new Intent(getApplicationContext(),
                EventBusSendAct.class);
        startActivity(intent);
    }

    @Bind(R.id.receiver_tv)
    TextView receiverTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_receiver_layout);
        ButterKnife.bind(this);

        //注册RxBus
        RxBus.get().register(this);

    }

    @Subscribe
    public void getResult(String str) {
        //普通订阅发送的数据,没有tag的
        receiverTv.setText("返回数据:" + str);
    }

    //通过tag 订阅发送的数据
    @Subscribe(
            thread = EventThread.IO,
            tags = {
                    @Tag(EventBusSendAct.SZ_TAG)
            }
    )
    //接收带tag 的数据
    @Tag(EventBusSendAct.SZ_TAG)
    public void produceMsg(String str) {
        System.out.println("信息内容: " + str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册RxBus
        RxBus.get().unregister(this);
    }
}
