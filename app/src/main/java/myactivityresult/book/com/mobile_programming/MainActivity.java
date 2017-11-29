package myactivityresult.book.com.mobile_programming;

import android.content.Intent;
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
            OutlineTextView textView = new OutlineTextView(this, true, 10.0f);
            textView.setText("번호 : " + i);
            TimeTable.addView(textView);
        }

        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        startService(intent);
    }

    // 시간표의 각 칸을 눌렀을 때 호출
    public void SetSchedule(View v){
        // 새로운 액티비티 화면을 띄워줌
        Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 디비 전체를 읽어서 저장한 요일에 해당하는 시간의 칸을 색을 바꾸고 내용을 표시

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
