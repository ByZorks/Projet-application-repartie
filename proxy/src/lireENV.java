import java.io.*;

public class lireENV {

    String chemin;

    public lireENV(String s) {
        chemin = s;
    }

    public String getUser(){
            try {
                BufferedReader br = new BufferedReader(new FileReader(chemin));
                String line = br.readLine();
                return line;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public String getPassword(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(chemin));
            String line = br.readLine();
            line = br.readLine();
            return line;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getURL(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(chemin));
            String line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            return line;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
