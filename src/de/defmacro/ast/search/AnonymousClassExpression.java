package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AnonymousClassExpression
extends ClassExpression
{	
	public AnonymousClassExpression() {
		super("");
	}
	
	/*
	@Override
	public boolean eval(final ClassContainer node) {
		node.getASTNode().accept(this);
		return hasMatch();
	}
	*/
	/*
	public boolean eval(final TypeDeclaration subSearch) {
		subSearch.accept(this);
		return hasMatch();
	}*/
	
	@Override
	public boolean visit(final AnonymousClassDeclaration node) {
		setMatch(true); //found anonymous class inside TypeDeclaration
		evaluateMethodExpression(new ClassDeclaration(node));
		return false; //anonymous class found, don't look further
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		return true;	
	}
}
