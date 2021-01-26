import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/* 
 *  Program przedstawia ksiazke z ktorej moga 
 *  jednoczesnie korzystac wiele uzytkownikow
 *  Autor: Uladzimir Kaviaka
 *  Data: 15 stycznia 2020
 */


public class PhoneBook implements Serializable {

	private static final long serialVersionUID = 1L;
    ConcurrentHashMap<String, String> phoneBook;

	public PhoneBook() {
		phoneBook = new ConcurrentHashMap<String, String>();
	}

	public String LOAD(String filename) throws Exception {
		try (ObjectInputStream outputStream = new ObjectInputStream(new FileInputStream(filename))) {
			PhoneBook tmp = (PhoneBook) outputStream.readObject();
			this.phoneBook = tmp.phoneBook;
			return "OK";
		} catch (Exception e) {
			return  "ERROR: "+ "Blad podczas czytania do pliku";
		}
	}

	public String showCurrentPhoneBook() {
		StringBuilder sb = new StringBuilder();
		phoneBook.forEach((name, number) -> sb.append("Imie: " + name + " <-> Numer: " + number+"\n"));
		return sb.toString();
	}

	public String SAVE(String filename) throws Exception {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
			outputStream.writeObject(this);
		} catch (Exception e) {
			return "ERROR: "+ "Blad podczas zapisywania do pliku";
		}
		return "OK";
	}

	public String GET(String name) {
		if (phoneBook.containsKey(name)) {
			return "OK " + phoneBook.get(name);
		} else
			return "ERROR: "+ "Nie ma takiego uzytkownika";
	}

	public String PUT(String name, String number) {
		if (name.isEmpty() || number.isEmpty())
			return "Niepoprawne dane";
		phoneBook.put(name, number);
		return "OK";
	}

	public String REPLACE(String name, String number) {
		if (phoneBook.containsKey(name)) {
			phoneBook.replace(name, number);
			return "OK";
		} else
			return "ERROR: "+ "Nie ma takiego uzytkownika";
	}

	public String DELETE(String name) {
		if (phoneBook.containsKey(name)) {
			phoneBook.remove(name);
			return "OK";
		} else
			return "ERROR: "+ "Nie ma takiego uzytkownika";
	}

	public String LIST() {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> book = phoneBook.keys();
		while (book.hasMoreElements()) {
			sb.append(book.nextElement() + "\n");
		}
		return "OK\n"+sb.toString();
	}

}
