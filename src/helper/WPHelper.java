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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.prefs.*;
import org.json.JSONObject;

/**
 *
 * @author mac
 */
public class WPHelper {
    private Prowser browser = null;
    private Boolean is_logged_in = false;
    private Tab static_tab = null;
    public Prowser getBrowser(){
        if(browser==null)
            browser = new Prowser();
        return browser;
    }
    public Boolean logIn(){
        Tab t = getStaticTab();
        try{
            Request request = new Request(getPrefs().get(WP_SITEURL_KEY,"") + "/wp-login.php");
            request.setHttpMethod(Request.HTTP_METHOD_POST);
            request.addParameter("log", getPrefs().get(WP_USER_KEY, ""));
            request.addParameter("pwd", getPrefs().get(WP_PASSWORD_KEY, ""));
            request.addParameter("wp-submit", "Log In");
            request.addParameter("testcookie", "1");
            request.addParameter("redirect_to", getPrefs().get(WP_SITEURL_KEY,"") + "/wp-admin");
            Response response = t.go(request);
            return isLoggedIn();
        }catch(Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    
    public Boolean isLoggedIn(){
        Tab t = getStaticTab();
        try{
            Request r;
            r = new Request(getPrefs().get(WP_SITEURL_KEY,"") + "/console?query=is_logged_in");
            Response re = t.go(r);
            String html = re.getPageSource();
            JSONObject obj = new JSONObject(html);
            Boolean ree = obj.getString("result").equalsIgnoreCase("1");
            return ree;
        }catch(Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    public Tab getStaticTab(){
        if(static_tab==null)
            static_tab = getNewTab();
        return static_tab;
    }
    public Tab getNewTab()//String url, String username, String pass)
    {
        try{
            Tab tab = getBrowser().createTab();
                    
            return tab;
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
        
    }
    public String submitContent(String data_port, HashMap<String,String> data){
        Tab t = getNewTab();
        try{
            Request request = new Request(data_port);
            request.setHttpMethod(Request.HTTP_METHOD_POST);
            request.addParameter("submit", "submit");
            request.addParameter("action", "insert");
            for(String k : data.keySet()) {
                request.addParameter("data["+k+"]", data.get(k));
            }
            
            Response re = t.go(request);
            String html = re.getPageSource();
            JSONObject obj = new JSONObject(html);
            return html;
        }catch(Exception ex){
            return null;
        }           
        finally{
            getBrowser().closeTab(t);
        }
    }
    private Preferences prefs = null;
    public Preferences getPrefs(){
        if(prefs==null)
            prefs = Preferences.userRoot();
        return prefs;
    }
    public Boolean update(){
        HashMap<String,String> obj = new HashMap<String, String>();
        obj.put("name", "My Name");
        obj.put("order", "20");
        obj.put("id", "0");
                 
        submitContent(getPrefs().get(WP_SITEURL_KEY, "") + "?qd-api=product_cat_port", obj);
        return true;
    }
    public static String WP_USER_KEY = "wp-log";
    public static String WP_PASSWORD_KEY = "wp-pwd";
    public static String WP_SITEURL_KEY = "wp-siteurl";
}
