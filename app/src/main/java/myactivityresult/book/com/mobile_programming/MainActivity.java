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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어서오세요");
        builder.setMessage("실행하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("실행", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                painting();
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

    public void painting(){
        int position;

        for(int i=0; i<=83; i++){
            textViews[i].setBackgroundColor(Color.WHITE);
        }

        // 디비 전체를 읽어서 저장한 요일에 해당하는 시간의 칸을 색을 바꾸고 내용을 표시
        SQLiteHelper sqh = new SQLiteHelper(MainActivity.this);
        Cursor cursor = sqh.getSchedule();
        while (cursor.moveToNext()) {
            String day = cursor.getString(cursor.getColumnIndex(Table.Day));
            int start_time = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int end_time = cursor.getInt(cursor.getColumnIndex(Table.EndTime));
            String content = cursor.getString(cursor.getColumnIndex(Table.Content));

            // day와 time을 index로 시간표 칸을 지정해서 색칠하고 내용을 표시
            position = getPosition(day, start_time);
            // Log.d("시작",+start_time+"끝"+end_time );
            for(int i=position; i<=(end_time - start_time)*7+position; i=i+7) {
                textViews[i].setBackgroundColor(Color.BLUE);
                textViews[i].setText(content);
            }
        }
    }

    public int getPosition(String day, int start_time){
        int position;

        if(day.equals("월요일")){
            position = (start_time - 8) * 7 + 0;  // 텍뷰 행렬의 인덱스가 0번부터 시작하므로
        }
        else if(day.equals("화요일")){
            position = (start_time - 8) * 7 + 1;
        }
        else if(day.equals("수요일")){
            position = (start_time - 8) * 7 + 2;
        }
        else if(day.equals("목요일")){
            position = (start_time - 8) * 7 + 3;
        }
        else if(day.equals("금요일")){
            position = (start_time - 8) * 7 + 4;
        }
        else if(day.equals("토요일")){
            position = (start_time - 8) * 7 + 5;
        }
        else{
            position = (start_time - 8) * 7 + 6;
        }
        return position;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        painting();
    }

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



