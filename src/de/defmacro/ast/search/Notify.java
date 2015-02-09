package de.defmacro.ast.search;

public class Notify 
extends AbstractStatement 
{
	@Override
	protected ResultVisitor createResultVisitor() {
		return new MethodInvocationResultVisitor("notify");
	}
}
