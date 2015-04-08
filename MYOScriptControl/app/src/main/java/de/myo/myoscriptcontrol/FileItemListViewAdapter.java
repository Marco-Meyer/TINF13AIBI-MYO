package de.myo.myoscriptcontrol;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class FileItemListViewAdapter extends ArrayAdapter<FileItem>{

	private Context c;
	private int id;
	private List<FileItem> fileItems;
	
	public FileItemListViewAdapter(Context context, int textViewResourceId, List<FileItem> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		fileItems = objects;
	}

	public FileItem getItem(int i)
    {
        return fileItems.get(i);
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final FileItem o = fileItems.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
            ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);

            String name = o.getName();
            name = FileManager.getFileExtension(name);
            switch (name){
                case "py":{
                    Drawable image = c.getResources().getDrawable(R.drawable.python);
                    imageCity.setImageDrawable(image);
                    break;
                }
                default:{
                    String uri = "drawable/" + o.getImage();
                    int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
                    Drawable image = c.getResources().getDrawable(imageResource);
                    imageCity.setImageDrawable(image);
                    break;
                }
            }

            if(t1!=null)
                 t1.setText(o.getName());
            if(t2!=null)
                 t2.setText(o.getData());
            if(t3!=null)
                 t3.setText(o.getDate());

        }
        return v;
    }
}
