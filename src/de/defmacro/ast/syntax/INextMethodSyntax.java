package de.defmacro.ast.syntax;

import de.defmacro.ast.search.MethodExpression;

public interface INextMethodSyntax
extends IMethodSyntax
{
	public INextMethodSyntax andIsMethod(String methodName);
	public INextMethodSyntax andIs(MethodExpression expr);
}
