import java.net.*;

import java.io.*;
import java.util.Scanner;

public class Server {



    public static void main(String[] args) throws IOException{
        int number,res;

        ServerSocket socS = new ServerSocket(1234);

        Socket socA = socS.accept();

        System.out.println("client connecte");

        Scanner sc = new Scanner(socA.getInputStream());


        number = sc.nextInt();



        PrintStream p=new PrintStream(socA.getOutputStream());

        if (number==1){
            //voter
            res= 0;
            System.out.println("fonction a implementer :"+ number);
            p.println(res);

        }







    }



}