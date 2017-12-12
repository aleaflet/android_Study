package myactivityresult.book.com.mobile_programming;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class MainThread extends Thread {
    TextView[] textViews = new TextView[84];
    private Handler handler;
    private Context mContext;

    public MainThread(Context mContext, TextView[] textViews){
                this.mContext = mContext;
                this.textViews = textViews;
                handler = new Handler();
            }

        public void run(){
            while(true) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    painting();
                }
            });
            try{
                Thread.sleep(5000);
            } catch(InterruptedException e){
                Log.d("Exception","InterruptedException");
            }
        }
    }

    public void painting(){
        int position;

        // 디비 전체를 읽어서 저장한 요일에 해당하는 시간의 칸을 색을 바꾸고 내용을 표시
        SQLiteHelper sqh = new SQLiteHelper(mContext);
        Cursor cursor = sqh.getSchedule();
        while (cursor.moveToNext()) {
            String day = cursor.getString(cursor.getColumnIndex(Table.Day));
            int start_time = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int end_time = cursor.getInt(cursor.getColumnIndex(Table.EndTime));
            String content = cursor.getString(cursor.getColumnIndex(Table.Content));

            // day와 time을 index로 시간표 칸을 지정해서 색칠하고 내용을 표시
            position = getPosition(day, start_time);
            Log.d("시작",+start_time+"끝"+end_time );
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
}
