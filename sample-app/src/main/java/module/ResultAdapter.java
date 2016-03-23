package module;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.samples.R;

/**
 */
public class ResultAdapter extends BaseAdapter {

    private List<Object> list;
    private Context mContext;
    private LayoutInflater inflater;

    public ResultAdapter(List<Object> list, Context context) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
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
            convertView = inflater.inflate(R.layout.result_layout_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position + 1 <= list.size()) {
            Object obj = list.get(position);
            if (obj instanceof String) {
                String name = (String) obj;
                holder.resultTv.setText(name);
                holder.resultTv.setVisibility(View.VISIBLE);
            } else if (obj instanceof Bitmap) {
                Bitmap bitm = (Bitmap) obj;
                BitmapDrawable drawable = new BitmapDrawable(bitm);
                holder.resultIv.setBackgroundDrawable(drawable);
                holder.resultIv.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.result_tv)
        TextView resultTv;
        @Bind(R.id.result_iv)
        ImageView resultIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
