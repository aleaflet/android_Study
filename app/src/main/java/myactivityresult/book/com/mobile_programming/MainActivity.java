package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

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
                //Toast.makeText(MainActivity.this, position + "번 칸 입니다", Toast.LENGTH_LONG).show();
                //view.setBackgroundColor(Color.BLUE);

                Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
                startActivityForResult(intent, 0);

                switch (position){
                    case 0 :
                        break;
                    default :
                        break;
                }
            }
        });

        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        startService(intent);
        Log.d("test","서비스 실행");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position;

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
            for(int i=position; i<(position + end_time - start_time); i++) {
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            temp_textView.setText(position + "번");
            temp_textView.setTextColor(Color.RED);
            temp_textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView textView2 = (TextView)findViewById(R.id.time);
            int temp = textView2.getHeight();

            temp_textView.setHeight(temp-2);
            temp_textView.setBackgroundColor(Color.WHITE);

            textViews[position] = temp_textView;
            return temp_textView;
        }
    }
}
