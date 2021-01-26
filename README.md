# Klient-Serwer Request
Program przedstawia pracę klienta oraz serwera. Kilka klientów podłączane do serwera oraz korzystają ze wspólnej książki telefonicznej.
# Idea
Napisanie serwera podobnego do pracy Microsoft SQL Server, który obsługuje przychodzące komendy i wykonuje te komendy na serwerze i wysyła klientowi wiadomości zwrotne.
# Serwer
Serwer jest wielowątkowym czyli każdy nowe przychodzące połączenie będzie obsługiwane w nowym wątku co pozwala równolegle wielu użytkownikom korzystać ze wspólnej książki telefonicznej. Książkę telefoniczną przedstawia kolekcja **ConcurrentHashMap<K,V>** która jest biezpieczna wątkowo.
# Komendy akceptujące przez Serwer
 - ``LOAD filename`` — *wczytanie książki telefonicznej z pliku binranego*
 - ``SAVE filename`` — *zapisywanie książki telefonicznej do pliku binranego*
 - ``GET username`` — *zwraca numer danego użytkownika*
 - ``PUT username number`` — *dodaje do książki użytkownika z numerem*
 - ``REPLACE username number`` — *zamienia numer istniejącego użytkownika*
 - ``DELETE username`` — *usuwa z książki użytkownika*
 - ``LIST`` — *zwraca listę imion zawartych w książce*
 - ``CLOSE`` — *zamyka nasłuchiwanie przychodzących połączeń*
 - ``BYE`` — *zakońcenie pracy serwera oraz klienta*
# Screenshots
![Screenshot_1](https://user-images.githubusercontent.com/19534189/105908886-11968400-6027-11eb-92be-4de48ea2f4d7.png)
