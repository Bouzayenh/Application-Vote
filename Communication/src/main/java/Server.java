import java.net.*;

import java.io.*;
import java.util.Scanner;

public class Server {



    public static void main(String[] args) throws IOException{
        Socket socCl=null;
        Socket socBDD=null;
        InputStreamReader input=null;
        OutputStreamWriter output=null;
        BufferedReader buffR=null;
        BufferedWriter buffW=null;
        ServerSocket serverSocket=null;

        serverSocket=new ServerSocket(1234);

        while (true) {

            try {

                socCl = serverSocket.accept();

                input = new InputStreamReader(socCl.getInputStream());
                output = new OutputStreamWriter(socCl.getOutputStream());

                buffR = new BufferedReader(input);
                buffW = new BufferedWriter(output);

                String msgRecu="";
                while (true) {
                    msgRecu = buffR.readLine();

                    System.out.println("msg Client :" + msgRecu);

                    buffW.write("msg Recu");
                    buffW.newLine();
                    buffW.flush();


                    socBDD =serverSocket.accept();

                    System.out.println("BDD connecté");
                    PrintStream p=new PrintStream(socBDD.getOutputStream());
                    p.println(msgRecu);

                    if (msgRecu.equalsIgnoreCase("DECONNECTE")) {
                        break;
                    }
                }
                socCl.close();
                input.close();
                output.close();
                buffR.close();
                buffW.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        int number,res;
//
//        ServerSocket socCl = new ServerSocket(1234);
//
//        Socket socA = socCl.accept();
//
//        System.out.println("client connecte");
//
//        Scanner sc = new Scanner(socA.getInputStream());
//
//
//        number = sc.nextInt();
//
//
//
//        PrintStream p=new PrintStream(socA.getOutputStream());
//
//        if (number==1){
//            //voter
//            res= 0;
//            System.out.println("fonction a implementer :"+ number);
//            p.println(res);

    }

}



