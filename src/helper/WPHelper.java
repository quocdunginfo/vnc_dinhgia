/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.zenkey.net.prowser.Prowser;
import com.zenkey.net.prowser.Request;
import com.zenkey.net.prowser.Response;
import com.zenkey.net.prowser.Tab;
import java.util.prefs.*;

/**
 *
 * @author mac
 */
public class WPHelper {
    public Tab getContent()//String url, String username, String pass)
    {
        Preferences prefs = getPrefs();
        try{
            Prowser prowser = new Prowser();
            Tab tab = prowser.createTab();
            Request request = new Request("http://localhost/vnc_2015/wp-login.php");
            request.setHttpMethod(Request.HTTP_METHOD_POST);
            request.addParameter("log", prefs.get(WP_USER_KEY, "admin"));
            request.addParameter("pwd", prefs.get(WP_PASSWORD_KEY, "admin"));
            request.addParameter("wp-submit", "Log In");
            request.addParameter("testcookie", "1");
            request.addParameter("redirect_to", "http://localhost/vnc_2015/wp-admin.php");
            
            Response response = tab.go(request);
            String html = response.getPageSource();
            request = new Request("http://localhost/vnc_2015/?qd-api=product_port");
            response = tab.go(request);
            html = response.getPageSource();
            System.out.println(html);
            return tab;
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
        
    }
    public void setAuthen(){
        try{
            Preferences prefs = getPrefs();
            prefs.put(WP_USER_KEY, "admin");
            prefs.put(WP_PASSWORD_KEY, "admin");
            prefs.flush();
        }catch(Exception ex){}
    }
    public Preferences getPrefs(){
        Preferences prefs;
        return prefs = Preferences.systemNodeForPackage(this.getClass());
    }
    public static String WP_USER_KEY = "wp-log";
    public static String WP_PASSWORD_KEY = "wp-pwd";
}
