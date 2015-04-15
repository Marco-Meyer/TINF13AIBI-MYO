package de.myo.myoscriptcontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.myo.myoscriptcontrol.R;


public class ErrorActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        String errorMessage = getIntent().getStringExtra("errorMessage");

        TextView errorView = (TextView)findViewById(R.id.errorText);

        errorView.setText(errorMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_error, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public static void handleError(Context context, String errorMessage) {
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        context.startActivity(intent);
    }
}
