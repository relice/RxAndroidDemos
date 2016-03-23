package rx.android.samples;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import module.PupuControler;
import module.ResultAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "RxAndroidSamples";
    @Bind(R.id.more_btn)
    Button moreBtn;
    @Bind(R.id.scheduler_example)
    Button schedulerExample;
    @Bind(R.id.clear_example)
    Button clearExample;
    @Bind(R.id.btn_layout)
    LinearLayout btnLayout;
    @Bind(R.id.scheduler_lv)
    ListView schedulerLv;
    @Bind(R.id.scheduler_pb)
    ProgressBar schedulerPb;

    private Handler backgroundHandler;
    private MainActivity mAct;
    private ProgressBar dialog;
    private PupuControler mPopu;
    private ResultAdapter resultAdapter;
    private ArrayList<Object> adaInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        mAct = this;
        initView();

        //使用RxBinding来解决 手抖问题 嘿嘿
        //gradle添加JackWharton的包
        RxView.clicks(schedulerExample).throttleFirst(500, TimeUnit.MILLISECONDS)//防手抖设置
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        initMerge(1);
                        initUiils(3);
//                        initPublishObjects(1);
                        //initThreadScheduler(1);
//                        initTransform(1);
                        //initFromFunction(2);
                        //initObserable(3);
                        //onRunSchedulerExampleButtonClicked();
                    }
                });
    }

    /**
     * 将两个或更多的Observables合并在一起，可以使用操作符 join，zip，merge
     *
     * @param i
     */
    private void initMerge(int i) {
        switch (i) {
            case 1:
                List<String> strs = new ArrayList<>();
                strs.add("1");
                strs.add("2");
                strs.add("3");

                Observable<String> ob1 = Observable.from(strs);
                Collections.reverse(strs);
                Observable<String> ob2 = Observable.from(strs);

                Observable.merge(ob1, ob2).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        showResult("merge:" + s);
                    }
                });
                break;
            case 2:
        }
    }

    /**
     * 将基于RxJava的方法展示一些例子
     * repeat(),重复指定数据
     * defer(),推迟发送数据
     * range(),从x数到N
     * interval(),类似looper轮询器
     * timer(),如果你需要一个一段时间之后才发射的Observable
     *
     * @param i
     */
    private void initUiils(int i) {
        switch (i) {
            case 1:
                //重复3次数据
                Observable.just("a", "c", "g")
                        .repeat(3).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        showResult("repeat:" + s);
                    }
                });
                break;
            case 2:
                //重3发射到6: 3,4,5,6 共4位
                Observable.range(3, 4)
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(mAct, "Yeaaah!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mAct, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Integer number) {
                                Toast.makeText(mAct, "I say " + number, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case 3:
                //相当于Looper轮询器,每3s轮询一次
                Subscription subscribe = Observable
                        .interval(3, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(mAct, "Yeaaah!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mAct, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Long number) {
                                showResult("interval:" + number);
                                Toast.makeText(mAct, "I say " + number, Toast.LENGTH_SHORT).show();
                            }
                        });
                clearResult(subscribe);
                break;
            case 4:
                //过一段时间后执行.
                Subscription subscribe1 = Observable.timer(3, TimeUnit.SECONDS)
                        .subscribe(new Observer<Long>() {

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Long number) {
                                Log.d("RXJAVA", "I say " + number);
                                showResult("timer:" + number);
                            }
                        });
                clearResult(subscribe1);
                break;
            case 5:
                //综合练习
                Observable<String> repeats = Observable.just("a", "g", "c", "g", "w")
                        .take(4)
                        .repeat(4);
                //去除重复:最终输出a,c,g
                repeats.distinct().subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        showResult("distinct:" + s);
                    }
                });
                break;
        }
    }

    /**
     * 推送数据
     * 使用特点:
     * 1.Subject = Observable + Observer,
     * 2.也就是说 Subject相对 Observble而言.
     * {@link Observable} 在create时就推送数据,最后才订阅:
     * Observble ->create(推送数据) ->subscribe()
     * <p/>
     * Subject 先处理了订阅,后推送数据:
     * PublishSubject 可以配合SerializedSubject 构成事件总线(如广播接受者)
     * {@link PublishSubject}  ->create() ->subscribe() ->onNext(推送数据)
     *
     * @param num
     */
    private void initPublishObjects(int num) {
        switch (num) {
            case 1:
                PublishSubject<String> puobj = PublishSubject.create();
                puobj.subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        showResult("Publish1:" + s);
                    }
                });
                //Observable无法像PublishSubject一样,订阅后又调用onNext推送数据
                puobj.onNext("A");
                puobj.onNext("P");
                puobj.onNext("P");
                break;
            // Subject的子类还有
            //  1.ReplaySubject
            //ReplaySubject会缓存它所订阅的所有数据,向任意一个订阅它的观察者重发:
            //  2.AsyncSubject
            //当Observable完成时AsyncSubject只会发布最后一个数据给已经订阅的每一个观察者。
        }
    }

    /**
     * 线程控制
     *
     * @param testNum
     */
    private void initThreadScheduler(int testNum) {
        switch (testNum) {
            case 1:
                Subscription subscribe = Observable.just(1, 3, 5)
                        //由于subscribeOn的调用位置不一样,因此只调用一次
                        .subscribeOn(Schedulers.io()).map(new Func1<Integer, String>() {
                            @Override
                            public String call(Integer integer) {
                                return "转换" + integer;
                            }
                        })
                                //subscribe在mian线程运行
                        .observeOn(AndroidSchedulers.mainThread())
                                //.subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                showResult("subscribeOn:" + s);
                            }
                        });
                break;
        }
    }

    /**
     * rxjava的核心变换,map(),flatMap(),flat()
     */
    private void initTransform(int testNum) {
        switch (testNum) {
            case 1:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(R.drawable.ic_launcher);
                        subscriber.onCompleted();
                    }
                }).map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer imgID) {
                        //map转换:把字符串转换成 bitmap
                        Bitmap bitmap = BitmapFactory.decodeResource
                                (getResources(), imgID);
                        return bitmap;
                    }
                }).subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        showResult(bitmap);
                    }
                });
                break;
            case 2:
                ArrayList<Student> students = new ArrayList<>();
                Student st1 = new Student("Jay_Zhou");
                ArrayList<Course> courses1 = new ArrayList<>();
                courses1.add(new Course("语文"));
                courses1.add(new Course("体育"));
                st1.setCorses(courses1);
                students.add(st1);

                Student st2 = new Student("Bobo");
                ArrayList<Course> courses2 = new ArrayList<>();
                courses2.add(new Course("美术"));
                st2.setCorses(courses2);
                students.add(st2);

                Observable.from(students)
                        .flatMap(new Func1<Student, Observable<Course>>() {
                            @Override
                            public Observable<Course> call(Student student) {
                                //与map()区别其实flat是嵌套操作,它把student内的course集合平铺了
                                //然后重新from()回调,因此最终返回的就是Course对象
                                System.out.println("name:" + student.getName());
                                return Observable.from(student.getCorses());
                            }
                        })
                        .subscribe(new Action1<Course>() {
                            @Override
                            public void call(Course course) {
                                showResult("flatMap_CourseName:" + course.getCourseName());
                            }
                        });
                break;
            case 3:
                //源码分析:map()与flatMap()都是通过lift()进行包装而来的.
                Integer[] names = {1, 3, 5};
                Observable.from(names)
                        .lift(new Observable.Operator<String, Integer>() {
                            @Override
                            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                                return new Subscriber<Integer>() {
                                    @Override
                                    public void onCompleted() {
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        subscriber.onError(e);
                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        //把int 转成 string
                                        subscriber.onNext(integer + "");
                                    }
                                };
                            }
                        })
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                //订阅输出,转成string的int
                                showResult("flat:" + s);
                            }
                        });
                break;
            case 4:
                Integer[] nums = {1, 3, 5};
                class LiftAllTransFromer implements Observable.Transformer<Integer, String> {
                    @Override
                    public Observable<String> call(Observable<Integer> integerObservable) {
                        return integerObservable.lift(new Observable.Operator<String, Integer>() {
                            @Override
                            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                                return new Subscriber<Integer>() {
                                    @Override
                                    public void onCompleted() {
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        subscriber.onError(e);
                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        //把int 转成 string
                                        subscriber.onNext(integer + "");
                                    }
                                };
                            }
                        })
                                .lift(new Observable.Operator<String, String>() {
                                    @Override
                                    public Subscriber<? super String> call(final Subscriber<? super String> subscriber) {
                                        return new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                subscriber.onError(e);
                                            }

                                            @Override
                                            public void onNext(String integer) {
                                                //把int 转成 string
                                                subscriber.onNext(integer + "");
                                            }
                                        };
                                    }
                                })
                                .lift(new Observable.Operator<String, String>() {
                                    @Override
                                    public Subscriber<? super String> call(final Subscriber<? super String> subscriber) {
                                        return new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                subscriber.onError(e);
                                            }

                                            @Override
                                            public void onNext(String integer) {
                                                //把int 转成 string
                                                subscriber.onNext(integer + "");
                                            }
                                        };
                                    }
                                })
                                .lift(new Observable.Operator<String, String>() {
                                    @Override
                                    public Subscriber<? super String> call(final Subscriber<? super String> subscriber) {
                                        return new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                subscriber.onError(e);
                                            }

                                            @Override
                                            public void onNext(String integer) {
                                                //把int 转成 string
                                                subscriber.onNext(integer + "");
                                            }
                                        };
                                    }
                                });
                    }
                }
                LiftAllTransFromer liftAll = new LiftAllTransFromer();

                Observable.from(nums)
                        .compose(liftAll)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                //订阅输出,转成string的int
                                showResult("compose:" + s);
                            }
                        });
                break;
        }
    }

    /**
     * 打印字符串数组,设置img的background场景,doOnSubscribe()初使用
     */
    private void initFromFunction(int testNum) {
        switch (testNum) {
            case 1:
                String[] names = {"a", "b", "c"};
                //from这里可以是数据,也可以是集合
                //        Observable.from(list)
                Observable.from(names)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println("字符串数组+++" + s);
                            }
                        });
                break;
            case 2:
                Observable<Drawable> dob = Observable.create(
                        new Observable.OnSubscribe<Drawable>() {
                            @Override
                            public void call(Subscriber<? super Drawable> subscriber) {
                                Drawable drawable = mAct.getResources()
                                        .getDrawable(R.drawable.ic_launcher);
                                subscriber.onNext(drawable);
                                subscriber.onCompleted();
                            }
                        });
                dob.subscribeOn(Schedulers.io());
                dob.subscribeOn(AndroidSchedulers.mainThread());
                dob.observeOn(AndroidSchedulers.mainThread());
                dob.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //一般把显示进度圈放在doOnSubscribe
                        //需要在主线程执行
                        schedulerPb.setVisibility(View.VISIBLE);
                        System.out.println("doOnSubscribe..");
                    }
                })
                        .subscribe(new Subscriber<Drawable>() {
                            @Override
                            public void onCompleted() {
                                schedulerPb.setVisibility(View.GONE);
                                System.out.println("onCompleted..");
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("onError:" + e.getMessage());
                            }

                            @Override
                            public void onNext(Drawable drawable) {
                                schedulerPb.setBackgroundDrawable(drawable);
                            }
                        });
                break;
            case 3:
                Observable.just("a", "b", "c", "d")
                        //由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；
                        .subscribeOn(Schedulers.io())//指定subscribe()发生在io线程
                                //observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程 。
                        .subscribeOn(AndroidSchedulers.mainThread())//指定订阅者的回调发生在主线程
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                showResult("ioThread:" + s);
                            }
                        });
                break;
        }
    }

    /**
     * 创建 Observable ->create(),just(),from()方法
     */
    private void initObserable(int testNum) {
        switch (testNum) {
            case 1:
                //方法1:create() 原始调用
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override//call()是最原始的调用
                    public void call(Subscriber<? super String> subscriber) {
                        //这里也可以通过for循环推送数据
                        subscriber.onNext("Hello ");
                        subscriber.onNext("RxJava ");
                        subscriber.onNext("By Relice");
                        //只有私有的 Subscriber才访问到
                        subscriber.onCompleted();
                    }
                })
                        .subscribe();
                break;
            case 2:
                //方法2:from() 可以传入可变参数
                Observable ob2 = Observable.just("Hello", "RxJava", "By Relice");
                //将传入的 Subscriber 作为 Subscription 返回。这是为了方便 unsubscribe().
                Subscription subscribe = ob2.subscribe(new Subscriber() {

                    @Override
                    public void onCompleted() {
                        System.out.println("--onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("****onError:" + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        String info = (String) o;
                        showResult("just:" + info);
                    }
                });
                subscribe.unsubscribe();
                break;
            case 3:
                //方法3:from() 可以传入数据或Iterable拆分后的对象
                //        String[] strs = new String[]{"Hello","RxJava","By Relice"};
                //        Observable ob3 = Observable.from(strs);
                List<String> lists = new ArrayList<>();
                lists.add("java");
                lists.add("c++");
                lists.add("oc");
                Observable ob3 = Observable.from(lists);

                //subscribe() 还支持不完整定义的回调，RxJava 会自动根据定义创建出 Subscriber 。
                // 形式如下：Action1对象
                ob3.subscribe(new Action1<String>() {
                    @Override
                    public void call(String o) {
                        showResult("from:" + o);
                    }
                });
                break;
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_btn:
                mPopu.show(moreBtn);
                break;
        }
    }

    private void showResult(Object result) {
        checkUiThread();
        System.out.println("printResult  " + result);
        if (result != null) {
            adaInfos.add(result);
            resultAdapter.notifyDataSetChanged();
        }
    }

    private void clearResult(final Subscription subscribe) {
        clearExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("adaInfos  " + adaInfos);
                if (subscribe != null) {
                    subscribe.unsubscribe();
                    System.out.println("unsubscribe  ");
                }

                if (adaInfos != null) {
                    adaInfos.clear();
                    resultAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 检查当前线程
     */
    public void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }

    private void initView() {
        moreBtn.setOnClickListener(this);
        clearResult(null);

        //RxBus项目
        ArrayList<String> strings = new ArrayList<>();
        strings.add("EventBut");
        strings.add("RxJava");
        mPopu = new PupuControler(mAct);
        mPopu.build(strings);

        if (resultAdapter == null) {
            adaInfos = new ArrayList<>();
            resultAdapter = new ResultAdapter(adaInfos, mAct);
            schedulerLv.setAdapter(resultAdapter);
        }
    }
}
