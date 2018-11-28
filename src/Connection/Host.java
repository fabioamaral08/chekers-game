/*
 * Thread para que o host fique esperando uma nova conexão
 */
package Connection;

/**
 *
 * @author fabio
 */
public class Host implements Runnable {

    /**
     * Objeto da classe Connection
     */
    private Connection con;

    /**
     * Construtor da classe
     * 
     * @param con Connection
     */
    public Host(Connection con) {
        this.con = con;
    }

    @Override
    public void run() {
        con.initCon();
    }

}
