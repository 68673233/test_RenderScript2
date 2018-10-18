//
//进行波形的计算试例
// 两个缓冲区的数据进行一一相加后，返回后，进行显示到界面上。
#include "pragma.rsh"

rs_allocation gIn;// 输入的 allocation 对象

float __attribute__((kernel)) add(float in, uint32_t x){

   float index=  *(float *) rsGetElementAt(gIn,x);
   //rsDebug("in:",in,x,index);
   return (in+index)*10+100;
   //return (in+index)*100+100;
}