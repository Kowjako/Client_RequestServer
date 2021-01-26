import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/* 
 *  Program przedstawia Serwer
 *  Autor: Uladzimir Kaviaka
 *  Data: 15 stycznia 2020
 */


public class PhoneBookServer extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int PORT = 15500;
	PhoneBook phoneBook = new PhoneBook();
	
	JPanel p = new JPanel(null);
	JLabel dialogLbl = new JLabel("Komunikat: ");
	JLabel users = new JLabel("Klienci: ");
	JTextArea dialogArea = new JTextArea();
	JScrollPane scroll = new JScrollPane();
	ServerSocket listenSocket;
	private boolean notClosed = false;
	
	JComboBox<ClientThread> clients = new JComboBox<ClientThread>();

	public PhoneBookServer() {
		setTitle("PhoneBook - Serwer");
		setSize(300, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		dialogArea.setEditable(false);
		dialogArea.setLineWrap(true);
		dialogArea.setWrapStyleWord(true);
		scroll = new JScrollPane(dialogArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(90, 40, 185, 410);
		p.add(scroll);
		
		dialogLbl.setBounds(5,40,80,20);
		p.add(dialogLbl);
		
		users.setBounds(5,10,80,20);
		p.add(users);
		
		clients.setBounds(90,10,185,20);
		clients.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				e.getItem();
			}
		});
		p.add(clients);
		
		setContentPane(p);
		setVisible(true);
		new Thread(this).start();
	}
	
	public static void main(String[] args) {
		new PhoneBookServer();
	}
	
	public void printGettedMessage(ClientThread client, String message) {
		dialogArea.append(client.name+": " + message + "\n");
	}
	
	public void printSentMessage(String message) {
		dialogArea.append(message + "\n");
	}
	
	public void Close() throws IOException {
		notClosed = true;
		listenSocket.close();
	}
	
	public void addClient(ClientThread client,String name) {
		client.setName(name);
		clients.addItem(client);
	}
	
	public void removeClient(ClientThread client) {
		clients.removeItem(client);
	}
	@Override
	public void run() {
		boolean socket_created = false;
		try {
			listenSocket = new ServerSocket(PORT);
			socket_created = true;
			while (!notClosed) {
				Socket client = listenSocket.accept();
				if (client != null)
					new ClientThread(this, client);
			}
			listenSocket.close();
		} catch (IOException e) {
			if(socket_created==false) JOptionPane.showMessageDialog(this, "Nie mozna utworyzc serwer na tym porcie", "Blad serwera", JOptionPane.INFORMATION_MESSAGE);
			else
				if(notClosed) JOptionPane.showMessageDialog(this, "Gniazdo serwera zamkniete przez slowo CLOSE", "Odpowiedz serwera", JOptionPane.INFORMATION_MESSAGE);
				else
			JOptionPane.showMessageDialog(this, "Nie ma polaczaenia z klientem", "Blad serwera", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}

	}
}
