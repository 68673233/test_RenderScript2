package com.test.test_renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.renderscript.*;

import android.renderscript.Element;
import android.support.v8.renderscript.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    Button btn;

    RenderScript mRs;
    Allocation mInAllocation,mOutAllocation,mInAllocation2;
    ScriptC_testField scriptGrayed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn=findViewById(R.id.textView);
        ImageView image1=findViewById(R.id.imageView);
        ImageView image2=findViewById(R.id.imageView2);

//        javaCallRs();

        final Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.mipmap.splash3);
        image1.setImageBitmap(bmp);
        final Bitmap bmp2=Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), Bitmap.Config.ARGB_8888);
        final Bitmap bmp3=BitmapFactory.decodeResource(getResources(), R.mipmap.splash2);
//        bmp2= SketchUtil.sketchBitmap(bmp,this);
        image2.setImageBitmap(bmp2);

        mRs = RenderScript.create(this);
        mInAllocation = Allocation.createFromBitmap(mRs, bmp,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        mOutAllocation = Allocation.createFromBitmap(mRs, bmp2,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        mInAllocation2 = Allocation.createFromBitmap(mRs, bmp3,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        scriptGrayed = new ScriptC_testField(mRs);

        //region test1
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createRS(bmp,bmp2);
//            }
//        });
    //endregion

        //region 测试2
        // 测试2
//        btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                scriptGrayed.invoke_add();
//            }
//        });
//
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
////                    createRS(bmp,bmp2);
//                    avgRS(bmp,bmp2);
//                    try {
//                        Thread.sleep(40);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
    //endregion

       //region 测试3
        RSView rsView=new RSView(this,image2);
        rsView.initSinAddCos();
        rsView.add();
        //endregion
    }

    private void javaCallRs() {

//        long startTime = System.currentTimeMillis();
//        // Instantiates the RenderScript context.
//        RenderScript mRS = RenderScript.create(this);
//        // Create an input array, containing some numbers.
//        int inputArray[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
//        // Instantiates the input Allocation, that will contain our sample
//        // numbers.
//        Allocation inputAllocation = Allocation.createSized(mRS, Element.I32(mRS), inputArray.length);
//        // Copies the input array into the input Allocation.
//        inputAllocation.copyFrom(inputArray);
//        // Instantiates the output Allocation, that will contain the result of the process.
//        Allocation outputAllocation = Allocation.createSized(mRS, Element.I32(mRS), inputArray.length);
//        // Instantiates the sum script.
//        ScriptC_sum myScript = new ScriptC_sum(mRS);
//        // Run the sum process, taking the elements belonging to the inputAllocation and placing
//        // the process results inside the outputAllocation.
//        myScript.forEach_sum2(inputAllocation, outputAllocation);
//        // Copies the result of the process from the outputAllocation to a simple int array.
//        int outputArray[] = new int[inputArray.length];
//        outputAllocation.copyTo(outputArray);
//        Log.d("tag", "time = "+ (System.currentTimeMillis() - startTime));
//        String debugString = "Output: ";
//        for (int i = 0; i < outputArray.length; i++)
//            debugString += String.valueOf(outputArray[i]) + (i < outputArray.length - 1 ? ", " : "");
//
//        Toast.makeText(this,debugString,Toast.LENGTH_LONG).show();
    }

    public static class SketchUtil {
        public static Bitmap sketchBitmap(Bitmap bitmap, Context context){
            RenderScript renderScript = RenderScript.create(context);
            ScriptC_sketch sketchScript = new ScriptC_sketch(renderScript);



            Allocation in = Allocation.createFromBitmap(renderScript,bitmap);
            Allocation out = Allocation.createTyped(renderScript,in.getType());

            // call kernel
            sketchScript.forEach_invert(in,out);

            out.copyTo(bitmap);

            renderScript.destroy();
            sketchScript.destroy();
            in.destroy();
            out.destroy();

            return bitmap;
        }
    }

    private void createRS(Bitmap mBitmapIn ,Bitmap bmpOut) {
        String TAG="tag";
//        long startTime = System.currentTimeMillis();
        scriptGrayed.forEach_root(mInAllocation, mOutAllocation);
//        long endTime = System.currentTimeMillis();
//        Log.i(TAG,"运行时间："+( endTime-startTime));
        //设置属性
//        scriptGrayed.set_gIn(mInAllocation);
//        scriptGrayed.set_gOut(mOutAllocation);

        mOutAllocation.copyTo(bmpOut);
    }

    private void avgRS(Bitmap mBitmapIn ,Bitmap bmpOut){

        scriptGrayed.set_gIn(mInAllocation2);
        scriptGrayed.set_width(800);
        scriptGrayed.set_height(600);
         scriptGrayed.forEach_averageBitmap(mInAllocation,mOutAllocation);
         mOutAllocation.copyTo(bmpOut);
    }

}
