/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

/**
 *
 * @author fabio
 */
public class Host implements Runnable {

    private Connection con;

    public Host(Connection con) {
        this.con = con;
    }

    @Override
    public void run() {
        con.initCon();
    }

}
