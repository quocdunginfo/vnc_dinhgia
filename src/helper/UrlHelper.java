/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author mac
 */
public class UrlHelper {
    public static String getContent(){
        try{
            URL url = new URL("http://localhost/vnc_2015/?qd-api=product_port");
            InputStream is = url.openConnection().getInputStream();

            BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
            String re = "";
            String line = null;
            while( ( line = reader.readLine() ) != null )  {
               re+=line+'\n';
            }
            reader.close();
            return re;
        }catch(Exception ex){
            return null;
        }
    }
    
}
