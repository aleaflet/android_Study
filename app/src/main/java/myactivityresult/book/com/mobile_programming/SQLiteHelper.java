package myactivityresult.book.com.mobile_programming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table.TableName + " (" +
                Table.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.Day + " TEXT, " +
                Table.StartTime + " INTEGER, " +
                Table.EndTime + " INTEGER, " +
                Table.Content + " TEXT" + ");" );
        Log.d("test", "SQLite onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Table.TableName + ";");  // 업그레이드 되면 이전 테이블 제거
        onCreate(db);  // 테이블의 새 인스턴스 생성
    }

    public void addSchedule(String day, int start_time, int end_time, String content){
        ContentValues contentvalues = new ContentValues();

        // ID는 자동으로 증가하기 때문에 입력할 필요 없음
        contentvalues.put(Table.Day, day);
        contentvalues.put(Table.StartTime, start_time);
        contentvalues.put(Table.EndTime, end_time);
        contentvalues.put(Table.Content, content);

        SQLiteDatabase sqlDB = getWritableDatabase();  // 읽고 쓸수 있는 데이터베이스를 가져옴
        sqlDB.insert(Table.TableName, Table.Day, contentvalues);
    }

    public void changeSchedule(String day, int start_time, int end_time, String content){
        SQLiteDatabase sqlDB = getWritableDatabase();

        Cursor cursor = Search(day);
        while(cursor.moveToNext()){
            int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));
            int temp_end  = cursor.getInt(cursor.getColumnIndex(Table.EndTime));

            if( ((start_time < temp_start) && (temp_start < end_time)) ||
                    ((temp_start < start_time) && (start_time < temp_end)) ) {
                String[] whereArgs = new String[] { day, String.valueOf(temp_start) };
                sqlDB.execSQL("delete from " + Table.TableName + " where "
                        + Table.Day + " = ? , " + Table.StartTime + " = ?;", whereArgs);  // 겹치는 스케줄 삭제
            }
        }

        addSchedule(day,start_time,end_time,content);  // 바뀐 스케줄 저장
    }

    public Cursor Search(String Day){
        SQLiteDatabase sqlDB = getWritableDatabase();
        String[] selectionArgs = new String[] { Day };
        Cursor cursor = sqlDB.rawQuery("select * from " + Table.TableName +
                " where " + Table.Day + " = ?;", selectionArgs);
        return cursor;
    }

    public Cursor getSchedule(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        String[] selectionArgs = new String[] { };
        Cursor cursor = sqlDB.rawQuery("select * from " + Table.TableName + ";", selectionArgs);
        return cursor;
    }
}
