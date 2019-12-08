package halla.icsw.book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String DB_NAME="bookbook.db";

    private CustomAdapter adapter;
    private LinearLayout audio;
    private LinearLayout book;
    private ListView audioListView;
    private ListView bookListView;
    private ListView bestRankListView;
    private ListView bookListView1;
    private ListView bookListView2;
    private ListView bookListView3;
    private ListView bookListView4;
    private ListView bookListView5;
    AlertDialog.Builder builder;
    TextView read,read2;
    int count;

    EditText searchText;

    //파싱하기 위한 String
    String strHtml = "";
    String strHtml1 = "";
    String strHtml2 = "";
    String strHtml3 = "";
    String strHtml4 = "";
    String strHtml5 = "";

    //파싱 리스트뷰 롱크릭 String
    String strLong1 = "";
    String strLong2 = "";
    String strLong3 = "";
    String strLong4 = "";
    String strLong5 = "";
    String strLong6 = "";
    String lon;
    ArrayList<String> arrayList;
    ArrayList<String> arrayList2;



    String[] data2= new String[21];
    String[] data= new String[21];
    SQLiteDatabase db;
    Cursor cursor;


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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        read = findViewById(R.id.read);
        read2 = findViewById(R.id.read2);
        db=openOrCreateDatabase(DB_NAME , Context.MODE_PRIVATE, null);

        cursor=db.rawQuery("SELECT 명언,사람 FROM 책;", null);
        arrayList = new ArrayList<String>();
        arrayList2 = new ArrayList<String>();
        while (cursor.moveToNext()){
            String a = cursor.getString(0);
            arrayList.add(a);
            String b = cursor.getString(1);
            arrayList2.add(b);
        }

            Timer timer = new Timer();
            TimerTask time = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            read.setText(arrayList.get(count));
                            read2.setText(arrayList2.get(count));
                            count++;
                            if(count == arrayList.size()){
                                count = 0;
                            }
                        }
                    });
                }
            };
            timer.schedule(time,1,4000);


        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) copyDatabase(dbFile);

        //로그인 회원 정보 db 불러옴
//        helper = new dbHelper(MainActivity.this);
//        db = helper.getReadableDatabase();
//        String sql = "select * from 회원";
//        Cursor cursor = db.rawQuery(sql,null);
//        while(cursor.moveToNext()){
//            String id = cursor.getString(0);
//            String pass = cursor.getString(1);
//        }






        //글쓰기 버튼
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BookReport.class);
                intent.putExtra("aa","독후감");
                startActivity(intent);
            }
        });

        //베스트 셀러 파싱하기 위한 핸들러
        Handler h1 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing();
            } };
        new WorkerThread1(h1).start();

        //소설 파싱하기 위한 핸들러
        Handler h2 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing2();
            } };
        new WorkerThread2(h2).start();

        //시 파싱하기 위한 핸들러
        Handler h3 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing3();
            } };
        new WorkerThread3(h3).start();

        //자기계발 파싱하기 위한 핸들러
        Handler h4 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing4();
            } };
        new WorkerThread4(h4).start();

        //인문학 파싱하기 위한 핸들러
        Handler h5 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing5();
            } };
        new WorkerThread5(h5).start();

        //예술,대중문화 파싱하기 위한 핸들러
        Handler h6 = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing6();
            } };
        new WorkerThread6(h6).start();


        //findViewByID부분
        TextView bookButton=findViewById(R.id.bookButton);
        TextView audioButton=findViewById(R.id.audioButton);
        Button bestRankButton=findViewById(R.id.bestRankButton);
        Button genre1 = findViewById(R.id.genre1);
        Button genre2 = findViewById(R.id.genre2);
        Button genre3 = findViewById(R.id.genre3);
        Button genre4 = findViewById(R.id.genre4);
        Button genre5 = findViewById(R.id.genre5);
        Button mapButton = findViewById(R.id.mapButton);
        TextView signUpButton = findViewById(R.id.signUpText);
        TextView logInButton = findViewById(R.id.loginText);
        Button myPageButton = findViewById(R.id.myPageButton);
        Button gameButton = findViewById(R.id.gameButton);

        book=findViewById(R.id.book);
        audio=findViewById(R.id.audio);
        audioListView=findViewById(R.id.audioListView);
        bookListView=findViewById(R.id.bookListView);
        bestRankListView=findViewById(R.id.bestRankListView);
        bookListView1=findViewById(R.id.bookListView1);
        bookListView2=findViewById(R.id.bookListView2);
        bookListView3=findViewById(R.id.bookListView3);
        bookListView4=findViewById(R.id.bookListView4);
        bookListView5=findViewById(R.id.bookListView5);

        //검색 Focus 자동 키보드 삭제
