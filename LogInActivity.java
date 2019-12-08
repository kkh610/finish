package halla.icsw.book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    dbHelper helper;
    SQLiteDatabase db;
    EditText id, pw;
    Button b1;
    AlertDialog.Builder builder;
    CheckBox auto;



    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        Button member = findViewById(R.id.member);
        id = findViewById(R.id.l_id);
        pw = findViewById(R.id.l_pw);
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        b1 = findViewById(R.id.login);
        auto = findViewById(R.id.auto);



        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }


    public void login(View v) {
        String idtext = id.getText().toString();
        String pwtext = pw.getText().toString();

        if (idtext.length() > 0 && pwtext.length() > 0) {

            Cursor cursor = db.rawQuery("SELECT _id,password,name FROM 회원 WHERE _id = '" + idtext + "' AND password = '" + pwtext + "';", null);


            if (cursor.getCount() > 0) {
                Toast.makeText(this, "로그인되었습니다.", Toast.LENGTH_LONG).show();
                while (cursor.moveToNext()) {
                    String _id = cursor.getString(0);
                    String password =cursor.getString(1);
                    name = cursor.getString(2);
                }
                // 마이페이지로 보내는거 수정중이었음
                Intent intent1 = new Intent(LogInActivity.this, MainActivity.class);
                intent1.putExtra("name", name);
                startActivity(intent1);

            } else if (cursor.getCount() == 0) {
                dialog("로그인","회원이 아닙니다.");
            } else {
                dialog("회원정보","회원정보를 입력해주세요.");
            }
        }
        else {
            dialog("회원정보","회원정보를 입력해주세요.");
        }
    }
    public void mai(View v){
            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            
        }
    //다이얼 로그
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
}
