package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

class dbHelper2 extends SQLiteOpenHelper {
    public dbHelper2(Context context) { super (context, "BookMark.db", null, 1); }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE 북마크 ( 이름 TEXT);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldver, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS 북마크");
        onCreate(db);
    }
}


public class BookMarkActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText textView;
    TextView text;

    String value = "";
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ArrayList<String> items2;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        dbHelper2 helper = new dbHelper2(this);
        db = helper.getWritableDatabase();

        text = (TextView) findViewById(R.id.text);


        Intent intent = getIntent();
        value = intent.getStringExtra("bookName");

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(BookMarkActivity.this,
                android.R.layout.simple_list_item_single_choice, items);

        listView = (ListView) findViewById(R.id.lii);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        String nn = value;
        db.execSQL("INSERT INTO 북마크 VALUES  ('" + nn + "');");

        Cursor cursor = db.rawQuery("SELECT 이름 FROM 북마크;", null);
        while (cursor.moveToNext()) {
            String n = cursor.getString(0);
            items.add(n);
        }
        adapter.notifyDataSetChanged();
        items2 = items;

    }


    /*public void onAdd(View view) {
        String n = value;
        items.add(n);
        db.execSQL("INSERT INTO 북마크 VALUES  ('" + n + "');");

        adapter.notifyDataSetChanged();
    }*/

    public void onlist(View view) {
        Intent intent = new Intent(BookMarkActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void onDelete (View view) {
        int pos = listView.getCheckedItemPosition();
        String aa = adapter.getItem(pos);
        if (pos != ListView.INVALID_POSITION) {
            items.remove(pos);
            db.execSQL("DELETE FROM 북마크 WHERE 이름 = '" + aa + "';");
            listView.clearChoices();
            adapter.notifyDataSetChanged();
        }
       // adapter.notifyDataSetChanged();
       /* db.execSQL("DELETE FROM 북마크 WHERE 이름 = '" + n + "';");
        Cursor cursor = db.rawQuery("SELECT 이름 FROM 북마크;", null);
        while (cursor.moveToNext()) {
            String nn = cursor.getString(0);
            items.add(nn);
        }
        adapter.notifyDataSetChanged();
        items2 = items;
        //onlist(view);*/
    }

}
