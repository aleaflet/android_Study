package myactivityresult.book.com.mobile_programming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {
    GridLayout TimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeTable = (GridLayout)findViewById(R.id.TimeTable);

        for(int i=1; i<105; i++){
            OutlineTextView textView = new OutlineTextView(this);
            TimeTable.addView(textView);
            TimeTable.setAlignmentMode();
        }
    }

    // 시간표의 각 칸을 눌렀을 때 호출
    public void SetSchedule(View v){
        // 다이얼로그 또는 새로운 액티비티 화면을 띄워줌

        // 설정한 시간과 일정 내용을 디비에 저장

        // 저장한 시간의 칸을 색을 바꾸고 내용을 표시시

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
