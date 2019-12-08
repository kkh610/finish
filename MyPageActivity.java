package halla.icsw.book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MyPageActivity extends AppCompatActivity {

    dbHelper helper;
    SQLiteDatabase db;
    TextView nameText;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        nameText = findViewById(R.id.nametext);

        helper = new dbHelper(MyPageActivity.this);
        db = helper.getReadableDatabase();

        Intent intent = getIntent();
        String nameIntent = intent.getStringExtra("name1");


        nameText.setText(nameIntent + "님");


        if (nameText.getText().toString().equals("null님")) {
            nameText.setText("로그인이 필요합니다.");
        }

    }

    public void bookMarkOnClick(View view) {
        if (nameText.getText().toString().equals("로그인이 필요합니다.")) {
            dialog("로그인","로그인이 필요합니다.");
        }
        else {
            Intent intent = new Intent(MyPageActivity.this, BookMarkActivity.class);
            startActivity(intent);
        }
    }

    public void mylist(View view) {
        if (nameText.getText().toString().equals("로그인이 필요합니다.")) {
            dialog("로그인","로그인이 필요합니다.");
        }
        else {
            Intent intent = new Intent(MyPageActivity.this, BookReport.class);
            intent.putExtra("aa", "독후감목록");
            startActivity(intent);
        }
    }


    public void dialog(String string,String string2){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(string).setMessage(string2);
        builder.setIcon(R.drawable.loginn);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});
        AlertDialog alertDialog =  builder.show();
        TextView msg = (TextView)alertDialog.findViewById(android.R.id.message);
        msg.setTextColor(Color.rgb(163,100,96));

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.aler);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.height = 530;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.show();

    }

    public void bye(View v){
        if (nameText.getText().toString().equals("로그인이 필요합니다.")) {
            dialog("로그인","로그인이 필요합니다.");
        }
        else {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("탈퇴").setMessage("탈퇴하시겠습니까");
            builder.setIcon(R.drawable.loginn);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String s = nameText.getText().toString();
                    String ss = s.substring(0, 3);
                    db.execSQL("DELETE FROM 회원 WHERE name = '" + ss + "';");
                    nameText.setText("로그인이 필요합니다.");

                }
            });
            AlertDialog alertDialog = builder.show();
            TextView msg = (TextView) alertDialog.findViewById(android.R.id.message);
            msg.setTextColor(Color.rgb(163, 100, 96));

            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.aler);
            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            params.height = 500;
            alertDialog.getWindow().setAttributes(params);
            alertDialog.show();

        }
    }


}