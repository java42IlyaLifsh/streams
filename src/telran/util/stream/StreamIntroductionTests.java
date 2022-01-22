package telran.util.stream;
//c
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

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
	// V.R. OK
	private Integer [] getLotoNumbers(int nNumbers, int min, int max) {
		 
		//using one stream to get array of unique random numbers in the given range
		return new Random().ints(min, max+1).distinct().limit(nNumbers)
				.boxed().toArray(Integer[]::new);
	}
	@Test
	void lotoTest () {
		Integer [] lotoNumbers = getLotoNumbers(7, 1, 49);
		assertEquals(7, lotoNumbers.length);
		
		assertEquals(7, new HashSet<Integer>(Arrays.asList(lotoNumbers)).size());
		Arrays.stream(lotoNumbers).forEach(n -> assertTrue(n >= 1 && n <= 49));
	}
	/**
	 * 
	 * @param ar
	 * @return true if ar contains two numbers, the sum of which equals half of all array's numbers
	 * complexity O[N] 
	 */
	private boolean isHalfSum(int []ar) {
		// V.R. Cool!
	if (ar.length<3) return false;
	int desSum=Arrays.stream(ar).sum() /2;
	HashSet <Integer> hash = new HashSet<>();
	for (int n:ar) {
		int x=desSum-n;
		if (hash.contains(x)) return true;
		hash.add(n);
		}
		return false;
	}
		

	@Test
	void isHalfSumTest() {
		int ar[] = {1,2, 10, -7};
		assertTrue(isHalfSum(ar));
		int ar1[] = {1, 2, 10, 7};
		assertFalse(isHalfSum(ar1));
		/* V.R. Some additional tests will not disturb. Like following:
		int ar2[] = {1,2};
		assertFalse(isHalfSum(ar2));
		int ar3[] = {13, 13, 13, 13};
		assertTrue(isHalfSum(ar3));
		*/
	}

}
