/* 
 *  Program uruchamia serwer i dwoch klientow
 *  Autor: Uladzimir Kaviaka
 *  Data: 15 stycznia 2020
 */

class Tester {
	
	public static void main(String [] args) throws Exception{
		new PhoneBookServer();
		new PhoneBookClient();
		new PhoneBookClient();
	}	
}

