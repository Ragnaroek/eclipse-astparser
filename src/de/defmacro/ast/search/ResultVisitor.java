package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

/*package-private*/ class ResultVisitor
extends ASTVisitor
{
	private boolean fHasMatched = false;
	private ASTNode fMatchedNode = null;
	
	/**
 	* @param match - der uebergebene Wert
 	* @return
 	*/
	protected boolean setMatch(final boolean match)
	{
		fHasMatched = match;
		return match;
	}
	
	protected boolean andMatch(final boolean match) {
		return setMatch(fHasMatched && match);
	}
	
	protected boolean hasMatch()
	{
		return fHasMatched;
	}
	
	/**
	 * Liefert den gefundenen Knoten im AST oder <code>null</code>.
	 * Liefert immer <code>null</code>, wenn {@link VisitorEvaluation#hasMatch()}
	 * falsch liefert.
	 * @return
	 */
	protected ASTNode getMatchedNode()
	{
		return fMatchedNode;
	}
	
	protected void setMatchedNode(final ASTNode node)
	{
		this.fMatchedNode = node;
	}
}
