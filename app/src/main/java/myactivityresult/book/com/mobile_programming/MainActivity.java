package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    TextView[] textViews = new TextView[84];
    MainThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView)findViewById(R.id.gridView01);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String day = getDay(position);
                int time = getTime(position);
                String content = "";
                boolean isEmpty = true;

                SQLiteHelper sqh = new SQLiteHelper(MainActivity.this);
                Cursor cursor = sqh.Search(day); // 누른 칸의 요일의 일정을 받아옴
                while(cursor.moveToNext()){
                    int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
                    int temp_end  = cursor.getInt(cursor.getColumnIndex(Table.EndTime));

                    if( (temp_start <= time) && (time <= temp_end) ) { // 누른 칸의 시간이 일정에 포함되는지 확인
                        isEmpty = false;
                        content = cursor.getString(cursor.getColumnIndex(Table.Content));
                    }
                }

                if(isEmpty) {  // 비어있으면 일정 설정 화면으로
                    Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
                    startActivityForResult(intent, 0);
                }
                else{  // 비어있지 않으면 내용을 보여줌
                    Toast.makeText(getApplication(), content , Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        startService(intent);
        Log.d("test","서비스 실행");
    }

    @Override
    protected void onResume() {
        super.onResume();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어서오세요");
        builder.setMessage("실행하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("실행", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                thread = new MainThread(getApplicationContext(), textViews);
                thread.start();
            }
        });
        builder.setNegativeButton("종료", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStop() {
        thread.interrupt();
        super.onStop();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MainThread.painting();
    }*/

    public int getTime(int position){
        int temp = position / 7;
        int start_time=0;
        switch (temp){
            case 0:
                start_time = 8;
                break;
            case 1:
                start_time = 9;
                break;
            case 2:
                start_time = 10;
                break;
            case 3:
                start_time = 11;
                break;
            case 4:
                start_time = 12;
                break;
            case 5:
                start_time = 13;
                break;
            case 6:
                start_time = 14;
                break;
            case 7:
                start_time = 15;
                break;
            case 8:
                start_time = 16;
                break;
            case 9:
                start_time = 17;
                break;
            case 10:
                start_time = 18;
                break;
            case 11:
                start_time = 19;
                break;
        }
        return start_time;
    }
    public String getDay(int position){
        String day = "";
        int temp = position % 7;
        switch (temp){
            case 0 :
                day = "월요일";
                break;
            case 1 :
                day = "화요일";
                break;
            case 2 :
                day = "수요일";
                break;
            case 3 :
                day = "목요일";
                break;
            case 4 :
                day = "금요일";
                break;
            case 5 :
                day = "토요일";
                break;
            case 6 :
                day = "일요일";
                break;
        }
        return day;
    }

    class ImageAdapter extends BaseAdapter {
        Context mContext;

        public ImageAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount(){
            return 84;  // 시간표 총 84칸
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView temp_textView = new TextView(getApplicationContext());
            temp_textView.setTextColor(Color.RED);
            temp_textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView textView2 = (TextView)findViewById(R.id.time);
            int temp = textView2.getHeight();

            temp_textView.setHeight(temp-2);
            temp_textView.setBackgroundColor(Color.WHITE);

            if(textViews[position] == null) {
                textViews[position] = temp_textView;
            }
            return temp_textView;
        }
    }
}



