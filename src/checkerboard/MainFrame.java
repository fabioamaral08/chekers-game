package checkerboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        setSize(new Dimension(850, 660));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cb = new CBController();

        createMenu();
        createTextArea();
        createButton();
        CheckerBoard checkerBoard = new CheckerBoard(8, 8, 3, cb);
        getContentPane().add(checkerBoard, BorderLayout.CENTER);

        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == host) {
                    System.out.println("host");
                }
            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == connect) {
                    System.out.println("conectar");
                }
            }
        });
        
        concede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Manda pro controlador resolver
            }
        });
    }

    public void createTextArea() {
        this.logText = new JTextArea();
        this.logText.setEditable(false);
        JScrollPane scroll = new JScrollPane(logText);
        scroll.setBorder(BorderFactory.createTitledBorder("Histórico de Jogadas"));
        scroll.setSize(200, 500);
        scroll.setLocation(CheckerBoard.HOUSE_SIDE * 9 - 15, 10);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll);
    }

    private void createButton() {
        this.concede = new JButton("Desistir");
        this.concede.setSize(200, 49);
        this.concede.setLocation(CheckerBoard.HOUSE_SIDE * 9 - 15, 510);
        
        add(concede);

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

    }

    public void connectAction() {

    }

}
