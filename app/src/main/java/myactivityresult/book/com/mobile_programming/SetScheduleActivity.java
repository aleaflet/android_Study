package myactivityresult.book.com.mobile_programming;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetScheduleActivity extends AppCompatActivity {
    String day;
    EditText EdtStartHour, EdtEndHour, EdtContent;
    int StartHour, EndHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
    }

    public void SaveSchedule(View v){
        // 요일과 시간을 설정
        RadioGroup rg = (RadioGroup) findViewById(R.id.group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkID) {
                RadioButton radio = (RadioButton) findViewById(checkID);
                day = radio.getText().toString();
            }
        });

        EdtStartHour = (EditText)findViewById(R.id.StartHour);
        EdtEndHour = (EditText)findViewById(R.id.EndHour);
        EdtContent = (EditText)findViewById(R.id.content);
        StartHour = Integer.parseInt(EdtStartHour.getText().toString());
        EndHour = Integer.parseInt(EdtEndHour.getText().toString());

        checkOverlap(StartHour, EndHour);
    }

    public void checkOverlap(int StartHour, int EndHour){
        // 일정이 겹치는지 확인
        boolean isOverlap =  false;
        SQLiteHelper sqh = new SQLiteHelper(this);
        Cursor cursor = sqh.Search(day);
        while(cursor.moveToNext()){
            int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int temp_end  = cursor.getInt(cursor.getColumnIndex(Table.StartTime));

            if( ((StartHour < temp_start) && (temp_start < EndHour)) ||
                    ((temp_start < StartHour) && (StartHour < temp_end)) ) {
                isOverlap = true;
                break;
            }
        }
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
}
