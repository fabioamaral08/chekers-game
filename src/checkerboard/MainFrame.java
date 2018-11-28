package checkerboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem host;
    private JMenuItem connect;
    private JTextArea logText;
    private JButton concede;
    private CBController cb;
    private boolean isHost;
    private CheckerBoard checkerBoard;
    private JLabel turn;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainFrame() {
        this.setTitle("Damas");
        isHost = false;
        setSize(new Dimension(850, 660));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        cb = new CBController();

        createMenu();
        createTextArea();
        createButton();
        createLabel();
        checkerBoard = new CheckerBoard(8, 8, 3, cb);
        cb.setMF(this);
        getContentPane().add(checkerBoard, BorderLayout.CENTER);

        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == host) {
                    hostAction();
                }
            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == connect) {
                    connectAction();
                }
            }
        });

        concede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cb.concede();
            }
        });
    }

    public void createTextArea() {
        this.logText = new JTextArea();
        this.logText.setEditable(false);
        this.logText.setLineWrap(true);
        this.logText.setFont(new Font("Dialog", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(logText);
        scroll.setBorder(BorderFactory.createTitledBorder("Histórico de Jogadas"));
        scroll.setSize(200, 7 * CheckerBoard.HOUSE_SIDE);
        scroll.setLocation(CheckerBoard.HOUSE_SIDE * 9 - 15, CheckerBoard.HOUSE_SIDE / 2);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll);
    }

    private void createButton() {
        this.concede = new JButton("Desistir");
        this.concede.setSize(200, CheckerBoard.HOUSE_SIDE / 2);
        this.concede.setLocation(CheckerBoard.HOUSE_SIDE * 9 - 15, 7 * CheckerBoard.HOUSE_SIDE + CheckerBoard.HOUSE_SIDE / 2);
        this.concede.setFont(new Font("Dialog", Font.BOLD, 15));
        this.concede.setEnabled(false);

        add(concede);

    }

    private void createLabel() {
        this.turn = new JLabel("Turno", JLabel.CENTER);
        this.turn.setSize(200, CheckerBoard.HOUSE_SIDE / 2);
        this.turn.setLocation(CheckerBoard.HOUSE_SIDE * 9 - 15, 0);
        this.turn.setFont(new Font("Dialog", Font.BOLD, 16));

        add(this.turn);
    }

    public void createMenu() {
        this.menuBar = new JMenuBar();
        menu = new JMenu();
        menu.setText("Jogar");
        host = new JMenuItem();
        host.setName("hostBtn");
        host.setText("Ser host");

        connect = new JMenuItem();
        connect.setName("conectarBtn");
        connect.setText("Conectar");

        menu.add(host);
        menu.add(connect);
        this.menuBar.add(menu);
        this.setJMenuBar(menuBar);

    }

    public void hostAction() {
        if (isHost) {
            this.setTitle("Damas");
            host.setText("Ser host");
            connect.setText("Conectar");
            cb.cancelHost();
            this.isHost = false;
        } else {
            this.setTitle("Buscando ...");
            host.setText("Cancelar");
            connect.setText("Informações");
            isHost = true;
            cb.host();
        }

    }

    public void connectAction() {
        if (isHost) {
            String msg = "Seu IP é: " + cb.getIP() + "\n"
                    + "E sua porta é : " + cb.getPort();

            JOptionPane.showMessageDialog(null, msg);
        } else {
            String ip = JOptionPane.showInputDialog("Digite o IP:");
            if (ip == null) {
                return;
            }
            cb.connect(ip, 5000);
        }

    }

    public void setTurn(String str) {
        this.turn.setText(str);
    }
    
    

    public void setLogText(String text) {
        String str = this.logText.getText();
        this.logText.setText(str + text);
    }

    public CheckerBoard getCheckerBoard() {
        return checkerBoard;
    }

    public JMenu getMenu() {
        return menu;
    }

    public JButton getConcede() {
        return concede;
    }
    
    

}
