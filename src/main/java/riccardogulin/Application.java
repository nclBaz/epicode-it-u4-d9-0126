package riccardogulin;

import com.github.javafaker.Faker;
import riccardogulin.entities.User;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {


	public static void main(String[] args) {
		Supplier<List<User>> randomUserSupplier = () -> {
			ArrayList<User> users = new ArrayList<>();

			Random random = new Random();

			Faker faker = new Faker(Locale.ITALIAN);

			for (int i = 0; i < 100; i++) {
				users.add(new User(faker.lordOfTheRings().character(), faker.name().lastName(), random.nextInt(0, 100), faker.lordOfTheRings().location()));
			}
			return users;
		};

		List<User> randomUsers = randomUserSupplier.get();

		System.out.println("************************************** COLLECTORS **************************************");

		// 1. Raggruppiamo gli utenti maggiorenni per città
		Map<String, List<User>> usersByCity = randomUsers.stream()
				.filter(user -> user.getAge() < 18)
				.collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, users) -> System.out.println("City: " + city + ", users: " + users));

		// 2. Raggruppiamo gli utenti per età
		Map<Integer, List<User>> usersByAge = randomUsers.stream().collect(Collectors.groupingBy(user -> user.getAge()));
		usersByAge.forEach((age, users) -> System.out.println("Age: " + age + ", users: " + users));
//		usersByAge.get(15).forEach(user -> System.out.println(user));

		// 3. Raggruppiamo gli utenti per cognome
		Map<String, List<User>> usersBySurname = randomUsers.stream().collect(Collectors.groupingBy(user -> user.getSurname()));
		usersBySurname.forEach((surname, users) -> System.out.println("Surname: " + surname + ", users: " + users));

		// 4. Concateniamo nomi e cognomi degli utenti in una stringa
		String namesSurnames = randomUsers.stream().map(user -> user.getName() + ", " + user.getSurname())
				.reduce("", (partialString, stringToAdd) -> partialString + ". " + stringToAdd);
		System.out.println(namesSurnames);

		String namesSurnames2 = randomUsers.stream().map(user -> user.getName() + ", " + user.getSurname())
				.collect(Collectors.joining(". "));
		System.out.println(namesSurnames2);

		// 5.a Somma delle età degli utenti (tramite reduce)
		int sommaEtà = randomUsers.stream()
				.map(user -> user.getAge())
				.reduce(0, (partialSum, currentAge) -> partialSum + currentAge);
		System.out.println(sommaEtà);

		// 5.b Somma delle età degli utenti (tramite collect)
		int sommaEtà2 = randomUsers.stream()
				.collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println(sommaEtà2);

		// 6. Calcola età media degli utenti
		double average = randomUsers.stream().collect(Collectors.averagingInt(user -> user.getAge()));
		System.out.println(average);

		// 7. Raggruppiamo gli utenti per città e calcoliamo la media delle età per ogni città
		Map<String, Double> averagePerCity = randomUsers.stream()
				.collect(Collectors.groupingBy(user -> user.getCity(), Collectors.averagingInt(user -> user.getAge())));

		averagePerCity.forEach((city, avg) -> System.out.println("City: " + city + ", average: " + avg));

		// 8. Raggruppiamo gli utenti per città e calcoliamo statistiche tipo età media, somma delle età, età massima, età minima
		Map<String, IntSummaryStatistics> statsPerCity = randomUsers.stream()
				.collect(Collectors.groupingBy(user -> user.getCity(), Collectors.summarizingInt(user -> user.getAge())));

		statsPerCity.forEach((city, stats) -> System.out.println("City: " + city + ", stats: " + stats));

		System.out.println("---------------------------------- COMPARATORS --------------------------------------------------------");
		// 1. Ordiniamo la lista utenti per età crescente
		List<User> sortedByAge = randomUsers.stream().sorted(Comparator.comparing(user -> user.getAge())).toList();
		sortedByAge.forEach(user -> System.out.println(user));

		// Alternativa compatta con :: al posto delle lambda
//		List<User> sortedByAge2 = randomUsers.stream().sorted(Comparator.comparing(User::getAge)).toList();
//		sortedByAge.forEach(System.out::println);

		// 2. Ordiniamo gli utenti per età decrescente
		List<User> sortedByAgeDesc = randomUsers.stream().sorted(Comparator.comparing(User::getAge).reversed()).toList();
		sortedByAgeDesc.forEach(System.out::println);
		// Se uso reversed per invertire l'ordine non posso usare la sintassi semplice con le lambda (a meno di non fare un casting
		// (User user) -> user.getAge(). Alternativa uso la sintassi :: User::getAge

		System.out.println("-----------------------------");
		// 3. Ordiniamo gli utenti per cognome ordine discendente
		List<User> sortedBySurname = randomUsers.stream().sorted(Comparator.comparing(User::getSurname).reversed()).toList();
		sortedBySurname.forEach(System.out::println);


	}
}
