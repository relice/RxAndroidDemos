package module;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import eventbus.samples.EventBusReceiverAct;
import rx.android.samples.R;

/**
 */
public class ShowPopupAdapter extends BaseAdapter {

    private List<String> list;
    private Context mContext;
    private LayoutInflater inflater;

    public ShowPopupAdapter(List<String> list, Context context) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if (list.size() % 4 == 0) {
            return list.size();
        } else {
            return list.size() + (4 - list.size() % 4);
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.show_popup_layout_item, null);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.show_pop_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position + 1 <= list.size()) {
            String name = list.get(position);
            holder.button.setText(name);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("position++++" + position);
                    String str = holder.button.getText().toString();
                    if (str != null && !TextUtils.isEmpty(str)) {
                        if (str.equals(list.get(0))) {
                            mContext.startActivity(new Intent(mContext,
                                    EventBusReceiverAct.class));
                        } else {
                            Toast.makeText(mContext, list.get(position), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            holder.button.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        Button button;
    }

}
