package de.defmacro.ast.syntax;

import de.defmacro.ast.search.MethodExpression;


public interface IClassSyntax
extends ISearchSyntax
{	
	//TODO is muesste eine ClassExpression erwarten, die ueber FActory
	//kombiniert werden kann!
	public INextMethodSyntax is(MethodExpression expression);
	
	
	/**
	 * Zur Vereinfachung == isMethod(method("name"))
	 * @param methodName
	 * @return
	 */
	public INextMethodSyntax isMethod(String methodName);
	
	public IClassSyntax inInnerClass(String name);
	public IClassSyntax inAnonymousClass();
	
	public IBackToClassSyntax isInnerClass(String name);
	public IBackToClassSyntax isAnonymousClass();
}
