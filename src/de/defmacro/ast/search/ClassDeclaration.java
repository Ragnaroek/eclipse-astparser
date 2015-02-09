package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassDeclaration
implements ITypesafeASTNode
{
	private final ASTNode fNode;
	
	public ClassDeclaration(final TypeDeclaration type) {
		this.fNode = type;
	}
	
	public ClassDeclaration(final AnonymousClassDeclaration anon) {
		this.fNode = anon;
	}

	public ASTNode getASTNode() {
		return fNode;
	}
}
