package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookReport extends AppCompatActivity {

    class dbHelper3 extends SQLiteOpenHelper {
        public dbHelper3(Context context) {
            super(context, "report.db", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE 독후감 (gtitle text,btite text,write text,contect text,genre text);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contact");
            onCreate(db);
        }
    }

    dbHelper3 helper;
    SQLiteDatabase db;
    Button delete;
    EditText gtitle, btitle, write, contect;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4, rb5;
    RadioButton[] buttons;
    String bgenre;
    String ggtitle;
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ArrayList<String> items2;
    LinearLayout book, li;
    ScrollView sc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_report);
        helper = new dbHelper3(this);
        db = helper.getWritableDatabase();
        gtitle = findViewById(R.id.gtitle);
        btitle = findViewById(R.id.btitle);
        write = findViewById(R.id.write);
        contect = findViewById(R.id.contect);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        buttons = new RadioButton[]{rb1, rb2, rb3, rb4, rb5};
        book = findViewById(R.id.book);
        li = findViewById(R.id.li);
        delete = findViewById(R.id.delete);
        sc = findViewById(R.id.sc);
        Intent intent = new Intent();

        if(getIntent().getStringExtra("aa").equals("독후감")){
            sc.setVisibility(View.VISIBLE);
            li.setVisibility(View.GONE);
        }
        if(getIntent().getStringExtra("aa").equals("독후감목록")) {
            sc.setVisibility(View.GONE);
            li.setVisibility(View.VISIBLE);
        }

        if (items2 == null) {
            items = new ArrayList<String>();
        } else {
            items = items2;
        }

        adapter = new ArrayAdapter<String>(BookReport.this,
                android.R.layout.simple_list_item_single_choice, items);

        // 어댑터 설정
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void save(View v) {
        if (gtitle.length() > 0 && btitle.length() > 0 && write.length() > 0 && contect.length() > 0) {
            ggtitle = gtitle.getText().toString();
            String bbtitle = btitle.getText().toString();
            String write2 = write.getText().toString();
            String contect2 = contect.getText().toString();
            for (int i = 0; i < 5; i++) {
                if (buttons[i].isChecked() == true) {
                    bgenre = buttons[i].getText().toString();
                }
            }
            db.execSQL("INSERT INTO 독후감 VALUES ('" + ggtitle + "','" + bbtitle + "','" + write2 + "','" + contect2 + "','" + bgenre + "');");

            Toast.makeText(this, "독후감이 저장되었습니다.", Toast.LENGTH_LONG).show();
            Cursor cursor = db.rawQuery("SELECT gtitle FROM 독후감;", null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String s = cursor.getString(0);
                items.add(i, s);
            }
            adapter.notifyDataSetChanged();
            book.setVisibility(View.GONE);
            li.setVisibility(View.VISIBLE);
            items2 = items;
        } else {
            Toast.makeText(this, "모두 작성해 주세요.", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(View v) {
        Intent intent = new Intent(BookReport.this, MainActivity.class);
        startActivity(intent);
    }

    public void delete(View v) {
        int pos = listView.getCheckedItemPosition();
        if (pos != ListView.INVALID_POSITION) {
            items.remove(pos);
            listView.clearChoices();
            adapter.notifyDataSetChanged();
        }
    }


}

