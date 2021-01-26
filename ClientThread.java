import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/* 
 *  Program obslugiwania kazdego klienta w osobnym watku
 *  Autor: Uladzimir Kaviaka
 *  Data: 15 stycznia 2020
 */


public class ClientThread extends Thread {
	PhoneBookServer server;
	Socket client;
	String response;
	public String name;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	
	public ClientThread(PhoneBookServer server, Socket client) {
		this.server = server;
		this.client = client;
		new Thread(this).start();
	}
	
	@Override 
	public String toString() {
		return name;
	}
	
	private void sendMessage(String msg) {
		try {
			server.printSentMessage(msg);
			outputStream.writeObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {		
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
			name = (String)inputStream.readObject();
			server.addClient(this,name);
			while (true) {
				String msg = (String) inputStream.readObject();
				server.printGettedMessage(this, msg);
				if (msg.startsWith("LOAD")) {
					String path = msg.substring(msg.indexOf(' ') + 1);
					response = server.phoneBook.LOAD(path);
					sendMessage(response);
				}
				if(msg.startsWith("PUT")) {
					String[] data = (msg.substring(msg.indexOf(' ')+1)).split(" ");
					response = server.phoneBook.PUT(data[0],data[1]);
					sendMessage(response);
				}
				if(msg.startsWith("SAVE")) {
					String path = msg.substring(msg.indexOf(' ') + 1);
					response = server.phoneBook.SAVE(path);
					sendMessage(response);
				}
				if(msg.startsWith("REPLACE")) {
					String[] data = (msg.substring(msg.indexOf(' ')+1)).split(" ");
					response = server.phoneBook.REPLACE(data[0],data[1]);
					sendMessage(response);
				}
				if(msg.startsWith("DELETE")) {
					String path = msg.substring(msg.indexOf(' ') + 1);
					response = server.phoneBook.DELETE(path);
					sendMessage(response);
				}
				if(msg.startsWith("LIST")) {
					response = server.phoneBook.LIST();
					sendMessage(response);
				}
				if(msg.startsWith("GET")) {
					String path = msg.substring(msg.indexOf(' ') + 1);
					response = server.phoneBook.GET(path);
					sendMessage(response);
				}
				if(msg.startsWith("CLOSE")) {
					server.Close();
				}
				if(msg.startsWith("BYE")) {
					server.Close();
					client.close();
					inputStream.close();
					outputStream.close();
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Serwer nie moze odczytac wiadomosci klienta", "Blad serwera",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	}

}
