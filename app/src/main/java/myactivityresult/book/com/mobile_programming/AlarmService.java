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
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

// 디비를 참고해서 일정 10분 전에 알람
public class AlarmService extends Service implements Runnable {
    boolean isRun = true;

    public IBinder onBind(Intent intent) {
        Log.d("서비스 =>", "onBind 호출됨");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("서비스 =>", "onStartCommand 호출됨");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d("서비스 =>", "onCreate 호출됨");
        Thread thread = new Thread();
        thread.start();

        super.onCreate();
    }

    public void run(){
        while (true){
            while(isRun){
                // 디비 체크해서 일정 시간을 확인한 후, 알림을 보여줌
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR);
                int minute =  now.get(Calendar.MINUTE);
                String today = getToday();  // 오늘 요일

                SQLiteHelper sqh = new SQLiteHelper(this);
                Cursor cursor = sqh.Search(today);
                while(cursor.moveToNext()) {
                    int temp_start = cursor.getInt(cursor.getColumnIndex(Table.StartTime));

                    if (hour == temp_start - 1)   // 10분 전 알림
                        if(minute == 50)
                            MakeNotification();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d("서비스 =>", "onDestroy 호출됨");
        super.onDestroy();
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
    }

    public String getToday(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);   // 일월화수목금토 = 7654321
        String today = "";

        switch(day){
            case 7:
                today = "일요일";
                break;
            case 6:
                today = "월요일";
                break;
            case 5:
                today = "화요일";
                break;
            case 4:
                today = "수요일";
                break;
            case 3:
                today = "목요일";
                break;
            case 2:
                today = "금요일";
                break;
            case 1:
                today = "토요일";
                break;
        }
        return today;
    }
}
