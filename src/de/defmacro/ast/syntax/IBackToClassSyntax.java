package de.defmacro.ast.syntax;

public interface IBackToClassSyntax 
extends ISearchSyntax
{
	public INextMethodSyntax andIsMethod(String methodName);
	
	public IClassSyntax andInInnerClass(String name);
	public IClassSyntax andInAnonymousClass();
	
	public IBackToClassSyntax andIsInnerClass(String name);
	public IBackToClassSyntax andIsAnonymousClass();
}
