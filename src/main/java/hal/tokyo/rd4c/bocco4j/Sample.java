/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bocco4j;

/**
 *
 * @author gn5r
 */
public class Sample {

    public static void main(String[] args) throws Exception {
        BoccoAPI boccoApi = new BoccoAPI("oXclKHqmDQabjqfuLr2GNd0ASovTuzVyhAtvnyfDEbYGJD4hrIkmZ6hcUPqeP5CF",
                "terashima.hidetoshi.150147@roivy.net",
                "h8910teto");

        boccoApi.createSessions();
        boccoApi.getFirstRooID();
        if (boccoApi.postMessage(args[0]) == true) {
            System.out.println("送信しました");
        } else {
            System.out.println("失敗です");
        }
    }
}
