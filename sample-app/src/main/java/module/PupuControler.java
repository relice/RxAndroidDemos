package module;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import rx.android.samples.R;

/**
 * Created by relicemxd on 16/3/2.
 */
public class PupuControler {

    private Context mCont;
    private PopupWindow mPopupWindow;
    private List<String> lists = new ArrayList<>();

    public PupuControler(Context cont) {
        this.mCont = cont;
    }

    public void show(View parent) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(parent, 0, 6);
        }
    }

    public void build(final List<String> lists) {
        this.lists = lists;
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //Popupwindow的view的相关操作
        View popupView = LayoutInflater.from(mCont).inflate(R.layout.show_popu_layout, null);
        GridView gridView = (GridView) popupView.findViewById(R.id.show_pop_gridview);
        ShowPopupAdapter showPopupAdapter = new ShowPopupAdapter(lists, mCont);
        gridView.setAdapter(showPopupAdapter);
        mPopupWindow.setContentView(popupView);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow.dismiss();
            }
        });
    }
}
