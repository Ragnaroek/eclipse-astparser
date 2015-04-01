package de.defmacro.ast.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import de.defmacro.ast.JavaParser;
import de.defmacro.ast.syntax.IBackToClassSyntax;
import de.defmacro.ast.syntax.IBackToplevelSyntax;
import de.defmacro.ast.syntax.IClassSyntax;
import de.defmacro.ast.syntax.ICompilationUnitSyntax;
import de.defmacro.ast.syntax.IExpressionSyntax;
import de.defmacro.ast.syntax.INextMethodSyntax;
import de.defmacro.ast.syntax.ISearchSyntax;
import de.defmacro.ast.syntax.SyntaxException;

import static de.defmacro.ast.search.ExpressionFactory.*;

//TODO is(...) - Suche auch fuer anonyme und innere Klassen
//TODO is(...) - Statement-Verknüpfung mit or, not, and Operator

public class SearchIn
implements ICompilationUnitSyntax, ISearchSyntax
{	
	private static class Delegate
	implements ISearchSyntax, ICompilationUnitSyntax, IClassSyntax, 
    INextMethodSyntax, IBackToplevelSyntax, IBackToClassSyntax, IExpressionSyntax
	{
		private final List<String> fClasses;
		private final List<ClassExpression> fClassSearchs;
		private final CompilationUnit fUnit;
		private ClassExpression fCurrentClass;
		private MethodExpression fCurrentMethod;
		
		public Delegate(final CompilationUnit unit)
		{
			fClassSearchs = new ArrayList<ClassExpression>();
			fClasses = new ArrayList<String>();
			fUnit = unit;
		}
		
		public INextMethodSyntax isMethod(final String methodName) 
		{
			MethodExpression search = new MethodExpression(methodName);
			is(search);
			return this;
		}

		public INextMethodSyntax is(MethodExpression expression) {
			fCurrentClass.addMethodSearch(expression);
			fCurrentMethod = expression;
			return this;
		}

		public INextMethodSyntax andIsMethod(final String methodName) {
			isMethod(methodName);
			return this;
		}
		
		public INextMethodSyntax andIs(final MethodExpression expr) {
			is(expr);
			return this;
		}

		public INextMethodSyntax withReturnType(final Class<?> returnType) {
		    fCurrentMethod.setReturnType(returnType);
			return this;
		}
		
		public INextMethodSyntax withArgumentTypes(final Class<?>... types) {
			fCurrentMethod.setArguments(types);
			return this;
		}
		
		public INextMethodSyntax withModifiers(final ModifierKeyword... modifiers) {
			fCurrentMethod.addModifier(modifiers);
			return this;
		}

		public INextMethodSyntax withStatement(final IStatement expr) {
			fCurrentMethod.addExpression(expr);
			return this;
		}
		
		public INextMethodSyntax withoutArguments() {
			//no-op, sugar method
			return this;
		}

		public boolean searchThis() {
			return new CompilationUnitExpression(fClassSearchs).eval(new ClassContainer(fUnit));
		}

		public IClassSyntax inClass(final String clazz) 
		{
			return addClass(clazz);
		}

		public IBackToplevelSyntax isClass(final String clazz) 
		{
			return addClass(clazz);
		}

		public IBackToplevelSyntax isInPackage(String pack) {
			throw new UnsupportedOperationException();
		}
		
		private Delegate addClass(final String clazz)
		{
			checkClass(clazz);
			fCurrentClass = new ClassExpression(clazz);
			fClassSearchs.add(fCurrentClass);
			fClasses.add(clazz);
			return this;
		}
		
		private void checkClass(final String clazz)
		{
			if( fClasses.contains(clazz) ) {
				throw new SyntaxException("You cannot search twice for class = '" + clazz + "'");
			}
		}

		public IBackToClassSyntax isAnonymousClass() {
			fCurrentClass.addAnonymousClassSearch(new AnonymousClassExpression());
			return this;
		}
		
		public IClassSyntax inAnonymousClass() {
			AnonymousClassExpression exp = new AnonymousClassExpression();
			fCurrentClass.addAnonymousClassSearch(exp);
			fCurrentClass = exp;
			return this;
		}

		public IBackToClassSyntax isInnerClass(final String name) {
			fCurrentClass.addInnerClassSearch(new InnerClassExpression(name));
			return this;
		}
		
		public IClassSyntax inInnerClass(String name) {
			InnerClassExpression exp = new InnerClassExpression(name);
			fCurrentClass.addInnerClassSearch(exp);
			fCurrentClass = exp;
			return this;
		}

		public IClassSyntax andInClass(final String clazz) {
			return addClass(clazz);
		}

		public IBackToplevelSyntax andIsClass(final String clazz) {
			return addClass(clazz);
		}
		
		public IClassSyntax andInAnonymousClass() {
			return inAnonymousClass();
		}
		
		public IBackToClassSyntax andIsAnonymousClass() {
			return isAnonymousClass();
		}

		public IClassSyntax andInInnerClass(String name) {
			return inInnerClass(name);
		}

		public IBackToClassSyntax andIsInnerClass(String name) {
			return isInnerClass(name);
		}
	}
	
	private Delegate fDelegate;
	
	public SearchIn(final CompilationUnit unit)
	{
		if (unit == null) {
			throw new NullPointerException("unit must not be null");
		}
		
		fDelegate = new Delegate(unit);
	}
	
	public IClassSyntax inClass(final String clazz) {
		fDelegate.inClass(clazz);
		return fDelegate;
	}

	public IBackToplevelSyntax isClass(final String clazz) {
		fDelegate.isClass(clazz);
		return fDelegate;
	}

	public IBackToplevelSyntax isInPackage(final String pack) {
		fDelegate.isInPackage(pack);
		return fDelegate;
	}
	
	public boolean searchThis() {
		return fDelegate.searchThis();
	}
	
	public static void main(String[] args) throws Exception {
		new SearchIn(new JavaParser().parseCompilationUnit(new File("./...")))
		.inClass("Bla")
		.isMethod("blubb").withArgumentTypes(String.class, Integer.class)
		.withModifiers(ModifierKeyword.PUBLIC_KEYWORD)
		.withStatement(and(waitStatement(), notifyStatement()))
		.searchThis();
		
		new SearchIn(new JavaParser().parseCompilationUnit(new File("./...")))
		.inClass("")
		.isAnonymousClass()
		.andIsAnonymousClass()
		.searchThis();
		
		new SearchIn(null)
		.inClass("*")
		.isAnonymousClass();
		
		new SearchIn(null)
		.inClass("*")
		.inInnerClass("*");
		
	}
}
