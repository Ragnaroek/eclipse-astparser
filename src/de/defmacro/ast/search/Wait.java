package de.defmacro.ast.search;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;

import de.defmacro.ast.syntax.IWaitExpressionSyntax;
import de.defmacro.ast.syntax.SyntaxException;

public class Wait
extends AbstractStatement
implements IWaitExpressionSyntax
{
	private long fTimeout = -1;
	private boolean fAnyTimout = false; 
	
	/**
	 * Erweitert nur den Rueckgabetyp von ICommonSyntax auf WaitStatement.
	 */
	/*
	@Override
	public WaitStatement onLine(final int line) {
		super.onLine(line); 
		return this;
	}*/

	/**
	 * Es muss genau das uebergeben timeout (als Literal im Quelltext) im wait-Ausdruck uebergeben werden.
	 */
	public Wait withTimeout(final long timeout) 
	{
		if(timeout < 0) {
			throw new SyntaxException("timeout must be > 0");
		}
		
		this.fTimeout = timeout;
		this.fAnyTimout = false;
		return this;
	}

	public Wait withoutTimeout() {
		this.fTimeout = -1;
		this.fAnyTimout = false;
		return this;
	}
	
	/**
	 * AnyTimeout bedeutet das irgendein Timeout angegeben sein muss. Auch Methodenaufruf oder <code>new Long(1)</code>.
	 */
	public Wait withAnyTimeout() {
		this.fTimeout = Long.MAX_VALUE;
		this.fAnyTimout = true;
		return this;
	}

	@Override
	protected ResultVisitor createResultVisitor() 
	{
		return new MethodInvocationResultVisitor("wait") 
		{
			@Override
			public void visitMethodInvocation(final MethodInvocation node) {
				if( !checkWaitTimout(node) ) {
					setMatch(false); //passt doch nicht, zuruecksetzen
					setMatchedNode(null);
				}
			}

			private boolean checkWaitTimout(final MethodInvocation invocation) 
			{
				List<?> arguments = invocation.arguments();
				
				if(fTimeout < 0) {
					return arguments.size() == 0;
				} else { //Timeout angegeben
					if(arguments.size() == 1) { //genau 1 Argument fuer wait
						Object o = arguments.get(0);
						if(o instanceof NumberLiteral) {
							NumberLiteral literal = (NumberLiteral)o;
							try {
								long time = Long.valueOf(literal.getToken());
								if(!fAnyTimout) { //nicht irgendein timeout
									return time == fTimeout; //nur true wenn genau das timeout angegeben wurde
								} else { //irgendein timeout muss angegeben sein
									//wenn Ausfuehrung hierhin kommt wurde ein long als Timeout angegeben
									return true; 
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
								return false;
							}	
						} else { //kein NumberLiteral angegeben, z.B. Methodenaufruf oder new Long(1)...
							if(fAnyTimout) { //irgendein timeout ist erfuellt
								return true; //deshalb true zurueckgeben
							} else { //ein genaues NumberLiteral Timeout jedoch nicht
								return false; //deshalb false
							}
						}
					}
					return false;
				}
			}
		};
	}
}
