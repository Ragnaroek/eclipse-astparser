package de.defmacro.ast.search;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class ClassExpression
extends ResultVisitor
implements IEvaluable<ClassContainer>
{
	private final String fName;
	private final List<MethodExpression> fMethods;
	private final List<AnonymousClassExpression> fAnonymousClasses;
	private final List<InnerClassExpression> fInnerClasses;
	
	public ClassExpression(final String name)
	{
		if (name == null) {
			throw new NullPointerException("name must not be null");
		}
		
		this.fName = name.trim();
		this.fMethods = new ArrayList<MethodExpression>();
		this.fAnonymousClasses = new ArrayList<AnonymousClassExpression>();
		this.fInnerClasses = new ArrayList<InnerClassExpression>();
	}
	
	public void addMethodSearch(final MethodExpression search)
	{
		fMethods.add(search);
	}
	
	public void addAnonymousClassSearch(final AnonymousClassExpression exp) {
		fAnonymousClasses.add(exp);
	}
	
	public void addInnerClassSearch(final InnerClassExpression exp) {
		fInnerClasses.add(exp);
	}

	public boolean eval(final ClassContainer node) 
	{
		node.getASTNode().accept(this);
		return hasMatch();
	}

	/**
	 * Visiting TypeDeclaration in __CompilationUnit__
	 */
	@Override
	public boolean visit(final TypeDeclaration node) 
	{	
		if(classNameMatches(node)) {
			setMatch(true);
			
			//Note: it cannot be searched for method-deklarations and anonymous classes
			//      this is enforced by the syntax
			evaluateMethodExpression(new ClassDeclaration(node));
			
			ClassContainer classContainer = new ClassContainer(node);
			
			for(final AnonymousClassExpression anony : fAnonymousClasses) {
				if(!anony.eval(classContainer)) {
					setMatch(false);
					return true;
				}
			}
			
			for(final InnerClassExpression inner : fInnerClasses) {
				if(!inner.eval(classContainer)) {
					setMatch(false);
					return true;
				}
			}
			
			return true; //weiter suchen (evtl. geschachtelte Typ-Deklarationen)
		}
		return true;
	}
	
	protected boolean classNameMatches(TypeDeclaration node) {
		return fName.equals("*") || node.getName().getFullyQualifiedName().equals(fName);
	}
	
	protected void evaluateMethodExpression(final ClassDeclaration node) 
	{
		for(final MethodExpression search : fMethods) {
			if( !search.eval(node) ) {
				setMatch(false); //invalidate match, because at least on MethodExpression is not true (and logic)
			}
		}
	}
}
