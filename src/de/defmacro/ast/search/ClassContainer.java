package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassContainer 
implements ITypesafeASTNode
{
	private ASTNode fNode;
	
	public ClassContainer(final CompilationUnit unit) {
		this.fNode = unit;
	}
	
	public ClassContainer(final TypeDeclaration t) {
		this.fNode = t;
	}
	
	public ASTNode getASTNode() {
		return fNode;
	}
}
