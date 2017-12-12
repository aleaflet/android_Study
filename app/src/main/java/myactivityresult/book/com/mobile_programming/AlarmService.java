package myactivityresult.book.com.mobile_programming;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

// 디비를 참고해서 일정 10분 전에 알람
public class AlarmService extends Service implements Runnable {
    boolean isRun;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "서비스 onStartCommand 호출됨");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d("test", "서비스 onCreate 호출됨");
        setIsRun(true);
        Thread thread = new Thread(this);
        thread.start();

        super.onCreate();
    }

    public void run(){
        while (true){
            while(isRun){
                // 디비 체크해서 일정 시간을 확인한 후, 알림을 보여줌
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute =  now.get(Calendar.MINUTE);
                String today = getToday();  // 오늘 요일

                Log.d("test", "서비스 오늘 요일 체크 = " + today);
                SQLiteHelper sqh = new SQLiteHelper(getApplicationContext());
                Cursor cursor = sqh.Search(today);
                while(cursor.moveToNext()) {
                    int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));

                    Log.d("test","현재 시간 : " + hour + "/ 일정 시작 시간 : " + temp_start);
                    if (hour == (temp_start - 1))   // 10분 전 알림
                        if(minute == 16)
                            MakeNotification();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
            }
            try {  // 알람 띄우면서 서비스 중지 시키고 1분뒤에 재시작
                Thread.sleep(60000);
                setIsRun(true);
            } catch (InterruptedException e) {
            }
        }
    }

    public void MakeNotification(){
        Log.d("test","알람 띄우면서 서비스 중지");
        setIsRun(false); // 서비스 일시 중지

        Resources res = getResources();
        Intent intent = new Intent(this, MainActivity.class);  // 일정표 액티비티를 띄움
        int NotificationID = 12345;
        PendingIntent contentIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("스케줄러").setContentText("잠시 후 일정이 있습니다")
                .setTicker("잠시 후 일정이 있습니다").setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(contentIntent).setAutoCancel(true)
                .setWhen(System.currentTimeMillis()).setDefaults(Notification.DEFAULT_ALL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NotificationID, builder.build());
    }

    public void setIsRun(boolean run){
        isRun = run;
        Log.d("test", "서비스 setIsRun 호출, isRun = " + isRun);
    }

    public String getToday(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);   // 일월화수목금토 = 1234567
        String today = "";

        switch(day){
            case 1:
                today = "일요일";
                break;
            case 2:
                today = "월요일";
                break;
            case 3:
                today = "화요일";
                break;
            case 4:
                today = "수요일";
                break;
            case 5:
                today = "목요일";
                break;
            case 6:
                today = "금요일";
                break;
            case 7:
                today = "토요일";
                break;
        }
        return today;
    }

    @Override
    public void onDestroy() {
        Log.d("test", "서비스 onDestroy 호출됨");
        super.onDestroy();
    }
}
