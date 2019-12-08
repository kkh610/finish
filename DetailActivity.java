package halla.icsw.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {

    private Intent intent;
    private TextView textView;
    private TextView textView2;

    //센서
    SensorManager m; Sensor s;

    //센서 x,y,z축
    private float angle;
    private float pitch;
    private float roll;

    String read;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        tts = new TextToSpeech(this, this);

        // MainActivity에서 보낸 imgRes를 받기위해 getIntent()로 초기화
        intent = getIntent();

        // "imgRes" key로 받은 값은 int 형이기 때문에 getIntExtra(key, defaultValue);
        // 받는 값이 String 형이면 getStringExtra(key);
        textView.setText(intent.getStringExtra("titleRes"));

        Toast.makeText(getApplicationContext(),"저작권으로 인해 일부분 미리듣기입니다.",Toast.LENGTH_LONG).show();

        //센서 코드
        m = (SensorManager) getSystemService(SENSOR_SERVICE);
        s = m.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if(s==null) {
            Toast.makeText(this,"방향센서 없음-> 프로그램 종료",Toast.LENGTH_LONG).show();
            finish();
        }
        m.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);

        //tts
        String str = intent.getStringExtra("titleRes");

        if (textView.toString() != null) {
            if (str.equals("모든 순간이 너였다.")) {
                textView2.setText("\n동화 같은 사랑\n\n결국 연애라는 건 추구하는 연애와 가치관이 비슷한 사람끼리 해야한다.\n" +
                        "누구나 동화 같은 사랑 속의 주인공을 꿈꾸지만 서로 자신이 생각하는 스토리가 옳다고 우기다 보면\n" +
                        "필히 한쪽은 악당이 되고야 말테니까.");
            } else if (str.equals("아무것도 아닌 지금은 없다")) {
                textView2.setText("앞이 깜깜하고 앞이 보이지 않을 때가 있죠.\n\n" +
                        "오늘이 그랬나요.\n\n" +
                        "그랬다면 당신은 대단한 거에요.\n\n" +
                        "그 힘든 하루를 또 참아냈으니까");
            } else if (str.equals("말그릇")) {
                textView2.setText("내게는 만나면 힐링이 되는 사람이 있다.\n" +
                        "상대를 보며 내 처지를 비관하지도 않고\n" +
                        "위안 삼지도 않게 되는\n" +
                        "온전히 마음으로 만나게 되는 사람.\n" +
                        "그래서 빡빡한 내 삶에 용기를 주고\n" +
                        "다시 열심히 살아보자고 다잡게 해주는 사람\n" +
                        "특별히 내게 충고나 조언을 하지 않는데도\n" +
                        "그냥 수다만으로도 마음이 편해지는 사람");
            } else if (str.equals("어른아이로 산다는 것")) {
                textView2.setText("이전에 느끼지 못했던 것들을 새롭게 느끼고.\n" +
                        "사소한 것들의 소중한 의미를 깨우쳐가면서 우리는\n" +
                        "조금씩 어른이 되어가고 있는 게 아닐까.");
            }
        }

        tts.speak(textView2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            angle = event.values[0];
            pitch = event.values[1]; // y축에 적용되는 힘
            roll = event.values[2]; // z축에 적용되는 힘

            if (pitch >= -160 && pitch <= -90 || pitch >= 90 && pitch <= 160 || roll == 0) {
                tts.stop();
            }
        }
        else {
            speak(textView2.getText().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (tts != null) tts.shutdown();
    }

    public void speak(String str){
        tts.speak(textView2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS)
        {
            Locale locale = Locale.getDefault();
            if(tts.isLanguageAvailable(locale)>=TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(locale);
            else
                Toast.makeText(this, "지원하지 않는 오류", Toast.LENGTH_SHORT).show();
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "음성 합성 초기화 오류", Toast.LENGTH_SHORT).show();
        }

        speak(textView2.getText().toString());

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
