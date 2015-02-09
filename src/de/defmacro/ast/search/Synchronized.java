package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.SynchronizedStatement;

public class Synchronized 
extends AbstractStatement 
{
	@Override
	protected ResultVisitor createResultVisitor() 
	{
		return new ResultVisitor() {
			@Override
			public boolean visit(final SynchronizedStatement node) {
				setMatch(true); //synchronized statement gefunden
				setMatchedNode(node);
				return true;
			}		
		};
	}
}
