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
import java.util.UUID;
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
    private final String Rooms = "/alpha/rooms";
    private final String Rooms_Joined = "/joined?";
    private final String Message = "/messages";

    private String accsessToken;
    private String roomID;

    public BoccoAPI(String APIKey, String Email, String Password) {
        this.APIKey = APIKey;
        this.Email = Email;
        this.Password = Password;
    }

    /*    POSTリクエスト    */
    private String post(String url, String data) throws Exception {
        /*    接続先URLのインスタンス生成    */
        URL urls = new URL(Host + url);
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
        con.setRequestProperty("Accept-Language", "jp");

        /*    リクエストボディの作成    */
        PrintStream ps = new PrintStream(con.getOutputStream());
        ps.print(data);
        ps.close();
        /*    接続開始    */
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
            System.out.println("ERROR Code:" + String.valueOf(statusCode));
        }

        con.disconnect();

        return result.toString();
    }

    public boolean createSessions() throws Exception {

        /*    Postデータを作成    */
        String data = "apikey=" + APIKey + "&email=" + Email + "&password=" + Password;

        /*    JSONデータ取得    */
        String json = post(Session, data);

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

    /*    GETリクエスト    */
    private String get(String url, String data) throws Exception {
        /*    接続先URLのインスタンス生成    */
        URL urls = new URL(Host + url + data);
        HttpURLConnection con = (HttpURLConnection) urls.openConnection();
        StringBuilder result = new StringBuilder();

        /*    レスポンスボディ受信を許可    */
        con.setDoOutput(true);
        /*    HTTPリクエストをGETに設定    */
        con.setRequestMethod("GET");
        /*    Content-Typeを設定    */
        con.setRequestProperty("Content-Type", "application/json");

        /*    接続開始    */
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
            System.out.println("ERROR Code:" + String.valueOf(statusCode));
        }

        con.disconnect();

        return result.toString();
    }

    public boolean getFirstRooID() throws Exception {
        String data = "access_token=" + accsessToken;

        String json = get(Rooms + Rooms_Joined, data);

        if (json.matches("")) {
            return false;
        }

        String room = json.substring(json.indexOf("[{\"uuid\":\"") + 1);
        room = room.substring(0, room.indexOf("\"background_image\"") - 1) + "}\n";

        /*    JSONパース    */
        JSONObject jsonObj = new JSONObject(room);
        roomID = jsonObj.getString("uuid");

        System.out.println("---- roomID ----");
        System.out.println(roomID);

        return true;
    }

    public boolean postMessage(String message) throws Exception {
        /*    UUID乱数ベースの作成*/
        UUID uuid = UUID.randomUUID();

        /*    POSTボディデータを作成    */
        String data = "access_token=" + accsessToken
                + "&media=text&text=" + message
                + "&unique_id=" + uuid.toString();

        String json = post(Rooms + "/" + roomID + Message, data);

        if (json.matches("")) {
            return false;
        }

        return true;
    }
}
