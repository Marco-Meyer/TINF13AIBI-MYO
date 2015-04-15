package de.myo.myoscriptcontrol.activities;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import de.myo.myoscriptcontrol.listmanagement.filemanagement.FileItem;
import de.myo.myoscriptcontrol.listmanagement.filemanagement.FileItemListViewAdapter;
import de.myo.myoscriptcontrol.R;

public class FileExplorerActivity extends ActionBarActivity {
    private File currentDir;
    private FileItemListViewAdapter adapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        listView = (ListView)findViewById(R.id.listViewFiles);
        setOnItemClickListener();
        currentDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        fill(currentDir);
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Verzeichnis: "+f.getName());
        List<FileItem>dir = new ArrayList<FileItem>();
        List<FileItem>fls = new ArrayList<FileItem>();
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
                    dir.add(new FileItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                } else {
                    fls.add(new FileItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        } catch(Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!currentDir.getName().equalsIgnoreCase("storage")){
            dir.add(0, new FileItem("..", "Verzeichnis zurück", "", f.getParent(), "directory_up"));
        }
        adapter = new FileItemListViewAdapter(FileExplorerActivity.this,R.layout.file_view,dir);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!currentDir.getName().equalsIgnoreCase("storage")){
            currentDir = currentDir.getParentFile();
            fill(currentDir);
        } else {
            super.onBackPressed();
        }
    }

    private void setOnItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileItem o = adapter.getItem(position);
                if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
                    currentDir = new File(o.getPath());
                    fill(currentDir);
                } else {
                    onFileClick(o);
                }
            }
        });
    }

    private void onFileClick(FileItem o)
    {
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_explorer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

