package com.example.korea_hachathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DrawView extends View implements View.OnTouchListener{

    private ArrayList<PaintPoint> points = new ArrayList<>();
    private int color = Color.BLACK ;
    private int lineWith = 100;
    public static int flag =0;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(flag == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    Paint p = new Paint();
                    p.setAlpha(255);
                    p.setStrokeJoin(Paint.Join.ROUND);
                    p.setStrokeCap(Paint.Cap.ROUND);
                    p.setAntiAlias(true);
                    p.setStyle(Paint.Style.STROKE);
                    p.setColor(color);
                    p.setStrokeWidth(lineWith);
                    points.add(new PaintPoint(event.getX(), event.getY(), true, p));
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                    points.add(new PaintPoint(event.getX(), event.getY(), false, null));
            }
            return true;
        }
        return false;
    }

    public void reset(){
        points.clear();
        invalidate();
    }


  public void send(Context context){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();

        Bitmap screenshot = this.getDrawingCache();

        Toast.makeText(getContext(), "save버튼 호출", Toast.LENGTH_SHORT).show();

        // @SuppressLint("SimpleDateFormat")SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        //Date currentTime = new Date();
        //String dateString = formatter.format(currentTime);
        String filename = "ctos_image.png";
        try{
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            if(file.createNewFile())
                Log.d("save","파일 생성 성공");
            OutputStream outStream = new FileOutputStream(file);
            screenshot.compress(Bitmap.CompressFormat.PNG,100,outStream);
           // outStream.flush();
            outStream.close();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                intent.setData(contentUri);
                context.sendBroadcast(intent);
            }else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.setDrawingCacheEnabled(false);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 1;i<points.size();i++)
        {
            if(!points.get(i).isDraw())
                continue;
            canvas.drawLine(points.get(i-1).getX(), points.get(i-1).getY(),
                    points.get(i).getX(), points.get(i).getY(),points.get(i).getPaint());
        }
    }

    public ArrayList<PaintPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PaintPoint> points) {
        this.points = points;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLineWith() {
        return lineWith;
    }

    public void setLineWith(int lineWith) {
        this.lineWith = lineWith;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        DrawView.flag = flag;
    }
}
