package de.myo.myoscriptcontrol.gesturerecording;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.myo.myoscriptcontrol.R;
import de.myo.myoscriptcontrol.gesturerecording.GesturePattern;
import de.myo.myoscriptcontrol.gesturerecording.GridPosition;

/**
 * Created by DTH on 26.03.2015.
 */
public class GesturePatternGridViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private GesturePattern mGesturePattern;

    public GesturePatternGridViewAdapter(Activity activity, GesturePattern gesturePattern) {
        this.mActivity = activity;
        this.mGesturePattern = gesturePattern;
    }

    @Override
    public int getCount() {
        return mGesturePattern.size();
    }

    @Override
    public GridPosition getItem(int location) {
        return mGesturePattern.get(location);
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
            convertView = mInflater.inflate(R.layout.gridview_gesturepattern_item, null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.imageViewGridPosition);
        TextView number = (TextView) convertView.findViewById(R.id.textViewPatternNumber);
        number.setText(position+1 + ".");
        GridPosition item = mGesturePattern.get(position);
        image.setImageDrawable(getDrawableForPosition(item));

        return convertView;
    }

    private Drawable getDrawableForPosition(GridPosition position) {
        switch (position) {
            case POS_CENTER:{
                return mActivity.getResources().getDrawable(R.drawable.pos_c);
            }
            case POS_EAST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_e);
            }
            case POS_NORTH:{
                return mActivity.getResources().getDrawable(R.drawable.pos_n);
            }
            case POS_NORTH_EAST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_ne);
            }
            case POS_NORTH_WEST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_nw);
            }
            case POS_WEST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_w);
            }
            case POS_SOUTH_EAST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_se);
            }
            case POS_SOUTH:{
                return mActivity.getResources().getDrawable(R.drawable.pos_s);
            }
            case POS_SOUTH_WEST:{
                return mActivity.getResources().getDrawable(R.drawable.pos_sw);
            }
            default:{
                return mActivity.getResources().getDrawable(R.drawable.pos_null);
            }
        }
    }

}
