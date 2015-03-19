package de.myo.myoscriptcontrol;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tommy on 19.03.2015.
 */
public class ScriptItemListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private ArrayList<ScriptItem> mScriptItems;

    public ScriptItemListViewAdapter(Activity activity, ArrayList<ScriptItem> scriptItems) {
        this.mActivity = activity;
        this.mScriptItems = scriptItems;
    }

    @Override
    public int getCount() {
        return mScriptItems.size();
    }

    @Override
    public ScriptItem getItem(int location) {
        return mScriptItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            //TODO Layout
            convertView = mInflater.inflate(R.layout.listview_gesture_item, null);
        }

//        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
//        TextView gestureName = (TextView) convertView.findViewById(R.id.gestureName);
//        TextView script = (TextView) convertView.findViewById(R.id.textViewListScript);
//        TextView date = (TextView) convertView.findViewById(R.id.textViewListItemDate);

//        ScriptItem item = mScriptItems.get(position);
//        gestureName.setText(item.getName());
//        script.setText(item.getScript());
//        date.setText(item.getDate());

        return convertView;
    }
}
