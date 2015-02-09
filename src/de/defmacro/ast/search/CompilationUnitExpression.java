package de.defmacro.ast.search;

import java.util.List;


public class CompilationUnitExpression 
extends ResultVisitor
implements IEvaluable<ClassContainer>
{
	private List<ClassExpression> fClassSearches;
	
	public CompilationUnitExpression(final List<ClassExpression> classSearches)
	{
		if (classSearches == null) {
			throw new NullPointerException("classSearch must not be null");
		}
		
		this.fClassSearches = classSearches;
	}
	
	public boolean eval(final ClassContainer thing) 
	{		
		for(ClassExpression search : fClassSearches) {
			if( !search.eval(thing) ) {
				return false;
			}
		}
		return true;
	}	
}
