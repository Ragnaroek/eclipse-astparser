package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.Block;


public class ExpressionFactory 
{
	public static MethodExpression method(final String name)
	{
		return new MethodExpression(name);
	}
	
	public static Wait waitStatement() 
	{
		return new Wait();
	}
	
	public static Notify notifyStatement()
	{
		return new Notify();
	}
	
	public static NotifyAll notifyAllStatement()
	{
		return new NotifyAll();
	}
	
	public static Synchronized synchronizedStatement()
	{
		return new Synchronized();
	}
	
	public static MethodExpression not(final MethodExpression expr)
	{
		return new MethodExpression() {
			@Override
			public boolean eval(final ClassDeclaration subSearch) {
				return !expr.eval(subSearch);
			}
		};
	}
	
	public static IStatement not(final IStatement expr)
	{		
		return new IStatement() {
			public final boolean eval(final Block block) {
				return !expr.eval(block);
			}
		};
	}
	
	public static IStatement or(final IStatement... statements)
	{
		return new IStatement() {
			public boolean eval(final Block block) {
				for(IEvaluable<Block> stmt : statements) {
					if(stmt.eval(block)) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	public static IStatement and(final IStatement...statements)
	{
		return new IStatement() {
			public boolean eval(final Block block) {
				for(IEvaluable<Block> stmt : statements) {
					if(!stmt.eval(block)) {
						return false;
					}
				}
				return true;
			}
		};
	}
}
