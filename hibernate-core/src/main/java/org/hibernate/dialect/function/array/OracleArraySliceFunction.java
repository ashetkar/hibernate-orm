/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.dialect.function.array;

import java.util.List;

import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.Expression;

/**
 * Oracle array_slice function.
 */
public class OracleArraySliceFunction extends ArraySliceUnnestFunction {

	public OracleArraySliceFunction() {
		super( false );
	}

	@Override
	public void render(
			SqlAppender sqlAppender,
			List<? extends SqlAstNode> sqlAstArguments,
			SqlAstTranslator<?> walker) {
		final String arrayTypeName = DdlTypeHelper.getTypeName(
				( (Expression) sqlAstArguments.get( 0 ) ).getExpressionType(),
				walker
		);
		sqlAppender.append( arrayTypeName );
		sqlAppender.append( "_slice(" );
		sqlAstArguments.get( 0 ).accept( walker );
		sqlAppender.append( ',' );
		sqlAstArguments.get( 1 ).accept( walker );
		sqlAppender.append( ',' );
		sqlAstArguments.get( 2 ).accept( walker );
		sqlAppender.append( ')' );
	}
}
