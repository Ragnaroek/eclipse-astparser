package de.defmacro.ast.syntax;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

public interface IMethodSyntax
extends ISearchSyntax, IExpressionSyntax
{		
	public INextMethodSyntax withoutArguments();
	
	/**
	 * Zur Zeit nur Suche nach einfachen Parametern (Klassentypen ohne Generics, Primitive Typen [Boolean.TYPE,...]) moeglich!
	 * Moegliche Erweiterung: withArgumentTypes(ComplexType... args);
	 * @param types
	 * @return
	 */
	public INextMethodSyntax withArgumentTypes(Class<?>... types);
	
	/**
	 * Als Integer-Werte werden die Werte aus der Klasse {@link ModifierKeyword} erwartet.
	 * @param modifiers
	 * @return
	 */
	public INextMethodSyntax withModifiers(ModifierKeyword... modifiers);
	
	/**
	 * Starting a search without setting a return type explicitly, returns <code>true</code> if the other
	 * parameters for the method are matching. Regardless of the return type. (That means _not_ setting
	 * the return type does _not_ means that the return type must be <code>void</code>.
	 * @param returnType
	 * @return
	 */
	public INextMethodSyntax withReturnType(Class<?> returnType);
}
