package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class InnerClassExpression 
extends ClassExpression 
{
	private ClassContainer fClassContainer;
	
	public InnerClassExpression(final String name) {
		super(name);
	}

	@Override
	public boolean visit(final TypeDeclaration node) {
		if(classNameMatches(node) && node != fClassContainer.getASTNode()) {
			setMatch(true);
			evaluateMethodExpression(new ClassDeclaration(node));
			return false; //found inner class
		}
		return true; //look deeper
	}

	@Override
	public boolean eval(final ClassContainer node) 
	{
		this.fClassContainer = node;
		node.getASTNode().accept(this);
		return hasMatch();
	}
	
}
