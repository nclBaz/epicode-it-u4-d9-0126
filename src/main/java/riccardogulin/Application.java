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
	}
}
