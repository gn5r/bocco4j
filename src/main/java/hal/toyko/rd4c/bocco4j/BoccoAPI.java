/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.toyko.rd4c.bocco4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private final String APIEndpoint = "https://api.bocco.me/alpha/";

    private String accsessToken;

    public BoccoAPI(String APIKey, String Email, String Password) {
        this.APIKey = APIKey;
        this.Email = Email;
        this.Password = Password;
    }

    /*    getリクエスト    */
    private String get(String URL, String data) {
        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(this.APIEndpoint + URL + data);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();

            // HTTPレスポンスコード
            final int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // テキストを取得する
                final InputStream in = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                // 1行ずつテキストを読み込む
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                System.out.println(status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                // コネクションを切断
                connection.disconnect();
            }
        }
        System.out.println("result=" + result.toString());

        return result.toString();

    }

    /*    postリクエスト  */
    private String post(String URL, String data) {

        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(this.APIEndpoint + URL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(data);
            out.close();
            connection.connect();

            // HTTPレスポンスコード
            final int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // テキストを取得する
                final InputStream in = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;

                // 1行ずつテキストを読み込む
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                System.out.println(status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                // コネクションを切断
                connection.disconnect();
            }
        }

        return result.toString();
    }

    /*    セッションを作成しアクセストークンを取得する*/
    public boolean createSessions() {
        String data = "apikey = " + this.APIKey + "&email=" + this.Email + "&password=" + this.Password;

        String response = this.post("sessions/", data);

        JSONObject json = new JSONObject(response);

        System.out.println(json.getString("access_token"));

        if (json.getString("code").matches("401001")) {
            return false;
        }

        return true;
    }

    /*/
    
    /*    ルームへメッセージを送信    */
    public boolean postMessage(String message) {

        return true;
    }

    private String getAccsessToken() {
        return accsessToken;
    }

    private void setAccsessToken(String accsessToken) {
        this.accsessToken = accsessToken;
    }

}
