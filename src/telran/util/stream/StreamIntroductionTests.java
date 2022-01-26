package telran.util.stream;
//Ilya_HW33
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StreamIntroductionTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void streamArraySourceTest() {
		int ar[] = {10, -8, 17, 13, 10};
		int expected[] = {-8, 10};
		int actual[] = Arrays.stream(ar).filter(n -> n % 2 == 0).distinct().sorted()
				.toArray();
		assertArrayEquals(expected, actual);
		
	}
	@Test
	void streamRandomSourceTest() {
		Random gen = new Random();
		assertEquals(10,gen.ints().limit(10).toArray().length);
		gen.ints(10, 10, 25).forEach(n -> assertTrue(n >= 10 && n < 25));
	}
	@Test
	void streamCollectionSourceTest() {
		List<Integer> list = Arrays.asList(10, -8, 30);
		
		Integer [] actual = list.stream().filter(n -> n < 30).sorted().toArray(Integer[]::new);
		Integer [] expected = {-8, 10};
		assertArrayEquals(expected, actual);
	}
	@Test
	void streamStringSourceTest() {
		String str = "Hello";
		str.chars().forEach(n -> System.out.printf("%c;", n));
		
		
	}
	@Test
	void conversionFromIntToInteger( ) {
		List<Integer> expected = Arrays.asList(1, 2, 3);
		int ar[] = {1, 2, 3};
		List<Integer> actual = Arrays.stream(ar).boxed().toList();
		assertIterableEquals(expected, actual);
	}
	@Test
	void conversionFromIntegerToInt() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		assertEquals(6, list.stream().mapToInt(n -> n).sum());
		assertArrayEquals(new int[] {1, 2,3}, list.stream().mapToInt(n -> n).toArray());
	}
	private Integer [] getLotoNumbers(int nNumbers, int min, int max) {
		
		return new Random().ints(min, max + 1).distinct().limit(nNumbers).boxed().toArray(Integer[]::new);
	}
	@Test
	void lotoTest () {
		Integer [] lotoNumbers = getLotoNumbers(7, 1, 49);
		assertEquals(7, lotoNumbers.length);
		
		assertEquals(7, new HashSet<Integer>(Arrays.asList(lotoNumbers)).size());
		Arrays.stream(lotoNumbers).forEach(n -> assertTrue(n >= 1 && n <= 49));
		HashSet<Integer> set = new HashSet<>();
		for(int i = 0; i < 1000; i++) {
			for(int num: getLotoNumbers(7, 1, 49)) {
				set.add(num);
			}
		}
		for (int i = 1; i <= 49; i++) {
			assertTrue(set.contains(i));
		}
		
	}
	/**
	 * 
	 * @param ar
	 * @return true if ar contains two numbers, the sum of which equals half of all array's numbers
	 * with no additional arrays / collections
	 */
	private boolean isHalfSumNoSet(int []ar) {
		int halfSum = Arrays.stream(ar).sum() / 2 ;
		
		Arrays.sort(ar);
		int leftInd = 0; 
	    int rightInd = (ar.length - 1); 
	 
	    while(leftInd < rightInd) { 
	        if(ar[leftInd] + ar[rightInd] == halfSum) 
	            return true; 
	        else if(ar[leftInd] + ar[rightInd] > halfSum) 
	            rightInd--; 
	        else 
	            leftInd++; 
	    } 
	    	return false; 
		
	}
	@Test
	void isHalfSumTest() {
		int ar[] = {1,2, 10, -3, -4};
		assertTrue(isHalfSum(ar));
		int ar1[] = {1, 2, 10, 7};
		assertFalse(isHalfSum(ar1));
	}
	/**
	 * 
	 * @param ar
	 * @return true if ar contains two numbers, the sum of which equals half of all array's numbers
	 * complexity O[N] 
	 */
	private boolean isHalfSum(int[] ar) {
		HashSet<Integer> helper = new HashSet<>();
		int halfSum = Arrays.stream(ar).sum() / 2;
		for(int num: ar) {
			if (helper.contains(halfSum - num)) {
				return true;
			} 
			helper.add(num);
			
		}
		return false;
	}
	private long sum(int ar[][]) {
//		return Arrays.stream(ar).mapToLong(a -> Arrays.stream(a).asLongStream().sum()).sum();
		return Arrays.stream(ar).flatMapToInt(Arrays::stream).asLongStream().sum();
	}
	@Test
	void groupsSumTest() {
		int ar[][] = {{1,2},{3,4},{5,6}};
		assertEquals(21, sum(ar));
	}
	@Test
	void evenOddTestRandom() {
		Map<String, List<Integer>> mapEvenOdd;
		mapEvenOdd = new Random().ints(10, 1,100).boxed().collect(Collectors.groupingBy(n -> n % 2 == 0 ? "even" : "odd"));
		mapEvenOdd.forEach((k, v) -> {
			if (k.equals("even")) {
				v.forEach(n -> assertTrue(n %2 == 0));
			} else {
				v.forEach(n -> assertTrue(n % 2 == 1));
			}
		});
		
		
		
	}
	@Test
	void evenOddDownStream() {
		List<Integer> list = Arrays.asList(1, 2 ,3,4,8,10, 7);
		Map<String, Long> mapOddEven = list.stream()
				.collect(Collectors.groupingBy(n -> n % 2 == 0 ? "even" : "odd", Collectors.counting()));
		assertEquals(3, mapOddEven.get("odd"));
		assertEquals(4, mapOddEven.get("even"));
		
		
	}
	@Test
	void digitsGroupingTest() {
		List<Integer> list = Arrays.asList(10, 300, 500, 1, 2, -100);
		Map<Integer, Integer> mapDigits = list.stream()
				.collect(Collectors.groupingBy(n -> Integer.toString(Math.abs(n)).length(),
						Collectors.summingInt(n -> n)));
		assertEquals(3, mapDigits.get(1));
		assertEquals(10, mapDigits.get(2));
		assertEquals(700, mapDigits.get(3));
	}
	@Test
	void testOccurrencesCount() {
		String str = "lmn ab lmn aa; a, lmn ab.aa";
		String outputExp = "lmn -> 3\naa -> 2\nab -> 2\na -> 1\n";
		String outputActual = getOccurrences(str);
		assertEquals(outputExp, outputActual);
	}

	private String getOccurrences(String str) {
		
		return Arrays.stream(str.split("[^A-Za-z]+")) //stream of words
				.collect(Collectors.groupingBy(s -> s, TreeMap::new, Collectors.counting()))//grouping word: occurrences
				.entrySet().stream().sorted((e1, e2)->Long.compare(e2.getValue(), e1.getValue()))//sorted by occurrences in descending
				.map(e -> String.format("%s -> %d", e.getKey(),e.getValue()))//stream entries to stream of strings
				.collect(Collectors.joining("\n")) + "\n";
	}
	private void arrayShuffling(int ar[]) {
		//printing out array in the shuffling order
		//with out any additional collections
		//one pipeline 
		System.out.println("\n\narrayShuffling");
		new Random().ints(0,ar.length).distinct().limit(ar.length)
		.forEach(i->System.out.print(ar[i] + " ; "));
		System.out.println();
	}
	@Test
	void shufflingTest() {
		arrayShuffling(new int[] {1,2,3,4});
	}
	private void digitStatistics() {
		//
		//generating 1_000_000 random positive numbers [1-Integer.MAX_VALUE)
		//display out digits and occurrences sorted by occurrences in descending order
		// 1: <occurrences value>
		// 2: ...
		// 4:
		System.out.println("digitStatistics");
		new Random().ints(1_000_000, 1, Integer.MAX_VALUE)
		.flatMap(n->Integer.toString(n).chars()).boxed()
		.collect(Collectors.groupingBy(n->n, Collectors.counting()))
		.entrySet().stream()
		.sorted((e1, e2) ->Long.compare(e2.getValue(), e1.getValue()))
		.forEach(e-> System.out.printf("%c : %d\n", e.getKey(), e.getValue()));
		System.out.println();
		
	}
	@Test
	void digitStatisticsTest() {
		digitStatistics();
	}

}
