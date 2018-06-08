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
        String result = "";

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

        switch (statusCode) {

            /*    レスポンスコード :200    */
            case HttpURLConnection.HTTP_OK:
                result = getResut(con);
                break;

            /*    レスポンスコード:201    */
            case HttpURLConnection.HTTP_CREATED:
                result = getResut(con);
                break;

            /*    レスポンスコード:401    */
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                result = "401";
                break;

            /*    レスポンスコード:400    */
            case HttpURLConnection.HTTP_BAD_REQUEST:
                result = "400";
                break;
        }

        con.disconnect();

        return result;
    }

    /*    access_tokenを取得する    */
    public boolean createSessions() throws Exception {

        /*    Postデータを作成    */
        String data = "apikey=" + APIKey + "&email=" + Email + "&password=" + Password;

        /*    JSONデータ取得    */
        String json = post(Session, data);

        /*    レスポンスコード読取り    */
        switch (json) {
            case "401":
                System.out.println("APIKey,Email,Passwordが間違っています");
                return false;

            case "400":
                System.out.println("BAD REQUEST");
                return false;
        }

        /*    JSONパース    */
        accsessToken = parseJson(json, "access_token");

        System.out.println("---- access_token ----");
        System.out.println(accsessToken);

        return true;
    }

    /*    GETリクエスト    */
    private String get(String url, String data) throws Exception {
        /*    接続先URLのインスタンス生成    */
        URL urls = new URL(Host + url + data);
        HttpURLConnection con = (HttpURLConnection) urls.openConnection();
        String result = "";

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

        switch (statusCode) {

            case HttpURLConnection.HTTP_OK:
                result = getResut(con);
                break;

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                result = "401";
                break;

            case HttpURLConnection.HTTP_BAD_REQUEST:
                result = "400";
                break;
        }

        con.disconnect();

        return result;
    }

    /*    1件目のroom情報を取得しIDを取り出す    */
    public boolean getFirstRooID() throws Exception {
        String data = "access_token=" + accsessToken;

        String json = get(Rooms + Rooms_Joined, data);

        /*    レスポンスコード読取り    */
        switch (json) {
            case "401":
                System.out.println("APIKey,Email,Passwordが間違っています");
                return false;

            case "400":
                System.out.println("BAD REQUEST");
                return false;
        }

        String room = json.substring(json.indexOf("[{\"uuid\":\"") + 1);
        room = room.substring(0, room.indexOf("\"background_image\"") - 1) + "}\n";

        /*    JSONパース    */
        roomID = parseJson(room, "uuid");

        System.out.println("---- roomID ----");
        System.out.println(roomID);

        return true;
    }

    /*    テキストメッセージ送信    */
    public boolean postMessage(String message) throws Exception {
        /*    UUID乱数ベースの作成*/
        UUID uuid = UUID.randomUUID();

        /*    POSTボディデータを作成    */
        String data = "access_token=" + accsessToken
                + "&media=text&text=" + message
                + "&unique_id=" + uuid.toString();

        String json = post(Rooms + "/" + roomID + Message, data);

        switch (json) {
            case "401":
                System.out.println("APIKey,Email,Passwordが間違っています");
                return false;

            case "400":
                System.out.println("BAD REQUEST");
                return false;
        }

        System.out.println("送信:" + parseJson(json, "text"));
        return true;
    }

    /*    JSONをパースして必要な要素を取り出す    */
    private String parseJson(String json, String key) {
        JSONObject jsonObj = new JSONObject(json);

        return jsonObj.getString(key);
    }

    /*    レスポンスボディ取得    */
    private String getResut(HttpURLConnection con) throws Exception {

        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String body;

        /*    レスポンスボディの情報を1行ずつ読み取る    */
        while ((body = reader.readLine()) != null) {
            result.append(body);
        }
        reader.close();

        return result.toString();
    }
}
