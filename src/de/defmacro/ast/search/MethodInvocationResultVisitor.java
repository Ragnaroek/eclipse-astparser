package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.MethodInvocation;

/*package-private*/ class MethodInvocationResultVisitor 
extends ResultVisitor 
{
	private final String fMethodName;
	
	public MethodInvocationResultVisitor(final String methodName)
	{
		if (methodName == null) {
			throw new NullPointerException("fMethodName must not be null");
		}

		this.fMethodName = methodName;
	}

	@Override
	public boolean visit(final MethodInvocation invocation) 
	{
		String name = invocation.getName().getIdentifier();
		if( name.equals(fMethodName) ) {
			setMatch(true);
			setMatchedNode(invocation);
			visitMethodInvocation(invocation);
		}
		return true;
	}
	
	/**
	 * Das setMatch(...) Flag ist schon gesetzt und die Node gesetzt.
	 * Die Standardimplementierung macht nichts.
	 * @param node
	 */
	public void visitMethodInvocation(final MethodInvocation node) {
		//no-op
	}
}
