package hello;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Pedersen {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        BigInteger p = new BigInteger("84111441111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111121111111111111111");
        BigInteger q = new BigInteger("2147483647");
        Random random = new Random();



        BigInteger g = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1));
        BigInteger s = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1));



        BigInteger h = g.modPow(s, q);
        BigInteger x = new BigInteger("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        BigInteger r=new BigInteger("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        long startTime1=System.currentTimeMillis();
        BigInteger c = g.modPow(x, q).multiply(h.modPow(r, q)).mod(q);//计算c，即加密数据

        long endTime1=System.currentTimeMillis();
        System.out.println("承诺运行时间： "+(endTime1 - startTime1)+"ms");


        System.out.println("c" + c);

    }



}






