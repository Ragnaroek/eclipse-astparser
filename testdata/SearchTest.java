
public class SearchTest {
	
	private static class InnerClass {
		public void innerClassMethod() {
			
		}
	}
	
	public void publicMethod1() {
		
	}
	
	public Object publicMethod11() {
		return null;
	}
	
	public String publicMethod2() {
		return null;
	}
	
	public Object publicMethod21() {
		return null;
	}
	
	private void privateMethod1() {
		
	}
	
	public final void finalMethod1() {
		
	}
	
	public static void staticMethod1() {
		
	}
	
	public final static void publicFinalStaticMethod() {
		
	}
	
	public void publicMethodStringArg(String str) {
		
	}
	
	public void publicMethod3StringArgs(String str, String str2, String str3) {
		
	}
	
	public void publicMethodMixedArgs(String str, Object obj, Integer in) {
		
	}
	
	public void publicMethodIntArg(int i) {
		
	}
	
	public void publicMethod3IntArg(int i, int i2, int i3) {
		
	}
	
	public void publicMethodMixedPrimitiveArgs(byte b, int i, double d) {
		
	}
	
	public void publicMethodMixedPrimitiveAndObjectArgs(byte b, Object o, double d) {
		
	}
	
	public final String modifiersArgsReturnType(Double d, int i) {
		return null;
	}
	
	public Object anonymousClass() {
		return new Runnable() {
			public void run() {
				//GNDN
			}
		};
	}
}

class NonPublicClass {
	
}
