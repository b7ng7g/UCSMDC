package hello;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

class PQGY {
    BigInteger p;
    BigInteger q;
    BigInteger g;
    BigInteger y;

    public PQGY() {
        // generate and print pqg
        SecureRandom random = new SecureRandom();

        while (true) {
            q = BigInteger.probablePrime(1024, random);
            p = q.multiply(BigInteger.valueOf(3));
            p = p.add(BigInteger.valueOf(2));
            if (p.isProbablePrime(1)) {
                break;
            }
        }

        //g=h^2modp
        BigInteger h = new BigInteger(1024, random);

        while(h.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || h.compareTo(BigInteger.valueOf(2)) == -1){
            h = new BigInteger(100, random);
        }

        g = h.modPow(BigInteger.valueOf(2), p);

        BigInteger x = new BigInteger(100, random);

        while(x.compareTo(q.subtract(BigInteger.valueOf(2))) == 1 || x.compareTo(BigInteger.valueOf(2)) == -1){
            x = new BigInteger(100, random);
        }

        // generate y: y = g^x where x is any random number between 2 and q-2.
        y = g.modPow(x, p);

        // display
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("g: " + g);
        System.out.println("y: " + y);
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getY() {
        return y;
    }
}

class Solution2 {

    public static void commit(BigInteger m, PQGY pqgy) throws IOException {

        SecureRandom random = new SecureRandom();
        //compute a commitment c = g^m*y^r
        //generate random number r between 2 and q-2
        BigInteger r = new BigInteger(100, random);

        while(r.compareTo(pqgy.getQ().subtract(BigInteger.valueOf(2))) == 1 || r.compareTo(BigInteger.valueOf(2)) == -1){
            r = new BigInteger(100, random);
        }
        //long startTime1=System.currentTimeMillis();
        BigInteger c =pqgy.getG().modPow(m, pqgy.getP()).multiply(pqgy.getY().modPow(r, pqgy.getP())).mod(pqgy.getP());
        //long endTime1=System.currentTimeMillis();
        //System.out.println("承诺运行时间： "+(endTime1 - startTime1)+"ms");
        //output to file
        FileWriter file = new FileWriter("commit.txt");
        file.write(c+"\n"+r);
        file.close();

        System.out.println("Written to commit.txt");
    }

    public static void reveal(BigInteger m, BigInteger c, BigInteger r, PQGY pqgy) {
        if(c.equals(pqgy.getG().modPow(m, pqgy.getP()).multiply(pqgy.getY().modPow(r, pqgy.getP())).mod(pqgy.getP()))){
            System.out.println("True");
        } else {
            System.out.println("False");
        }
    }

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pedersen’s commitment scheme");
        System.out.println("========================================");
        System.out.println("Setup");
        //generate pqgy
        PQGY pqgy = new PQGY();
        System.out.println("========================================");
        //menu
        while(true){
            String selection = "";

            System.out.println("Run algorithm: ");
            System.out.println("1. Commit");
            System.out.println("2. Reveal");
            System.out.println("3. Exit");
            System.out.print("Selection: ");

            selection = scanner.nextLine();

            //get user input
            if(selection.equals("1")){
                System.out.println("========================================");
                System.out.println("Commit");
                System.out.print("Enter value m: ");

                String mInput = scanner.nextLine();

                BigInteger m;

                try{
                    m = new BigInteger(mInput);
                } catch(NumberFormatException e){
                    System.out.println("Incorrect M input.");
                    break;
                }




                //承诺


                long startTime1=System.nanoTime();
                System.out.println("开始运行时间： "+startTime1+"ns");
                commit(m, pqgy);
                long endTime1=System.nanoTime();
                System.out.println("结束运行时间： "+endTime1+"ns");

                System.out.println("承诺运行时间： "+(endTime1 - startTime1)+"ns");



            } else if(selection.equals("2")){
                System.out.println("========================================");
                System.out.println("Reveal");
                System.out.println("========================================");

                System.out.print("Enter value m: ");
                String mInput = scanner.nextLine();

                BigInteger m;

                try{
                    m = new BigInteger(mInput);
                } catch(NumberFormatException e){
                    System.out.println("Incorrect M input.");
                    break;
                }

                System.out.print("Enter commit file: ");
                String filename = scanner.nextLine();

                //open and read m and r from a file
                File file = new File(filename);
                BufferedReader br;

                try{
                    br = new BufferedReader(new FileReader(file));
                } catch(FileNotFoundException e){
                    System.out.println("File not found");
                    break;
                }

                String cInput = br.readLine();
                String rInput = br.readLine();

                BigInteger c;
                BigInteger r;

                try{
                    c = new BigInteger(cInput);
                    r = new BigInteger(rInput);
                } catch(NumberFormatException | NullPointerException e){
                    System.out.println("File tampered");
                    break;
                }

                br.close();


                long startTime2=System.nanoTime();
                reveal(m, c, r, pqgy);
                long endTime2=System.nanoTime();
                System.out.println("解承诺运行时间： "+(endTime2 - startTime2)+"ns");



            } else if(selection.equals("3")){
                System.out.println("========================================");
                System.out.println("Goodbye");
                System.out.println("========================================");
                break;
            } else {
                System.out.println("Incorrect Input");
            }
        }

        scanner.close();
    }
}

