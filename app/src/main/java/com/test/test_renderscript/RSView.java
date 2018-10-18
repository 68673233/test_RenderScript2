package com.test.test_renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;

/**
 * Created by liwb on 2018/2/23.
 */

public class RSView {

    private Context context;
    private ImageView image;
    private Bitmap bmp;
    private Canvas canvas;
    private Paint p;

    RenderScript mRs;
    Allocation mInAllocation,mOutAllocation,mInAllocation2;
    float[] out=new float[360];
    ScriptC_computewave computewave;
    public RSView(Context context, ImageView image){
        this.context=context;
        this.image=image;

        mRs=RenderScript.create(context);

    }

    public void initSinAddCos(){
        float[] in=new float[360];
        float[] in2=new float[360];
        for (int i=0;i<in.length;i++){
            in[i]=(float) Math.sin(Math.PI*i/180);
            in2[i]=(float) Math.cos(Math.PI*i/180);
        }


        mInAllocation=Allocation.createSized(mRs, Element.F32(mRs),in.length);
        mInAllocation.copyFrom(in);
        mInAllocation2=Allocation.createSized(mRs,Element.F32(mRs),in.length);
        mInAllocation2.copyFrom(in2);
        mOutAllocation=Allocation.createSized(mRs,Element.F32(mRs),in.length);
        computewave=new ScriptC_computewave(mRs);
        bmp=Bitmap.createBitmap(360,200, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bmp);
        p=new Paint();
        p.setColor(Color.RED);
        image.setImageBitmap(bmp);
    }

    public void add(){
        computewave.set_gIn(mInAllocation2);
        Thread thread=new Thread(new Runnable() {
            private int c=255;
            @Override
            public void run() {
                while (true) {
                    computewave.forEach_add(mInAllocation, mOutAllocation);
//                    System.out.println("copy");
                    mOutAllocation.copyTo(out);

                    p.setColor(Color.WHITE);
                    canvas.drawRect(0,0,bmp.getWidth(),bmp.getHeight(),p);


                    p.setColor(Color.argb(c,255,255-c,255-c));
                    for (int i = 0; i < out.length; i++)
                        canvas.drawPoint(i, out[i], p);
                    c++;
                    if (c>255)c=128;
image.post(new Runnable() {
    @Override
    public void run() {
        image.setImageBitmap(bmp);
    }
});
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();
    }


}
