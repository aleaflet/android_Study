package myactivityresult.book.com.mobile_programming;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SetScheduleActivity extends AppCompatActivity {
    String day;
    EditText EdtContent;
    int StartHour, EndHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        NumberPicker picker1 = (NumberPicker)findViewById(R.id.picker1);

        picker1.setMinValue(8);
        picker1.setMaxValue(19);
        picker1.setWrapSelectorWheel(false);

        final NumberPicker picker2 = (NumberPicker)findViewById(R.id.picker2);
        picker2.setMinValue(8);
        picker2.setMaxValue(19);
        picker2.setValue(9);
        picker2.setWrapSelectorWheel(false);

        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(SetScheduleActivity.this, "Value : " + newVal, Toast.LENGTH_SHORT).show();
                StartHour = newVal;

                picker2.setMinValue(StartHour+1 );
                picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int end_old, int new_end ) {
                        EndHour = new_end;
                        Log.d("test","test"+ EndHour);
                    }
                });
            }
        });
        NumberPicker picker3 = (NumberPicker)findViewById(R.id.picker3);
        picker3.setMinValue(0);
        picker3.setMaxValue(6);
        day = "일요일";
        picker3.setDisplayedValues(new String[]{ "일요일", "월요일", "화요일", "수요일",
                "목요일", "금요일", "토요일"});
        picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old_day, int new_day ) {
                switch(new_day){
                    case 0:
                        day = "일요일";
                        break;
                    case 1:
                        day = "월요일";
                        break;
                    case 2:
                        day = "화요일";
                        break;
                    case 3:
                        day = "수요일";
                        break;
                    case 4:
                        day = "목요일";
                        break;
                    case 5:
                        day = "금요일";
                        break;
                    case 6:
                        day = "토요일";
                        break;
                }
            }
        });
    }

    public void SaveSchedule(View v){
        // 요일과 시간을 설정
        EdtContent = (EditText)findViewById(R.id.content);

        Log.d("test", "SaveSchedule 실행//요일 : " + day + "/시작시간 : " + StartHour +
                "/종료시간 : " + EndHour + "/내용 : " + EdtContent.getText().toString());
        checkOverlap(StartHour, EndHour);
    }

    public void checkOverlap(int StartHour, int EndHour){
        // 일정이 겹치는지 확인
        boolean isOverlap =  false;
        SQLiteHelper sqh = new SQLiteHelper(this);
        Cursor cursor = sqh.Search(day);
        while(cursor.moveToNext()){
            int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int temp_end  = cursor.getInt(cursor.getColumnIndex(Table.EndTime));

            Log.d("test","temp_start : " + temp_start + "temp_end : " + temp_end
                    +"/ StartHour : " + StartHour + "EndHour : " + EndHour );
            if( ((StartHour <= temp_start) && (temp_start <= EndHour)) ||
                    ((temp_start <= StartHour) && (StartHour <= temp_end)) ) {
                isOverlap = true;
                break;
            }
        }
        Log.d("test", "checkOverlap() 실행시켜서 중복여부 확인 isOverlap=" + isOverlap);
        save(isOverlap);
    }

    public void save(boolean isOverlap){
        if(isOverlap){  // 중복인데 변경할 것이냐 물어보고 판단
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("일정 중복");
            builder.setMessage("일정을 변경하시겠습니까?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("변경", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    SQLiteHelper sqh = new SQLiteHelper(SetScheduleActivity.this);
                    sqh.changeSchedule(day, StartHour, EndHour, EdtContent.getText().toString());
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){ }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{    // 디비에 저장
            SQLiteHelper sqh = new SQLiteHelper(SetScheduleActivity.this);
            sqh.addSchedule(day, StartHour, EndHour, EdtContent.getText().toString());
        }
    }

    public void End(View v){
        finish();
    }
}
