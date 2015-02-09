package de.defmacro.ast.syntax;

public interface ICompilationUnitSyntax 
{
	public IBackToplevelSyntax isInPackage(String pack);
	public IClassSyntax inClass(String clazz);
	public IBackToplevelSyntax isClass(String clazz);
}
