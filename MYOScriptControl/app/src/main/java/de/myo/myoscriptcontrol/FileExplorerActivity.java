package de.myo.myoscriptcontrol;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class FileExplorerActivity extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = this.getActionBar();
        if (bar != null){
            bar.setLogo(R.drawable.directory_icon);
        }
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Verzeichnis: "+f.getName());
        List<Item>dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();
        try{
            for(File ff: dirs){
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){
                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    } else {
                        buf = 0;
                    }
                    String num_item = String.valueOf(buf);
                    if(buf == 0){
                        num_item = num_item + " Element";
                    } else {
                        num_item = num_item + " Elemente";
                    }
                    dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                } else {
                    fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        } catch(Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard")){
            dir.add(0,new Item("..","Verzeichnis zurück","",f.getParent(),"directory_up"));
        }
        adapter = new FileArrayAdapter(FileExplorerActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!currentDir.getName().equalsIgnoreCase("sdcard")){
            currentDir = currentDir.getParentFile();
            fill(currentDir);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        } else {
            onFileClick(o);
        }
    }
    private void onFileClick(Item o)
    {
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}

