import java.net.*;

import java.io.*;
import java.util.Scanner;

public class Server {



    public static void main(String[] args) throws IOException{
        Socket socCl=null;
        Socket socBDD=null;
        InputStreamReader input=null;
        OutputStreamWriter output=null;
        InputStreamReader inputBDD=null;
        OutputStreamWriter outputBDD=null;
        BufferedReader buffR=null;
        BufferedWriter buffW=null;
        BufferedReader buffBDDR=null;
        BufferedWriter buffBDDW=null;
        ServerSocket serverSocket=null;

        serverSocket=new ServerSocket(1234);

        while (true) {

            try {

                socCl = serverSocket.accept();

                input = new InputStreamReader(socCl.getInputStream());
                output = new OutputStreamWriter(socCl.getOutputStream());

                buffR = new BufferedReader(input);
                buffW = new BufferedWriter(output);

                socBDD =serverSocket.accept();

                inputBDD = new InputStreamReader(socBDD.getInputStream());
                outputBDD = new OutputStreamWriter(socBDD.getOutputStream());

                buffBDDR = new BufferedReader(inputBDD);
                buffBDDW = new BufferedWriter(outputBDD);

                String msgRecu="";
                String msgBDD="";
                while (true) {
                    msgRecu = buffR.readLine();

                    System.out.println("msg Client :" + msgRecu);

                    buffW.write("msg Recu");
                    buffW.newLine();
                    buffW.flush();

                    
                    System.out.println("BDD connecté");
                    /*PrintStream p=new PrintStream(socBDD.getOutputStream());
                    p.println(msgRecu);*/
                    buffBDDW.write(msgRecu);
                    buffBDDW.newLine();
                    buffBDDW.flush();

                    //msgBDD= buffBDDR.readLine();
                    System.out.print(msgBDD);

                    buffW.write(msgBDD);
                    buffW.newLine();
                    buffW.flush();

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



