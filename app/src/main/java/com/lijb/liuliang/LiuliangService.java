package com.lijb.liuliang;

import android.app.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijb on 2017/3/28.
 */

public class LiuliangService extends Service {
    NotificationManager mNotificationManager;
Notification mNotification;
     NotificationCompat.Builder builder;
    long  preDown = 0;//记录上一次的
    long preUp = 0;
    int preQQ = 0;
    public static  final  int MSG=0;
    private int level=0;
    private String up= "0";
    private String down ="0";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG:
                    int uid = android.os.Process.myUid();
//                    Log.e("123","uid "+uid);
                    /** 获取手机通过 2G/3G 接收的字节流量总数 */
                    long mobileDown=TrafficStats.getMobileRxBytes();
//                    Log.e("123","mobileDown = "+ mobileDown);
                    /** 获取手机通过 2G/3G 接收的数据包总数 */
                    TrafficStats.getMobileRxPackets();
                    /** 获取手机通过 2G/3G 发出的字节流量总数 */
                    long mobileUp =TrafficStats.getMobileTxBytes();
//                    Log.e("123","mobileUp = "+mobileUp);
                    /** 获取手机通过 2G/3G 发出的数据包总数 */
                    TrafficStats.getMobileTxPackets();
                    /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
                    long DownAll =  TrafficStats.getTotalRxBytes();
                   // Log.e("123","DownAll = "+ DownAll);
                    /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
                    TrafficStats.getTotalRxPackets();
                    /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
                    long upAll =TrafficStats.getTotalTxBytes();
                   // Log.e("123","upAll = "+upAll );
                    /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
                    TrafficStats.getTotalTxPackets();
                    /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
                    TrafficStats.getUidRxBytes(uid);
                    /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
                  long qq=  TrafficStats.getUidTxBytes(10211);
//                    Log.e("123","qq "+qq);
                    int qqLiuLiang=(int)(qq/1024);
//                    Log.e("123","qqLiuLiang "+qqLiuLiang );

                    int qqDown = qqLiuLiang-preQQ;
//                    Log.e("123","qqDown "+qqDown );
                    preQQ=qqLiuLiang;
                    long downLong =DownAll;
                    long upLong =upAll;
                    Log.e("123","down = "+(downLong-preDown) );
                    Log.e("123","up = "+(upAll-preUp) );
//                    int downLiuliang =down-preDown;
//                    int upLiuliang =up-preUp;
                    getNotificationInfo(downLong-preDown,upAll-preUp,false);
                    preDown=downLong;
                    preUp=upLong;
//                    Log.e("123","downLiuliang = "+downLiuliang );
//                    Log.e("123","upLiuliang = "+upLiuliang );
//                     更新通知栏

//                    RemoteViews view_custom = new RemoteViews(LiuliangService.this.getPackageName(),
//                            R.layout.pushmessage_view_custom);
//                    view_custom.setImageViewBitmap(R.id.custom_icon, toConformBitmap());
//                    view_custom.setImageViewBitmap(R.id.small_icon, toConformBitmap());
//                    view_custom.setTextViewText(R.id.tv_custom_title, "下载: "+(downLiuliang)+"KB/s  "+"上传 :"+(upLiuliang)+"KB/s");
//                    view_custom.setTextViewText(R.id.tv_custom_content, "网络流量监控");
//                    builder.setContent(view_custom);
//                    long random = (long)( Math.random()*10000+1);
//                    Log.e("123","random " +random);
//
//                    Log.e("123","random "+random);
//                    switch (random){
//                        case 1:
//                            builder.setSmallIcon(R.drawable.not_ic_kb_s_9900,1);
//                            break;
//                        case 2:
//                            builder.setSmallIcon(R.drawable.not_ic_kb_s_9900,2);
//                            break;
//                        case 3:
//                            builder.setSmallIcon(R.drawable.not_ic_kb_s_9900,3);
//                            break;
//
//                    }


                        builder.setSmallIcon(R.drawable.ic_kb_s_9900,level);
                    builder.setSmallIcon(R.drawable.ic_kb_s_9900,level);

                    BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
                    builder.setLargeIcon(bd.getBitmap());
                    builder.setContentTitle("下载: "+(down)+"KB/s  "+"上传 :"+(up)+"KB/s");
                    builder.setContentText("网络流量监控");
                    builder.setTicker("Mobile:"+"20MB"+ "  WIFI:"+"700MB");
                    mNotification =builder.build();
                    mNotificationManager.notify(0, mNotification);// 通知一下才会生效哦
                    break;
            }
        }
    };
    private void  getNotificationInfo(long downLong ,long upLong,boolean isKB){
        int  downInt =0;
        int  upInt =0;
        if (isKB){
            downInt = (int) (downLong/1024);
            upInt = (int) (downLong/1024);
            Log.e("123","downInt "+downInt);
            Log.e("123","upInt "+upInt);

        }else{

            downInt = (int) (downLong/1024)*8;
            upInt = (int) (downLong/1024)*8;
        }

//下载
         if(upInt<1000&&upInt>=0){
            level=downInt;
             down=String.valueOf(downInt);
             Log.e("123","level "+level);
         } else if (upInt>=1000&&upInt<9950){
             DecimalFormat df = new DecimalFormat("#.0");
             String str=df.format(upInt/1024f);
             Log.e("123","leverStrDown "+str);
             str = str.replace(".", "");
             int z =Integer.parseInt(str)*100;
             Log.e("123","zDown = "+z);
             up=str;
         }else if (level>=9950){
             up="10+";
         }
//上传
        if(downInt>1000&&downInt<=0){
            level=downInt;
            down=String.valueOf(downInt);
        } else if (downInt>=1000&&downInt<9950){
            DecimalFormat df = new DecimalFormat("#.0");
            String str=df.format(downInt/1024f);
            Log.e("123","leverStrUp "+str);
            str = str.replace(".", "");
            int z =Integer.parseInt(str)*100;
            Log.e("123","zUp = "+z);
            level=z;
            down=str;
        }else if (level>=9950){
            level=1000;
            down="10+";
        }
    }




