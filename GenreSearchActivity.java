package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GenreSearchActivity extends AppCompatActivity {

    String DB_NAME="bookbook.db";
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ArrayList<String> items2;
    ListView listView;

    private void copyDatabase(File dbFile){
        try {
            String folderPath="/data/data/"+getPackageName()+"/databases";
            File folder=new File(folderPath);
            if(!folder.exists()) folder.mkdirs();
            InputStream is=getAssets().open(DB_NAME);
            OutputStream os=new FileOutputStream(dbFile);
            byte[] buffer=new byte[1024];
            while(is.read(buffer)>0) os.write(buffer);
            os.flush();
            is.close(); os.close();
        }catch (Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_search);

        TextView searchText = findViewById(R.id.searchText);

        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);
        SQLiteDatabase db=openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Intent intent = getIntent();
        String searchStr = intent.getStringExtra("search");

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(GenreSearchActivity.this,
                android.R.layout.simple_list_item_1, items);

        listView = (ListView) findViewById(R.id.li);
        listView.setAdapter(adapter);

        /*if(searchStr.equals("")){
            Cursor cursor3=db.rawQuery(
                    "SELECT * FROM 책;", null);
            items.clear();
            while (cursor3.moveToNext()){
                String s = cursor3.getString(0);
                String ss = cursor3.getString(1);
                items.add(s+ " - "+ss);
            }
        }*/



        //SQLiteDatabase db=openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor=db.rawQuery(
                "SELECT * FROM 책 WHERE 책제목 like '%"+searchStr+ "%';", null
        );
        Cursor cursor2=db.rawQuery(
                "SELECT * FROM 책 WHERE 지은이 like '%"+searchStr+ "%';", null
        );


        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        while (cursor.moveToNext()){
            String s = cursor.getString(0);
            String ss = cursor.getString(1);
            items.add(s+ " - "+ss);
        }

        while (cursor2.moveToNext()){
            String s = cursor2.getString(0);
            String ss = cursor2.getString(1);
            items.add(s+ " - "+ss);
        }
        adapter.notifyDataSetChanged();
        items2 = items;

            if ( cursor.getCount()==0 && cursor2.getCount() ==0){
                items.add("검색 결과가 없습니다.");
            }
        }
    }
