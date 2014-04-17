/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mottley;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan
 */
public class BaseStatScraper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // Objects
        FileWriter writer = null;
        String pageSource = "";

        try {
            // Opening cvs file to write data to
            File file = new File("basestats.csv");
            if (file.exists()) {
                file.delete();
            }

            writer = new FileWriter(file);
            writer.append("Pokemon Num, Pokemon Name, HP, Atk, Def, Sp Atk, Sp Def, Speed\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(BaseStatScraper.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        for (int pNum = 1; pNum <= 719; pNum++) {
            // Selecting page to read
            if (pNum < 10)
            {
                pageSource = downloadPage("http://www.serebii.net/pokedex-xy/00" + pNum + ".shtml");
            }
            else if (pNum < 100)
            {
                pageSource = downloadPage("http://www.serebii.net/pokedex-xy/0" + pNum + ".shtml");
            }
            else
            {
                pageSource = downloadPage("http://www.serebii.net/pokedex-xy/" + pNum + ".shtml");
            }

            Scanner scanner = new Scanner(pageSource);
            String result = new String();
            try {
                while (scanner.hasNextLine()) {
                    result = scanner.nextLine();
                    if (result.contains("<title>")) {
                        result = result.substring(7);
                        String splitResult[] = result.split("-");
                        writer.append(splitResult[1].trim().replace("#", "") + ", ");
                        writer.append(splitResult[0].trim() + ", ");
                    } else if (result.contains("<tr><td colspan=\"2\" width=\"14%\" class=\"fooinfo\">Base Stats")) {
                        for (int i = 0; i < 6; i++) {
                            result = scanner.nextLine();
                            result = result.replace("<td align=\"center\" class=\"fooinfo\">", "");
                            result = result.replace("</td>", "");
                            if (i == 5) {
                                result = result.replace("</tr>", "");
                            }
                            writer.append(result);
                            if (i < 5) {
                                writer.append(", ");
                            } else if (i == 5) {
                                writer.append("\n");
                            }
                        }
                        writer.flush();
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(BaseStatScraper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                scanner.close();
            }
        }
        writer.close();
    }

    public static String downloadPage(String url) {
        try {
            URL pageURL = new URL(url);
            StringBuilder text = new StringBuilder();
            Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");
            try {
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine() + "\n");
                }
            } finally {
                scanner.close();
            }
            return text.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(BaseStatScraper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(BaseStatScraper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
