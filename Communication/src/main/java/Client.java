import java.net.*;

import java.io.*;
import java.util.*;

public class Client {



    public static void main(String[] args) throws  IOException,NoSuchElementException{

        int num,res;

        Scanner sc = new Scanner(System.in);

        Socket socc = new Socket("localhost", 1234);

        System.out.println("Connecté");

        Scanner scNum= new Scanner(socc.getInputStream());

        System.out.println("choisissez une action en ecriavnt son nombre"+'\n'
                            +"1-Voter"+"\n"+"2-conslutez vote"+"\n"+"3-etc"+"\n"
                            +"4-deconnecté"+"\n");

        num= sc.nextInt();

        PrintStream ps = new PrintStream(socc.getOutputStream());

        ps.println(num);

        res=scNum.nextInt();

        System.out.println(res);



//        InputStreamReader in = new InputStreamReader(socc.getInputStream());
//
//        BufferedReader bf = new BufferedReader(in);
//
//
//
//        String str = bf.readLine();
//
//        System.out.println("server : "+ str);

    }



}

