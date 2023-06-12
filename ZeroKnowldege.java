package hello;

import java.io.*;
import java.util.*;
//零知识证明
class ZeroKnowldege{

    public long power(long x, long y, long p)
    {
        // Initialize result
        long res = 1;

        // Update x if it is more
        // than or equal to p
        x = x % p;

        while (y > 0)
        {
            // If y is odd, multiply x
            // with result
            if((y & 1)==1)
                res = (res * x) % p;

            // y must be even now
            // y = y / 2
            y = y >> 1;
            x = (x * x) % p;
        }
        return res;
    }

    public static int g =23 , p = 1237;

    public static void main(String[] args) {
        Peggy peggy = new Peggy();  //证明者
        Victor victor =new Victor();   // 验证者

        //calculate y:
        long y = peggy.calculateY(g,p);
        victor.setY(y);

        //Iterations:
        Scanner sc = new Scanner(System.in);
        int iterations =sc.nextInt();
        peggy.peggyKnows(g,p,iterations,victor);

    }
}

//证明者
class Peggy{

    //predecided x.
    int x = 74;
    long y;

    public long calculateY(int g ,int p){
        ZeroKnowldege zk = new ZeroKnowldege();
        y =	zk.power(g,x,p);
        return y;
    }
    public void peggyKnows(int g, int p, int i, Victor victor){
        ZeroKnowldege zk =new ZeroKnowldege();
        System.out.println();
        for(int j=1;j<=i;j++){
            System.out.println("Round: "+j);

            //choose random r
            int r = (int)(Math.random())*1000;
            System.out.println("Peggy chooses random r :"+r);
            long C = zk.power(g,r,p);
            System.out.println("Peggy sends Victor computed C:"+C);
            victor.sendC(C);

            //Victor chooses request
            int request = victor.request();
            System.out.println("Victor Requesting for :"+request);

            if(request==0){
                if((victor.checkresponse(g,r,p,r,request)==false))
                    break;
            }
            else{
                long val = zk.power(x+r,1,p-1);
                if(victor.checkresponse(g,r,p,val,request)==false)
                    break;
            }
        }
    }

}

//验证者
class Victor{

    long y , C ;

    public void setY(long y){
        System.out.println("Victor recieves y:"+y);
        this.y = y;
    }

    public void sendC(long C){
        System.out.println("Victor recieves C"+C);
        this.C = C ;
    }
    public int request(){
        return ((int)Math.round(Math.random()));
    }
    public boolean checkresponse(int g ,int r,int p, long val ,int request){
        ZeroKnowldege zk =new ZeroKnowldege();
        if(request==0){
            long newC = zk.power(g,val,p);
            if(newC == C)
                System.out.println("Alright");
            else{
                System.out.println("Peggy lies");
                return false;
            }
            return true;
        }  
        else{
            long newC = zk.power(g,val,p);
            if(newC == C*y){
                System.out.println("Alright");
//                long endTime=System.currentTimeMillis();
//                System.out.print("endTime"+endTime);
                return true;
            }
            else
                System.out.println("Peggy lies");
            return false;
        }
    }

}