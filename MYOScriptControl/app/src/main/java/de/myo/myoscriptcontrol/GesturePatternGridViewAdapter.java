package de.myo.myoscriptcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

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
        switch (item){
            case POS_CENTER:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_c));
                break;
            }
            case POS_EAST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_e));
                break;
            }
            case POS_NORTH:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_n));
                break;
            }
            case POS_NORTH_EAST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_ne));
                break;
            }
            case POS_NORTH_WEST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_nw));
                break;
            }
            case POS_WEST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_w));
                break;
            }
            case POS_SOUTH_EAST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_se));
                break;
            }
            case POS_SOUTH:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_s));
                break;
            }
            case POS_SOUTH_WEST:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_sw));
                break;
            }
            default:{
                image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.pos_null));
                break;
            }
        }

        return convertView;
    }

}
