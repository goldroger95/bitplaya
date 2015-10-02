package com.example.devanksriram.bitplaya;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends Activity {
    private ListView ls1;
    private TextView tx1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String music_path = System.getenv("EXTERNAL_STORAGE")+"/Music";
        File f = new File(music_path);
        //File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "Music");
        if (f.exists()) {
            File filelist[] = f.listFiles();
            String nameoffiles[] = new String[filelist.length];
            Log.d("File", "Length : " + filelist.length);
            for (int i = 0; i < nameoffiles.length; i++) {
                nameoffiles[i] = filelist[i].getName();
                Log.d("Files", "Filename " + nameoffiles[i]);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_library, nameoffiles);
            ls1 = (ListView) findViewById(R.id.music_list);
            ls1.setAdapter(adapter);
        }
        else{
            tx1=(TextView)findViewById(R.id.tx1);
            tx1.setText("File does not exist");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
