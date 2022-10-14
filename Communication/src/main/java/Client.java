import java.io.*;
import java.net.*;

import java.util.*;

public class Client {



    public static void main(String[] args) throws IOException,NoSuchElementException{

        Socket socc=null;
        InputStreamReader input=null;
        OutputStreamWriter output=null;
        BufferedReader buffR=null;
        BufferedWriter buffW=null;


        try {

            socc = new Socket("localhost", 1234);

            System.out.println("Connecté");

            input = new InputStreamReader(socc.getInputStream());

            output= new OutputStreamWriter(socc.getOutputStream());

            buffR= new BufferedReader(input);
            buffW= new BufferedWriter(output);

            Scanner scanner = new Scanner(System.in);

            while(true){
                String msg = scanner.nextLine();
                buffW.write(msg);
                buffW.newLine();
                buffW.flush();

                System.out.println("Server: " + buffR.readLine());

                if (msg.equalsIgnoreCase("Deconnecte")){
                    break;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (socc != null)
                    socc.close();
                if (input!=null)
                    input.close();
                if (output!=null)
                    output.close();
                if (buffR != null)
                    buffR.close();
                if (buffW!=null)
                    buffW.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

//        System.out.println("choisissez une action en ecriavnt son nombre"+'\n'
//                            +"1-Voter"+"\n"+"2-conslutez vote"+"\n"+"3-etc"+"\n"
//                            +"4-deconnecté"+"\n");
//
//        num= sc.nextInt();
//
//        PrintStream ps = new PrintStream(socc.getOutputStream());
//
//        ps.println(num);
//
//        res=scNum.nextInt();
//
//        System.out.println(res);



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

