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
 * Created by Daniel on 19.03.2015.
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
            convertView = mInflater.inflate(R.layout.listview_script_item, null);
        }
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView scriptName = (TextView) convertView.findViewById(R.id.scriptName);

        TextView description = (TextView) convertView.findViewById(R.id.scriptDescription);
        TextView date = (TextView) convertView.findViewById(R.id.textViewListItemDate);

        ScriptItem item = mScriptItems.get(position);
        switch (item.getScriptFileType()){
            case "py":{
                thumbNail.setImageDrawable(mActivity.getApplicationContext().getResources().getDrawable(R.drawable.python));
                break;
            }
            default:{
                thumbNail.setImageDrawable(mActivity.getApplicationContext().getResources().getDrawable(R.drawable.file_icon));
                break;
            }
        }
        scriptName.setText(item.getName());
        description.setText(item.getDescription());
        date.setText(item.getDate());

        return convertView;
    }
}
