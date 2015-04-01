import java.util.function.Function;


public class Java8Class {
	public Java8Class() {
		
	}
	
	public Function<Integer, Integer> getAFunction() {
		return i -> i + 1;
	}
}
