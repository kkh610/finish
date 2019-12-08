package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GameMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);



        LinearLayout game1Layout = findViewById(R.id.game1Layout);
        LinearLayout game2Layout = findViewById(R.id.game2Layout);

        game1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameMainActivity.this, Game1Activity.class);
                startActivity(intent);
            }
        });

        game2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameMainActivity.this, Game2Activity.class);
                startActivity(intent);
            }
        });

    }
}