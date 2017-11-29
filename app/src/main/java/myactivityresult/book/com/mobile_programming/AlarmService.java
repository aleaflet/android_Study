package myactivityresult.book.com.mobile_programming;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

// 디비를 참고해서 일정 10분 전에 알람
public class AlarmService extends Service {
    private final IBinder bind = new LocalBinder();

    public class LocalBinder extends Binder{
        public AlarmService getService(){
            return AlarmService.this;
        }
    }
    public IBinder onBind(Intent intent) {
        Log.d("서비스 =>", "onBind 호출됨");
        return bind;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("서비스 =>", "onStartCommand 호출됨");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d("서비스 =>", "onCreate 호출됨");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("서비스 =>", "onDestroy 호출됨");
        super.onDestroy();
    }
}