//    public   Bitmap toConformBitmap() {
//
//        int bgHeight= getResources().getDrawable(R.drawable.not_num_1).getIntrinsicHeight();
//        int fgHeight= getResources().getDrawable(R.drawable.not_kb_s).getIntrinsicHeight();
//        int bgWidth= getResources().getDrawable(R.drawable.not_kb_s).getIntrinsicWidth();
//        int fgWidth= getResources().getDrawable(R.drawable.not_num_2).getIntrinsicWidth();
////        int bgWidth = background.getWidth();
////        int bgHeight = background.getHeight();
////        int fgWidth = foreground.getWidth();
////        int fgHeight = foreground.getHeight();
//        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
////        Bitmap newbmp1 = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
////        Bitmap newbmp2 = Bitmap.createBitmap(fgWidth, bgHeight, Bitmap.Config.ARGB_8888);
//
//
//        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.not_num_1);
//        Bitmap newbmp1 = bd.getBitmap();
//
//        BitmapDrawable bd2 = (BitmapDrawable) getResources().getDrawable(R.drawable.not_num_2);
//        BitmapDrawable bd3= (BitmapDrawable) getResources().getDrawable(R.drawable.not_kb_s);
//        Bitmap newbmp2 = bd2.getBitmap();
//
//        Bitmap newbmp3 = bd3.getBitmap();
////        Log.e("123","fgWidth "+fgWidth);
////        Log.e("123","bgHeight "+bgHeight);
////        Log.e("123","fgHeight "+fgHeight);
//        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight+fgHeight, Bitmap.Config.ARGB_8888);
//        Canvas cv = new Canvas(newbmp);
//        //draw bg into
//        cv.drawBitmap(newbmp1, 0, 0, null);//在 0，0坐标开始画入bg
//        //draw fg into
//        cv.drawBitmap(newbmp2, fgWidth, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
//        cv.drawBitmap(newbmp3, 0, fgHeight, null);
//        //save all clip
//        cv.save(Canvas.ALL_SAVE_FLAG);//保存
//        //store
//        cv.restore();//存储
//        return newbmp;
//    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getUids();
        setMsgNotification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(MSG);
//                    Log.e("123","handler 循环");
                }
            }
        }).start();
    }

    // 收到用户按返回键发出的广播，就显示通知栏
    private BroadcastReceiver backKeyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Toast.makeText(context, "QQ进入后台运行", 0).show();
            setMsgNotification();
        }
    };
    /**
     * 创建通知
     */
    private void setMsgNotification() {

        Intent intent = new Intent(this, MainActivity.class);
       builder   = new NotificationCompat.Builder(
                this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        builder.setSmallIcon(R.drawable.not_num_3);
        BitmapDrawable b =(BitmapDrawable) getResources().getDrawable(R.drawable.not_num_3);

//        builder.setLargeIcon(b.getBitmap());
        builder.setContentTitle("网络流量监控");
        builder.setContentText("下载速度 上传速度 ");
//        builder.setContentInfo("info");//设置通知的消息info
        builder.setSubText("是第三行吗");
        builder.setOngoing(true);
        RemoteViews view_custom = new RemoteViews(LiuliangService.this.getPackageName(),
                R.layout.pushmessage_view_custom);
        view_custom.setImageViewResource(R.id.custom_icon,R.mipmap.logo);
//        view_custom.setImageViewBitmap(R.id.custom_icon, toConformBitmap());
//                    view_custom.setImageViewBitmap(R.id.small_icon, toConformBitmap());
        view_custom.setTextViewText(R.id.tv_custom_title, "下载: "+"KB/s  "+"上传 :"+"KB/s");
        view_custom.setTextViewText(R.id.tv_custom_content, "网络流量监控");
        builder.setContent(view_custom);
        builder.setContentIntent(contentIntent);
        builder.setWhen(0)
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
        ;

//        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        mNotification =builder.build();


         mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mNotification);
    }
    public List getUids() {
        List<Integer> uidList = new ArrayList<Integer>();
      PackageManager  pm = getPackageManager();
        List<PackageInfo> packinfos = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                        | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        // System.out.println(info.packageName+"访问网络");
                        int uid = info.applicationInfo.uid;
                       String pakeageName =  info.applicationInfo.packageName;
                        Log.e("123", "pakeageName = " + pakeageName +"uid = " + uid);
                        Log.e("123", "uid = " + uid);
                        // String name = pm.getNameForUid(uid);com.tencent.mobileqq
                        // // textName.setText(name);
                        // Log.i("test", "name = "+name);
                        uidList.add(uid);
                    }
                }
            }
        }
        return uidList;
    }
}