//        searchText.clearFocus();

        //회원가입 버튼 클릭시
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭시
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
        });

        //오디오button 클릭시 스크롤뷰가 보이게함, 안보이게 함
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
                audioListView.setVisibility(View.VISIBLE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.INVISIBLE);
                bookListView2.setVisibility(View.INVISIBLE);
                bookListView3.setVisibility(View.INVISIBLE);
                bookListView4.setVisibility(View.INVISIBLE);
                bookListView5.setVisibility(View.INVISIBLE);
            }
        });

        //북button 클릭시 스크롤뷰가 보이게함, 안보이게 함
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.setVisibility(View.GONE);
                book.setVisibility(View.VISIBLE);
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.VISIBLE);
                bookListView1.setVisibility(View.INVISIBLE);
                bookListView2.setVisibility(View.INVISIBLE);
                bookListView3.setVisibility(View.INVISIBLE);
                bookListView4.setVisibility(View.INVISIBLE);
                bookListView5.setVisibility(View.INVISIBLE);
            }
        });

        //베스트셀러button 클릭시
        bestRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTMLParsing();
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.VISIBLE);
                bookListView1.setVisibility(View.GONE);
                bookListView2.setVisibility(View.GONE);
                bookListView3.setVisibility(View.GONE);
                bookListView4.setVisibility(View.GONE);
                bookListView5.setVisibility(View.GONE);
            }
        });
        //소설 button 클릭시
        genre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTMLParsing2();
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.VISIBLE);
                bookListView2.setVisibility(View.GONE);
                bookListView3.setVisibility(View.GONE);
                bookListView4.setVisibility(View.GONE);
                bookListView5.setVisibility(View.GONE);
            }
        });
        //시 button 클릭시
        genre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTMLParsing3();
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.GONE);
                bookListView2.setVisibility(View.VISIBLE);
                bookListView3.setVisibility(View.GONE);
                bookListView4.setVisibility(View.GONE);
                bookListView5.setVisibility(View.GONE);
            }
        });
        //자기계발 button 클릭시
        genre3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.GONE);
                bookListView2.setVisibility(View.GONE);
                bookListView3.setVisibility(View.VISIBLE);
                bookListView4.setVisibility(View.GONE);
                bookListView5.setVisibility(View.GONE);
            }
        });
        //인문학 button 클릭시
        genre4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.GONE);
                bookListView2.setVisibility(View.GONE);
                bookListView3.setVisibility(View.GONE);
                bookListView4.setVisibility(View.VISIBLE);
                bookListView5.setVisibility(View.GONE);
            }
        });
        //예술,대중문화 button 클릭시
        genre5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
                bookListView1.setVisibility(View.GONE);
                bookListView2.setVisibility(View.GONE);
                bookListView3.setVisibility(View.GONE);
                bookListView4.setVisibility(View.GONE);
                bookListView5.setVisibility(View.VISIBLE);
            }
        });

        //mapButton 클릭시
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        //myPageButton 클릭시
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String nameIntent = intent1.getStringExtra("name");
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                intent.putExtra("name1",nameIntent);
                startActivity(intent);
            }
        });

        //오디오북 list받아오고, tts기능까지 Intent 사용하여 넘겨줌.
        adapter = new CustomAdapter();
        audioListView = (ListView) findViewById(R.id.audioListView);
        setData();
        audioListView.setAdapter(adapter);
        audioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String titleRes = ((CustomDTO)adapter.getItem(position)).getTitle();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("titleRes", titleRes);
                startActivity(intent);
            }
        });

    }

    //검색 버튼
    public void searchOnclick(View view){
        searchText = findViewById(R.id.searchText);

        String searchStr = searchText.getText().toString();
        if (searchStr.equals("")){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("검색").setMessage("검색어를 입력해주세요");
            AlertDialog alertDialog = builder.show();
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.aler);
            alertDialog.show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, GenreSearchActivity.class);
            intent.putExtra("search", searchStr);
            startActivity(intent);
        }
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    //오디오북 데이타
    private void setData() {
        TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);

        for (int i = 0; i < arrResId.length(); i++) {
            CustomDTO dto = new CustomDTO();
            dto.setResId(arrResId.getResourceId(i, 0));
            dto.setTitle(titles[i]);
            dto.setContent(contents[i]);
            adapter.addItem(dto);
        }
    }

    //베스트셀러 파싱을 위한 스레드
    class WorkerThread1 extends Thread {
        Handler h1;
        String strLine;
        WorkerThread1(Handler h1) { this.h1 = h1; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?BranchType=1&start=momain");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml += strLine;
                in.close();
                h1.sendMessage(new Message());
            } catch (Exception e) {
               Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //소설 파싱을 위한 스레드
    class WorkerThread2 extends Thread {
        Handler h2;
        String strLine;
        WorkerThread2(Handler h2) { this.h2 = h2; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?ViewRowsCount=50&ViewType=Detail&SortOrder=2&page=1&PublishDay=84&BranchType=1&BestType=Bestseller&CID=50917&MaxPageIndex=20&VType=0");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml1 += strLine;
                in.close();
                h2.sendMessage(new Message());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //시 파싱을 위한 스레드
    class WorkerThread3 extends Thread {
        Handler h3;
        String strLine;
        WorkerThread3(Handler h3) { this.h3 = h3; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?ViewRowsCount=50&ViewType=Detail&SortOrder=2&page=1&PublishDay=84&BranchType=1&BestType=Bestseller&CID=50940&MaxPageIndex=20&VType=0");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml2 += strLine;
                in.close();
                h3.sendMessage(new Message());
            } catch (Exception e) {
               Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //자기계발 파싱을 위한 스레드
    class WorkerThread4 extends Thread {
        Handler h4;
        String strLine;
        WorkerThread4(Handler h4) { this.h4 = h4; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?ViewRowsCount=50&ViewType=Detail&SortOrder=2&page=1&PublishDay=84&BranchType=1&BestType=Bestseller&CID=336&MaxPageIndex=20&VType=0");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml3 += strLine;
                in.close();
                h4.sendMessage(new Message());
            } catch (Exception e) {
               Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //인문 파싱을 위한 스레드
    class WorkerThread5 extends Thread {
        Handler h5;
        String strLine;
        WorkerThread5(Handler h5) { this.h5 = h5; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?ViewRowsCount=50&ViewType=Detail&SortOrder=2&page=1&PublishDay=84&BranchType=1&BestType=Bestseller&CID=656&MaxPageIndex=20&VType=0");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml4 += strLine;
                in.close();
                h5.sendMessage(new Message());
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //예술 파싱을 위한 스레드
    class WorkerThread6 extends Thread {
        Handler h6;
        String strLine;
        WorkerThread6(Handler h6) { this.h6 = h6; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?ViewRowsCount=50&ViewType=Detail&SortOrder=2&page=1&PublishDay=84&BranchType=1&BestType=Bestseller&CID=517&MaxPageIndex=20&VType=0");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml5 += strLine;
                in.close();
                h6.sendMessage(new Message());
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    //베스트셀러 파싱
    void HTMLParsing() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml.indexOf(" style=\"line-height:120%;", end);
                end = strHtml.indexOf("<", start);
                data[i] = i + "위 : " + strHtml.substring(start + 31, end)+" ";
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bestRankListView.setAdapter(adapter);

        //리스트뷰 item 클릭시
        bestRankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong1 += itemString1.substring(start2+4,end2);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });

        bestRankListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong1 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong1);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong1+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //소설 파싱
    void HTMLParsing2() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml1.indexOf(" style=\"line-height:120%;", end);
                end = strHtml1.indexOf("<", start);
                data[i] = i + "위 : " + strHtml1.substring(start + 31, end)+" ";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bookListView1.setAdapter(adapter);
        //리스트뷰 item 클릭시
        bookListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();
                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });
        bookListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong2 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong2);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong2+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //시 파싱
    void HTMLParsing3() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml2.indexOf(" style=\"line-height:120%;", end);
                end = strHtml2.indexOf("<", start);
                data[i] = i + "위 : " + strHtml2.substring(start + 31, end)+" ";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bookListView2.setAdapter(adapter);
        //리스트뷰 item 클릭시
        bookListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();
                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });
        bookListView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong2 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong2);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong2+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //자기계발 파싱
    void HTMLParsing4() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml3.indexOf(" style=\"line-height:120%;", end);
                end = strHtml3.indexOf("<", start);
                data[i] = i + "위 : " + strHtml3.substring(start + 31, end)+" ";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bookListView3.setAdapter(adapter);
        //리스트뷰 item 클릭시
        bookListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();
                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });
        bookListView3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong4 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong4);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong4+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //인문 파싱
    void HTMLParsing5() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml4.indexOf(" style=\"line-height:120%;", end);
                end = strHtml4.indexOf("<", start);
                data[i] = i + "위 : " + strHtml4.substring(start + 31, end)+" ";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bookListView4.setAdapter(adapter);
        //리스트뷰 item 클릭시
        bookListView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();
                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });
        bookListView4.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong5 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong5);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong5+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //예술 파싱
    void HTMLParsing6() {
        try {
            int start = 0, end = 0;
            for (int i = 1; i <= 20; i++) {
                start = strHtml5.indexOf(" style=\"line-height:120%;", end);
                end = strHtml5.indexOf("<", start);
                data[i] = i + "위 : " + strHtml5.substring(start + 31, end)+" ";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bookListView5.setAdapter(adapter);
        //리스트뷰 item 클릭시
        bookListView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();
                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                Toast.makeText(getApplicationContext(), itemString1.substring(start2+4,end2), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=m_book&sm=mtb_jum&query="+itemString1.substring(start2+4,end2)));
                startActivity(intent);
            }
        });
        bookListView5.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                int start2 = 0, end2 = 0;

                String itemString1 = adapter.getItemAtPosition(position).toString();

                start2 = itemString1.indexOf("위 : ", end2);
                end2 = itemString1.indexOf(" ",itemString1.length()-1);

                strLong6 = itemString1.substring(start2+4,end2);

                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                intent.putExtra("bookName", strLong6);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),strLong6+"이 북마크에 추가됩니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void gameb(View v){
        final CharSequence[] games = { "책 제목 나열하기", "제목 이어 말하기"};
        builder = new AlertDialog.Builder(this);
        builder.setTitle("게임");
        builder.setItems(games,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        if (games[id].equals("책 제목 나열하기")){
                            Intent intent = new Intent(MainActivity.this,Game1Activity.class);
                            startActivity(intent);
                        }
                        if (games[id].equals("제목 이어 말하기")){
                            Intent intent = new Intent(MainActivity.this,Game2Activity.class);
                            startActivity(intent);
                        }
                        //dialog.dismiss();
                    }
                });


        AlertDialog alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.aler);
        alertDialog.show();
    }




}
