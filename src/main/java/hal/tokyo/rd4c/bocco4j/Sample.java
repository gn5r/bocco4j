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
    public static void main(String[] args) throws Exception{
        BoccoAPI boccoApi = new BoccoAPI("APIKey","exsample@bocco4j.co.jp","password");
        
        boccoApi.createSessions();
    }
}
