/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mottley;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan
 */
public class BaseStatScraper
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        // Objects
        FileWriter writer = null;
        String pageSource = "";

        try
        {
            // Opening cvs file to write data to
            File file = new File("basestats.csv");
            
            // Deletes file if it already exists
            if (file.exists())
            {
                file.delete();
            }

            writer = new FileWriter(file);
            
            // Setting up the start of the file
            writer.append("Pokemon Num, Pokemon Name, HP, Atk, Def, Sp Atk, Sp Def, Speed\n");
            writer.flush();
            
        } 
        catch (IOException ex)
        {
            // Writer failed to open, exit with error code -1
            Logger.getLogger(BaseStatScraper.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        for (int pNum = 1; pNum <= 719; pNum++)
        {
            // Selecting page to read
            if (pNum < 10)
            {
                pageSource = com.mottley.URLFunctions.downloadPage("http://www.serebii.net/pokedex-xy/00" + pNum + ".shtml");
            }
            else if (pNum < 100)
            {
                pageSource = com.mottley.URLFunctions.downloadPage("http://www.serebii.net/pokedex-xy/0" + pNum + ".shtml");
            }
            else
            {
                pageSource = com.mottley.URLFunctions.downloadPage("http://www.serebii.net/pokedex-xy/" + pNum + ".shtml");
            }

            Scanner scanner = new Scanner(pageSource);
            String result = new String();
            try 
            {
                while (scanner.hasNextLine()) 
                {
                    result = scanner.nextLine();
                    
                    // Scanning line, looking for TITLE block
                    if (result.contains("<title>")) 
                    {
                        // Grabbing number and name
                        result = result.substring(7);
                        String splitResult[] = result.split("-");
                        
                        writer.append(splitResult[1].trim().replace("#", "") + ", ");
                        writer.append(splitResult[0].trim() + ", ");
                    } 
                    else if (result.contains("<tr><td colspan=\"2\" width=\"14%\" class=\"fooinfo\">Base Stats")) 
                    {
                        for (int i = 0; i < 6; i++) 
                        {
                            // Parsing base stats
                            result = scanner.nextLine();
                            result = result.replace("<td align=\"center\" class=\"fooinfo\">", "");
                            result = result.replace("</td>", "");
                            if (i == 5) 
                            {
                                result = result.replace("</tr>", "");
                            }
                            writer.append(result);
                            if (i < 5) 
                            {
                                // If not last stat, add a ',' to CSV
                                writer.append(", ");
                            } else if (i == 5) 
                            {
                                // If last stat, go to new line
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
}
