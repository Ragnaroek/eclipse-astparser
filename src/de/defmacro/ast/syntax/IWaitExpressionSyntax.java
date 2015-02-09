package de.defmacro.ast.syntax;

public interface IWaitExpressionSyntax 
extends ICommonSyntax
{
	public IWaitExpressionSyntax withTimeout(long timeout);
	public IWaitExpressionSyntax withAnyTimeout();
	public IWaitExpressionSyntax withoutTimeout();
}
