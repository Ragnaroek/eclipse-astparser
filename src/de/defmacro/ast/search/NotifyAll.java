package de.defmacro.ast.search;

public class NotifyAll
extends AbstractStatement
{
	@Override
	protected ResultVisitor createResultVisitor() {
		return new MethodInvocationResultVisitor("notifyAll");
	}	
}
