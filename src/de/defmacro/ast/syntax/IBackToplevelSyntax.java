package de.defmacro.ast.syntax;

public interface IBackToplevelSyntax
extends ISearchSyntax
{
	public IClassSyntax andInClass(String clazz);
	public IBackToplevelSyntax andIsClass(String clazz);
}
