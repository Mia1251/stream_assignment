package se.lexicon.vxo.service;

import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is not make all tests pass (except task1 because its non testable).
 * You have to solve each task by using a java.util.Stream or any of it's variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 */
public class StreamAssignment {

    private static List<Person> people = People.INSTANCE.getPeople();

    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //Write code here
        integers.forEach(System.out::println);
    }

    /**
     * Turning people into a Stream count all members
     */
    @Test
    public void task2() {
        long amount = 0;

        //Write code here
        amount = people.stream().count();
        System.out.println("Result: " + amount);

        assertEquals(10000, amount);
    }

    /**
     * Count all people that has Andersson as lastName.
     */
    @Test
    public void task3() {
        long amount = 0;
        int expected = 90;

        //Write code here
        amount = people.stream().filter(p -> p.getLastName().equalsIgnoreCase("Andersson")).count();
        System.out.println("There are " + amount + " persons that have Andersson as lastname.");

        assertEquals(expected, amount);
    }

    /**
     * Extract a list of all female
     */
    @Test
    public void task4() {
        int expectedSize = 4988;
        List<Person> females = null;

        //Write code here
        females = people.stream().filter(p -> p.getGender() == Gender.FEMALE).collect(Collectors.toList());
        System.out.println(females.size());

        assertNotNull(females);
        assertEquals(expectedSize, females.size());
    }

    /**
     * Extract a TreeSet with all birthDates
     */
    @Test
    public void task5() {
        int expectedSize = 8882;
        Set<LocalDate> dates = null;

        //Write code here
        dates = people.stream().map(p -> p.getDateOfBirth()).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(dates.size());

        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());
    }

    /**
     * Extract an array of all people named "Erik"
     */
    @Test
    public void task6() {
        int expectedLength = 3;

        Person[] result = null;

        //Write code here
        result = people.stream().filter(p -> p.getFirstName().equalsIgnoreCase("Erik")).toArray(Person[]::new);
        System.out.println(result.length);

        assertNotNull(result);
        assertEquals(expectedLength, result.length);
    }

    /**
     * Find a person that has id of 5436
     */
    @Test
    public void task7() {
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);

        Optional<Person> optional = null;

        //Write code here
        optional = people.stream().filter(p -> p.getPersonId() == expected.getPersonId()).findFirst();
        System.out.println(optional.get());


        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8() {
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");

        Optional<Person> optional = null;

        //Write code here
        optional = people.stream().min((o1, o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth()));
        System.out.println(optional.get().getDateOfBirth());

        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());
    }

    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9() {
        int expectedSize = 892;
        LocalDate date = LocalDate.parse("1920-01-01");

        List<PersonDto> dtoList = null;

        //Write code here
        Function<Person, PersonDto> personDtoFunction = person -> {
            PersonDto aPerson = new PersonDto(person.getPersonId(), person.getFirstName());
            return aPerson;
        };
        dtoList = people.stream().filter(p -> p.getDateOfBirth().isBefore(LocalDate.parse("1920-01-01")))
                .map(personDtoFunction).collect(Collectors.toList());
        System.out.println(dtoList.size());

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10() {
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;

        Optional<String> optional = null;

        //Write code here
        Predicate<Person> findId = p -> p.getPersonId()==personId;
        Function<Person, String> birthdateString = p -> p.getDateOfBirth().getDayOfWeek() + " " + p.getDateOfBirth().getDayOfMonth()
                + " " + p.getDateOfBirth().getMonth() + " " + p.getDateOfBirth().getYear();
        optional = people.stream().filter(findId).map(birthdateString).findFirst();
        System.out.println(optional.get());


        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11() {
        ToIntFunction<Person> personToAge =
                person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
        double expected = 54.42;
        double averageAge = 0;

        //Write code here
        averageAge = people.stream().mapToInt(personToAge).reduce(0, Integer::sum)/10000d;
        System.out.println(averageAge);


        assertTrue(averageAge > 0);
        assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12() {
        String[] expected = {"Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto"};

        String[] result = null;

        //Write code here
        Predicate<Person> extractPalindromes = person -> new StringBuilder(person.getFirstName())
                .reverse().toString().equalsIgnoreCase(person.getFirstName());
        Function<Person, String> personToString = Person::getFirstName;
        Comparator<String> sort = String::compareToIgnoreCase;
        result = people.stream().filter(extractPalindromes).map(personToString).sorted(sort).distinct().toArray(String[]::new);
        System.out.println(Arrays.toString(result));


        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13() {
        int expectedSize = 107;
        Map<String, List<Person>> personMap = null;

        //Write code here
        personMap = people.stream().collect(Collectors.groupingBy(Person::getLastName));
        System.out.println(personMap.size());

        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14() {
        LocalDate[] _2020_dates = null;

        //Write code here
        LocalDate start = LocalDate.parse("2020-01-01");
        LocalDate end = LocalDate.parse("2021-01-01");

        _2020_dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end)).toArray(LocalDate[]::new);
        System.out.println(_2020_dates.length);

        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.length);
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates[0]);
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates[_2020_dates.length - 1]);
    }

}
