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
import java.util.List;

/**
 * Created by Tommy on 12.03.2015.
 */
public class GestureItemListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private ArrayList<GestureItem> mGestureItems;

    public GestureItemListViewAdapter(Activity activity, ArrayList<GestureItem> gestureItems) {
        this.mActivity = activity;
        this.mGestureItems = gestureItems;
    }

    @Override
    public int getCount() {
        return mGestureItems.size();
    }

    @Override
    public GestureItem getItem(int location) {
        return mGestureItems.get(location);
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
            convertView = mInflater.inflate(R.layout.listview_gesture_item, null);
        }

        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView gestureName = (TextView) convertView.findViewById(R.id.gestureName);
        TextView script = (TextView) convertView.findViewById(R.id.textViewListScript);
        TextView date = (TextView) convertView.findViewById(R.id.textViewListItemDate);
//        TextView gpsPos = (TextView) convertView.findViewById(R.id.gpsPos);
//        TextView imageSize = (TextView) convertView.findViewById(R.id.imageSize);

        GestureItem item = mGestureItems.get(position);
        gestureName.setText(item.getName());
        script.setText(item.getScript());
        date.setText(item.getDate());

//        Bitmap ThumbImage = BitmapFactory.decodeFile(m.getThumbnailPath());
//        thumbNail.setImageBitmap(ThumbImage);
//
//        fileName.setText(m.getName());
//        date.setText(String.format("%s: %s", "Datum", m.getDate()));
//        String posStr;
//        if (m.getGpsPos() != null) {
//            posStr = String.format("Lat: %s° / Lon: %s°",
//                    Location.convert(m.getGpsPos().latitude, Location.FORMAT_DEGREES),
//                    Location.convert(m.getGpsPos().longitude, Location.FORMAT_DEGREES));
//        } else {
//            posStr = "Kein GPS-Tag";
//        }
//        gpsPos.setText(posStr);
//        imageSize.setText(String.format("%s: %s", "Auflösung", m.getImageSize()));

        return convertView;
    }

}
