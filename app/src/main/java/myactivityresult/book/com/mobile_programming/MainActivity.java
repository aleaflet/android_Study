package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView)findViewById(R.id.gridView01);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "번 칸 입니다", Toast.LENGTH_LONG).show();
                view.setBackgroundColor(Color.BLUE);

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
    }

    // 시간표의 빈 칸을 눌렀을 때 호출
    public void SetSchedule(View v){
        // 새로운 액티비티 화면을 띄워줌
        Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
        startActivityForResult(intent, 0);
    }

    // 시간표의 색칠된 칸을 눌렀을 때 호출
    public void ViewSchedule(View v){
        String content = "";
        Toast.makeText(MainActivity.this, "일정 : " + content, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 디비 전체를 읽어서 저장한 요일에 해당하는 시간의 칸을 색을 바꾸고 내용을 표시
        SQLiteHelper sqh = new SQLiteHelper(MainActivity.this);
        Cursor cursor = sqh.getSchedule();
        while (cursor.moveToNext()) {
            String day = cursor.getString(cursor.getColumnIndex(Table.Day));
            int start_time = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int end_time = cursor.getInt(cursor.getColumnIndex(Table.EndTime));
            String content = cursor.getString(cursor.getColumnIndex(Table.Content));

            // day와 time을 index로 시간표 칸을 지정해서 색칠하고 내용을 표시
        }
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

            return temp_textView;
        }
    }
}
