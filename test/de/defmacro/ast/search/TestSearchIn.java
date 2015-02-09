package de.defmacro.ast.search;

import static de.defmacro.ast.Constants.*;
import static org.junit.Assert.*;
import static de.defmacro.ast.search.ExpressionFactory.*;

import java.io.File;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.junit.Test;
import de.defmacro.ast.JavaParser;
import de.defmacro.ast.syntax.SyntaxException;

public class TestSearchIn 
{
	private SearchIn newSearch(final File file) throws Exception {
		return new SearchIn(new JavaParser().parseCompilationUnit(file));
	}
	
	private SearchIn newSearchInSearchTest() throws Exception {
		return newSearch(new File(TEST_DIR, "SearchTest.java"));
	}
	
	private SearchIn newSearchInTestDir(final String javaFile) throws Exception {
		return newSearch(new File(TEST_DIR, javaFile));
	}
	
	@Test public void testEmptySearch() throws Exception {
		assertTrue(newSearchInSearchTest().searchThis());
	}
	
	@Test public void testSearchIsClass() throws Exception {
		assertTrue(newSearchInSearchTest().isClass("SearchTest").searchThis());
		assertFalse(newSearchInSearchTest().isClass("DNExist").searchThis());
	}
	
	@Test public void testSearchIsClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().isClass("*").searchThis());
		assertFalse(newSearchInTestDir("EmptyFile.java").isClass("*").searchThis());
	}
	
	@Test public void testSearchInClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").searchThis());
		assertFalse(newSearchInSearchTest().inClass("DNExist").searchThis());
	}
	
	@Test public void testSearchTwoClasses() throws Exception {
		assertTrue(newSearchInSearchTest().isClass("SearchTest").andIsClass("NonPublicClass").searchThis());
		assertTrue(newSearchInSearchTest().isClass("SearchTest").andInClass("NonPublicClass").searchThis()); //strange, but syntactic correct
		assertFalse(newSearchInSearchTest().isClass("SearchTest").andIsClass("DNExists").searchThis());
		assertFalse(newSearchInSearchTest().isClass("DNExists").andIsClass("NonPublicClass").searchThis());
		assertFalse(newSearchInSearchTest().isClass("DNExists").andInClass("NonPublicClass").searchThis());
	}
	
	@Test(expected=SyntaxException.class) 
	public void testIllegalSearchSameTwoClasses() throws Exception {
		newSearchInSearchTest().isClass("SearchTest").andIsClass("SearchTest");
	}
	
	@Test(expected=SyntaxException.class) 
	public void testIllegalSearchTwoWildcards() throws Exception {
		newSearchInSearchTest().isClass("*").andIsClass("*");
	}
	
	@Test public void testSearchWithoutReturnTypePublicMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").searchThis());
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withoutArguments().searchThis());
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withModifiers(ModifierKeyword.PUBLIC_KEYWORD).searchThis());
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withModifiers(ModifierKeyword.PUBLIC_KEYWORD).withoutArguments().searchThis());
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod11").searchThis()); //this method has _not_ return type void, but return type is not set
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("doesNotExist").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").isMethod("publicMethod1").searchThis()); //Method in other class
	}
	
	@Test public void testSearchWithStringReturnTypePublicMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod2").withReturnType(String.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod21").withReturnType(String.class).searchThis());
	}
	
	@Test public void testSearchPrivateMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("privateMethod1").searchThis());
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("privateMethod1").withModifiers(ModifierKeyword.PRIVATE_KEYWORD).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("privateMethod1").withModifiers(ModifierKeyword.PUBLIC_KEYWORD).searchThis());
	}
	
	@Test public void testSearchFinalMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("finalMethod1").withModifiers(ModifierKeyword.FINAL_KEYWORD).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withModifiers(ModifierKeyword.FINAL_KEYWORD).searchThis());
	}
	
	@Test public void testSearchStaticMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("staticMethod1").withModifiers(ModifierKeyword.STATIC_KEYWORD).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withModifiers(ModifierKeyword.STATIC_KEYWORD).searchThis());
	}
	
	@Test public void testSearchPublicFinalStaticMethodNoArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicFinalStaticMethod").withModifiers(ModifierKeyword.STATIC_KEYWORD, ModifierKeyword.FINAL_KEYWORD, ModifierKeyword.PUBLIC_KEYWORD).searchThis());
		//modifier order shouldn't matter
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicFinalStaticMethod").withModifiers(ModifierKeyword.FINAL_KEYWORD, ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.STATIC_KEYWORD).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withModifiers(ModifierKeyword.FINAL_KEYWORD, ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.STATIC_KEYWORD).searchThis());
	}
	
	@Test public void testSearchPublicMethodStringArg() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethodStringArg").withArgumentTypes(String.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(String.class).searchThis());
	}
	
	@Test public void testSearchPublicMethod3StringArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod3StringArgs").withArgumentTypes(String.class, String.class, String.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(String.class, String.class, String.class).searchThis());
	}
	
	@Test public void testSearchPublicMethodMixedArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethodMixedArgs").withArgumentTypes(String.class, Object.class, Integer.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(String.class, Object.class, Integer.class).searchThis());
	}
	
	@Test public void testSearchPublicMethodPrimitiveArg() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethodIntArg").withArgumentTypes(Integer.TYPE).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(Integer.TYPE).searchThis());
	}
	
	@Test public void testSearchPublicMethod3PrimitiveArg() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod3IntArg").withArgumentTypes(Integer.TYPE, Integer.TYPE, Integer.TYPE).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(Integer.TYPE, Integer.TYPE, Integer.TYPE).searchThis());
	}
	
	@Test public void testSearchPublicMethodMixedPrimitiveArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethodMixedPrimitiveArgs").withArgumentTypes(Byte.TYPE, Integer.TYPE, Double.TYPE).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(Byte.TYPE, Integer.TYPE, Double.TYPE).searchThis());
	}
	
	@Test public void testSearchPublicMethodMixedPrimitiveAndObjectArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethodMixedPrimitiveAndObjectArgs").withArgumentTypes(Byte.TYPE, Object.class, Double.TYPE).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(Byte.TYPE, Object.class, Double.TYPE).searchThis());
	}
	
	@Test public void testSearchMethodWithModifiersArgumentsAndReturnType() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("modifiersArgsReturnType").withReturnType(String.class).withArgumentTypes(Double.class, Integer.TYPE).withModifiers(ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.FINAL_KEYWORD).searchThis());
		//order shouldn't matter
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("modifiersArgsReturnType").withArgumentTypes(Double.class, Integer.TYPE).withModifiers(ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.FINAL_KEYWORD).withReturnType(String.class).searchThis());
		//public keyword, but final not specified. Method is not _exactly_ as specified but modifiers are in method so this search return true!
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("modifiersArgsReturnType").withArgumentTypes(Double.class, Integer.TYPE).withModifiers(ModifierKeyword.PUBLIC_KEYWORD).withReturnType(String.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").withArgumentTypes(Double.class).withModifiers(ModifierKeyword.PUBLIC_KEYWORD).withReturnType(String.class).searchThis());
	}
	
	@Test public void testSearch2Methods() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("publicMethod2").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("doesNotExist").searchThis());
	}
	
	@Test public void testSearch4Methods() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("publicMethod2").andIsMethod("privateMethod1").andIsMethod("finalMethod1").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("publicMethod2").andIsMethod("privateMethod1").andIsMethod("doesNotExist").searchThis());
	}
	
	@Test public void testSearch2MethodsReturnTypeModifiersArgs() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("modifiersArgsReturnType").withArgumentTypes(Double.class, Integer.TYPE).withModifiers(ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.FINAL_KEYWORD).withReturnType(String.class).searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isMethod("publicMethod1").andIsMethod("publicMethod1").withArgumentTypes(Double.class, Integer.TYPE).withModifiers(ModifierKeyword.PUBLIC_KEYWORD, ModifierKeyword.FINAL_KEYWORD).withReturnType(String.class).searchThis());
	}
	
	//TODO Test Code suche
	// - synchronized
	// - wait
	// - notify
	// - Logischer Ausdruck
	
	//Anonymous Classes
	
	@Test public void testSearchIsAnonymousClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isAnonymousClass().searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").isAnonymousClass().searchThis());
	}
	
	@Test public void testSearchInAnonymousClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").inAnonymousClass().searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").inAnonymousClass().searchThis());
	}
	
	@Test public void testSearchMethodInAnonymousClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").inAnonymousClass().isMethod("run").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").inAnonymousClass().isMethod("xxx").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").inAnonymousClass().isMethod("run").searchThis());
	}
	
	@Test public void testSearchIsAnonymousClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("*").isAnonymousClass().searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").isAnonymousClass().searchThis());
	}
	
	@Test public void testSearchInAnonymousClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("*").inAnonymousClass().searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").inAnonymousClass().searchThis());
	}
	
	//TODO TEST andIsAnonymousClass
	
	//InnerClasses
	
	@Test public void testSearchIsInnerClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").isInnerClass("InnerClass").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").isInnerClass("DNExist").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").isInnerClass("InnerClass").searchThis());
	}
	
	@Test public void testSearchInInnerClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").inInnerClass("InnerClass").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").inInnerClass("DNExist").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").inInnerClass("InnerClass").searchThis());
	}
	
	@Test public void testSearchMethodInInnerClass() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").inInnerClass("InnerClass").isMethod("innerClassMethod").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").inInnerClass("InnerClass").isMethod("xxx").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").inInnerClass("InnerClass").isMethod("run").searchThis());
	}
	
	@Test public void testSearchMethodInInnerClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("SearchTest").inInnerClass("*").isMethod("innerClassMethod").searchThis());
		assertFalse(newSearchInSearchTest().inClass("SearchTest").inInnerClass("*").isMethod("xxx").searchThis());
		assertFalse(newSearchInSearchTest().inClass("NonPublicClass").inInnerClass("*").isMethod("run").searchThis());
	}
	
	//TODO Wildcard with method
	
	@Test public void testSearchInInnerClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("*").inInnerClass("InnerClass").searchThis());
		assertTrue(newSearchInSearchTest().inClass("*").inInnerClass("*").searchThis());
		assertFalse(newSearchInSearchTest().inClass("*").inInnerClass("DNExist").searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").inInnerClass("InnerClass").searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").inInnerClass("*").searchThis());
	}
	
	@Test public void testSearchIsInnerClassWithWildcard() throws Exception {
		assertTrue(newSearchInSearchTest().inClass("*").isInnerClass("InnerClass").searchThis());
		assertTrue(newSearchInSearchTest().inClass("*").isInnerClass("*").searchThis());
		assertFalse(newSearchInSearchTest().inClass("*").isInnerClass("DNExist").searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").isInnerClass("InnerClass").searchThis());
		assertFalse(newSearch(new File(TEST_DIR, "NoAnonymousAndInnerClasses.java")).inClass("*").isInnerClass("*").searchThis());
	}
	
	//TODO TEST andIsInnerClass
	
	
	//wait und notify in einer Methode Testen
	
	@Test public void testWaitAndNotifyUsed() throws Exception {
		assertTrue(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("waitAndNotifyUsed").withStatement(and(notifyStatement(), waitStatement())).searchThis());
		assertTrue(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("waitAndNotifyUsed").withStatement(and(or(notifyStatement(), notifyAllStatement()), waitStatement())).searchThis());
		
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("waitOnly").withStatement(and(notifyStatement(), waitStatement())).searchThis());
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("waitOnly").withStatement(and(or(notifyStatement(), notifyAllStatement()), waitStatement())).searchThis());
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("notifyOnly").withStatement(and(notifyStatement(), waitStatement())).searchThis());
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("notifyOnly").withStatement(and(notifyAllStatement(), waitStatement())).searchThis());
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("notifyAllOnly").withStatement(and(notifyStatement(), waitStatement())).searchThis());
		assertFalse(newSearchInTestDir("WaitNotifyInOneMethod.java").inClass("WaitNotifyInOneMethod").isMethod("notifyAllOnly").withStatement(and(notifyAllStatement(), waitStatement())).searchThis());
	}
}
