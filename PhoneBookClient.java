import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/* 
 *  Program przedstawia pojedynczego Klienta
 *  Autor: Uladzimir Kaviaka
 *  Data: 15 stycznia 2020
 */


public class PhoneBookClient extends JFrame implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	static final int PORT = 15500;

	JLabel msgLbl = new JLabel("Wiadomosc: ");
	JLabel dialogLbl = new JLabel("Komunikat: ");
	JPanel p = new JPanel(null);
	JTextField msgField = new JTextField(20);
	JTextArea dialogArea = new JTextArea();
	JScrollPane scroll = new JScrollPane();
	private Socket socket;
	private String name;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;

	public PhoneBookClient() {
		name = JOptionPane.showInputDialog("Wrpowadz imie klienta");
		setTitle("Client: "+name);
		setSize(300, 500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					output.close();
					input.close();
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}

			@Override
			public void windowClosed(WindowEvent event) {
				windowClosing(event);
			}
		});

		msgLbl.setBounds(10, 5, 80, 20);
		p.add(msgLbl);
		msgField.addActionListener(this);

		msgField.setBounds(90, 5, 185, 20);
		p.add(msgField);

		dialogLbl.setBounds(10, 40, 80, 20);
		p.add(dialogLbl);

		dialogArea.setEditable(false);
		dialogArea.setLineWrap(true);
		dialogArea.setWrapStyleWord(true);
		scroll = new JScrollPane(dialogArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(90, 40, 185, 400);
		p.add(scroll);

		setContentPane(p);
		setVisible(true);
		new Thread(this).start();
	}

	public static void main(String[] args) {
		new PhoneBookClient();
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket("localhost", PORT);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(name);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Problem z polaczeniem do serwera", "Blad klienta",
					JOptionPane.INFORMATION_MESSAGE);
		}
		try {
			while (true) {
				String response = (String) input.readObject();
				dialogArea.append("Serwer: " + response + "\n");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Problem z odczytaniem wiadomosci serwera", "Blad klienta",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object sender = e.getSource();
		if (sender == msgField) {
			try {
				String msg = msgField.getText();
				if(msg.equals("BYE")) {
					input.close();
					output.close();
					System.exit(0);
				}
				if(msg.equals("CLOSE")) {
					JOptionPane.showMessageDialog(null, "Gniazdo serwera bylo zamkniete", "Informacja",
							JOptionPane.INFORMATION_MESSAGE);
				}
				output.writeObject(msg);
				dialogArea.append(msg + "\n");
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Problem z wysylka wiadomosci", "Blad klienta",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
