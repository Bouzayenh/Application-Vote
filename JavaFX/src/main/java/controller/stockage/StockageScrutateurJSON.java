package controller.stockage;

import dataobject.ClePublique;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class StockageScrutateurJSON implements IStockageScrutateur{

    private JSONObject clesJSON;

    private String pathName;


    public StockageScrutateurJSON(String pathName) throws IOException {
        this.pathName = pathName;

        File fichier = new File(pathName);

        if (fichier.createNewFile()){
            FileWriter fileWriter = new FileWriter(fichier);
            fileWriter.write(new JSONObject().toString());
            fileWriter.close();
        }
    }

    /**
     * Actualise les informations contenues dans 'clesJSON' depuis le fichier '/pathName'.
     * @throws IOException
     */
    private void read() {
        try {
            clesJSON = new JSONObject(new String(Files.readAllBytes(Path.of(pathName))));
        }catch (IOException ignored){
        }
    }


    /**
     * Sauvegarde les informations contenues dans 'clesJSON' dans le fichier '/pathName'.
     * @throws IOException
     */
    private void write(){
        try {
            FileWriter fileWriter = new FileWriter(pathName);
            fileWriter.write(clesJSON.toString());
            fileWriter.close();
        }catch (IOException ignored){
        }
    }


    @Override
    public int insererVote(BigInteger p, BigInteger g, BigInteger h, BigInteger x) {

        read();

        int i = 0;
        while (!clesJSON.isNull(String.valueOf(i))){
            i++;
        }

        JSONObject cles = new JSONObject();
        cles.put("p", p.toString());
        cles.put("g", g.toString());
        cles.put("h", h.toString());
        cles.put("x", x.toString());

        clesJSON.put(String.valueOf(i), cles);

        write();

        return i;
    }

    @Override
    public ClePublique getClePublique(int idVote) {
        read();

        try {

            JSONObject clesVote = new JSONObject(clesJSON.getJSONObject(String.valueOf(idVote)).toString());

            return new ClePublique(
                    clesVote.getBigInteger("p"),
                    clesVote.getBigInteger("g"),
                    clesVote.getBigInteger("h")
            );
        }catch (JSONException e){
            return null;
        }
    }

    @Override
    public BigInteger getClePrivee(int idVote) {
        read();

        try {

            JSONObject clesVote = new JSONObject(clesJSON.getJSONObject(String.valueOf(idVote)).toString());

            return clesVote.getBigInteger("x");
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
