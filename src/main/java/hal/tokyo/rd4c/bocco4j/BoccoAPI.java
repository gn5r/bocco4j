/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bocco4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 *
 * @author gn5r
 */
public class BoccoAPI {

    private final String APIKey;
    private final String Email;
    private final String Password;
    private final String Host = "https://api.bocco.me";
    private final String Session = "/alpha/sessions";

    private String accsessToken;

    public BoccoAPI(String APIKey, String Email, String Password) {
        this.APIKey = APIKey;
        this.Email = Email;
        this.Password = Password;
    }

    /*    POSTリクエスト    */
    public String post(String url, String data) throws Exception {
        URL urls = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urls.openConnection();
        StringBuilder result = new StringBuilder();

        /*    ボディ送信を許可    */
        con.setDoInput(true);
        /*    レスポンスボディ受信を許可    */
        con.setDoOutput(true);
        /*    HTTPリクエストをPOSTに設定    */
        con.setRequestMethod("POST");
        /*    Content-Typeを設定    */
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        /*    リクエストボディの作成    */
        PrintStream ps = new PrintStream(con.getOutputStream());
        ps.print(data);
        ps.close();
        con.connect();

        /*    HTTP通信の確認    */
        final int statusCode = con.getResponseCode();

        if (statusCode == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String body = "";

            /*    レスポンスボディの情報を1行ずつ読み取る    */
            while ((body = reader.readLine()) != null) {
                result.append(body);
            }
            reader.close();

        } else {
            System.out.println("ERROR" + String.valueOf(statusCode));
        }

        con.disconnect();

        return result.toString();
    }

    public boolean createSessions() throws Exception {

        /*    Postデータを作成    */
        String data = "apikey=" + APIKey + "&email=" + Email + "&password=" + Password;

        /*    JSONデータ取得    */
        String json = post(Host + Session, data);

        /*    nullチェック    */
        if (json.matches("")) {
            return false;
        }
        /*    JSONパース    */
        JSONObject jsonObj = new JSONObject(json);
        /*    パースした内容からaccess_tokenを取り出す    */
        accsessToken = jsonObj.getString("access_token");
        
        System.out.println("---- access_token ----");
        System.out.println(accsessToken);
        
        return true;
    }
}
