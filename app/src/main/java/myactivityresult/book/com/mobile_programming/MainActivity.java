package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    ScreenRate screen_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 디바이스 실제 크기를 가져옴 */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y;
        screen_rate = new ScreenRate(ScreenWidth, ScreenHeight);
        screen_rate.setSize(100, 100); // 가상 화면 크기 설정

        gridView = (GridView)findViewById(R.id.gridView01);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.BLACK);
                int color = view.getDrawingCacheBackgroundColor();
                Log.d("test", ""+color);

                switch (position){
                    case 0 :
                        break;
                    default :
                        break;
                }
            }
        });

        /*
        for(int i=1; i<105; i++){
            OutlineTextView textView = new OutlineTextView(this, true, 10.0f);
            textView.setText("번호 : " + i);
            TimeTable.addView(textView);
        }
        */

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
        private Integer FunctionImage = R.drawable.block;

        Context mContext;

        public ImageAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount(){
            return 105;  // 시간표 총 105칸
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
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams( // 아이콘 이미지 영역 size 설정
                        screen_rate.getX(13), screen_rate.getY(7))); // 화면 비율에 따라서 크기 조절
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // 이미지 자체 size 조정 및 이동
                imageView.setPadding(5,7,3,7);  // 상하좌우 여백
            }
            else{
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(FunctionImage);

            return imageView;
        }
    }
}
