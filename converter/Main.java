import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    File lyrics = new File("input.txt");
    try {
    FileWriter writer = new FileWriter("output.txt");
      try (Scanner myReader = new Scanner(lyrics)) {
        // myReader.useLocale(new Locale("et"));
        int lineCount = 1;
        boolean prevEmpty = false;
        while (myReader.hasNextLine()) {
          String line = myReader.nextLine();
          if (lineCount == 1) {
            String id_line = line.toLowerCase().replace(", ","-").replace(" ","-").replace("ä","a").replace("ö","o")
                .replace("õ", "o").replace("ü", "u"); //need öäõü asendused ei tööta btw, ma ei tea miks
                writer.write("<li><a href=\"#" + id_line + "\">" + line + "</a></li>\n"); // ÄRA JÄTA SEDA RIDA KOPEERIDES SISSE!! See on sisukorda lisamise jaoks
                writer.write("<h4 id=\"" + id_line +"\">" + line + "</h4>\n");
            lineCount += 1;
          } else if (lineCount == 2) {
            if (line.trim().isEmpty()) { 
              writer.write("<p><em>&emsp;&emsp;&emsp;</em></p> <!--TODO: koht-->\n"); //kui kohta pole, tee otsitav märgistus
            } else {
              if (!line.contains("(")) { // if forgor (), try to add them
                line = "(" + line;
              }
              if (!line.contains(")")) {
                line = line + ")";
              }
              writer.write("<p><em>&emsp;&emsp;&emsp;" + line + "</em></p>\n");
            }
            lineCount += 1;
          } else {
            if (lineCount == 3) { //muidu jääb esimene <p> puudu
              line = "<p>" + line;
              lineCount += 1;
            }

            line.replace("     ", "&emsp;"); //asenda TAB tühikud html-i sõbraliku TAB-iga

            if (line.trim().isEmpty()) { //close paragraph tags salmi lõpus
              writer.write("</p>\n");
              prevEmpty = true;
            } else {
              if (prevEmpty) { //paragraph tags end on empty lines, so need to be reopened on the next line
                prevEmpty = false;
                line = "<p>" + line;
              }
              writer.write(line + "<br/>\n"); //line break rea lõppu
            }
          }
          if (!myReader.hasNextLine()) { //closes the last paragraph tag
            writer.write("</p>\n");
          }
        }
      } catch (FileNotFoundException e) {
        System.out.println("No file found.");
        e.printStackTrace();
      } 
    writer.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      System.out.println("No element.");
      e.printStackTrace();
    }
  }
}
