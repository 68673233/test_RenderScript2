#include "pragma.rsh"

rs_allocation gIn;// 输入图像的 allocation 对象
rs_allocation gOut;// 输出图像的 allocation 对象
rs_script gScript;// RenderScript 对象
uint32_t width;
uint32_t height;

// 一种常用的加权平均灰度化的权重值
const static float3 gMonoMult = {0.299f, 0.587f, 0.114f};

void init(){
    rsDebug("===init ======",0);
}

// rs 的核心函数，每一个像素都会执行这个函数
void root(const uchar4 *v_in, uchar4 *v_out) {
   // rsDebug("===root wiz 2 params",0);// debug info
    float4 f4 = rsUnpackColor8888(*(v_in));// 获得 输入allocation 中当前点的 RGBA 值
    float3 mono = dot(f4.rgb, gMonoMult);// 灰度运算
    f4.r=1-f4.r;
    f4.g=1-f4.g;
    f4.b=1-f4.b;
    *v_out = rsPackColorTo8888(f4.r,f4.g,f4.b);
    //*v_out = rsPackColorTo8888(mono);// 将灰度值写入 输入allocation 的当前对应位置点
}

uchar4 __attribute__((kernel)) averageBitmap(uchar4 in, uint32_t x, uint32_t y){
    uchar4 out=in;

    uchar4 left = in;
    uchar4 top = in;
    uchar4 right = in;
    uchar4 bottom = in;

        if(x - 1 > -1){ //access other element
            left = *(uchar4 *)(rsGetElementAt(gIn, x - 1, y));
        }
        if(y - 1 > -1){
            top = *(uchar4 *)rsGetElementAt(gIn, x , y - 1);
        }
        if(x + 1 < width){
            right = *(uchar4 *)rsGetElementAt(gIn, x + 1, y);
        }
        if(y + 1 < height){
            bottom = *(uchar4 *)rsGetElementAt(gIn, x, y + 1);
        }

        out.r = (left.r + top.r + right.r + bottom.r) / 4;
        out.g = (left.g + top.g + right.g + bottom.g) / 4;
        out.b = (left.b + top.b + right.b + bottom.b) / 4;

    return out;
}


/* 由JAVA中调用 */
void add(){
   rsDebug("123",0);
}

