package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class Game2Activity extends AppCompatActivity {

    String DB_NAME="bookbook.db";

    String title = "";
    String FN = "";
    String SN = "";

    TextView questionText;
    TextView scoreText;
    EditText answerText;
    MediaPlayer player;

    int score = 0;

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
        setContentView(R.layout.activity_game2);

        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);

        player = MediaPlayer.create(this,R.raw.ppp);
        player.setLooping(false);
        player.start();

    }

    public void game2Start(View view){

        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        scoreText = findViewById(R.id.scoreText);

        Random random = new Random();
        int randNum = random.nextInt(31)+1;
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery(
                "SELECT 책제목, FirstName, SecondName FROM 책 WHERE Game2 =  " + randNum + ";", null
        );

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                title = cursor.getString(0);
                FN = cursor.getString(1);
                SN = cursor.getString(2);
            }
            questionText.setText(FN);
        }

        Button answerButton = findViewById(R.id.answerButton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answerText.getText().toString().equals(SN)){
                    Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                    game2Start(view);
                    score+=10;
                    scoreText.setText(score+" 점");
                    answerText.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
                    score -= 10;
                    scoreText.setText(score+" 점");
                }
            }
        });

        Button hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),title,Toast.LENGTH_LONG).show();
                score -= 5;
                scoreText.setText(score+" 점");
            }
        });


    }
    public void ma(View v){
        player.stop();
        Intent intent = new Intent(Game2Activity.this,MainActivity.class);
        startActivity(intent);
    }
}