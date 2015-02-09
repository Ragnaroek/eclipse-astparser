package de.defmacro.ast.syntax;

import de.defmacro.ast.search.IStatement;


public interface IExpressionSyntax
extends ISearchSyntax
{
	public INextMethodSyntax withStatement(IStatement expr);
	//public INextMethodSyntax returns(ReturnStatement expr);
}
