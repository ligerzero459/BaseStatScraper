/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mottley;

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
public class URLFunctions {
    
    public static String downloadPage(String url) 
    {
        // Download page from a given URL
        // Modular, able to be put in any program
        try 
        {
            URL pageURL = new URL(url);
            StringBuilder text = new StringBuilder();
            Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");
            try 
            {
                while (scanner.hasNextLine()) 
                {
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
