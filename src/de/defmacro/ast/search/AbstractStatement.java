package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.Block;
import de.defmacro.ast.syntax.ICommonSyntax;

public abstract class AbstractStatement
implements ICommonSyntax, IStatement
{
	/*
	protected int fLine;

	public ICommonSyntax onLine(final int line) {
		this.fLine = line;
		return this;
	}*/

	/**
	 * Teste zuerst CommonSyntax.
	 */
	public boolean eval(final Block block) 
	{
		boolean evalOk = false;
		
		ResultVisitor visitor = createResultVisitor();
		block.accept(visitor);
		evalOk = visitor.hasMatch();
		
		if( evalOk ) {
			//
			// TODO hier CommonSyntax testen, wenn implementiert wird
			//
			evalOk = true;
		}
		
		return evalOk;
	}
	
	/**
	 * Liefert einen Visitor zurueck, der das jeweils auszuwertende Element
	 * sucht. Der Visitor muss gefundene Statement in der {@link VisitorEvaluation#getMatchedNode()}
	 * Methode zurueckliefern (falls gefunden)
	 * @return
	 */
	protected abstract ResultVisitor createResultVisitor();
}
