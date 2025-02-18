/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.dialect.function.array;

import java.util.List;

import org.hibernate.metamodel.mapping.JdbcMappingContainer;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.Expression;

/**
 * H2 array_set function.
 */
public class H2ArraySetFunction extends ArraySetUnnestFunction {

	public H2ArraySetFunction() {
	}

	@Override
	public void render(
			SqlAppender sqlAppender,
			List<? extends SqlAstNode> sqlAstArguments,
			SqlAstTranslator<?> walker) {
		final Expression arrayExpression = (Expression) sqlAstArguments.get( 0 );
		final Expression indexExpression = (Expression) sqlAstArguments.get( 1 );
		final Expression elementExpression = (Expression) sqlAstArguments.get( 2 );
		sqlAppender.append( "(select array_agg(case when i.idx=");
		indexExpression.accept( walker );
		sqlAppender.append(" then " );
		elementExpression.accept( walker );
		sqlAppender.append(" when " );
		arrayExpression.accept( walker );
		sqlAppender.append(" is not null and i.idx<=cardinality(");
		arrayExpression.accept( walker );
		sqlAppender.append(") then array_get(");
		arrayExpression.accept( walker );
		sqlAppender.append(",i.idx) end) from system_range(1," );
		sqlAppender.append( Integer.toString( getMaximumArraySize() ) );
		sqlAppender.append( ") i(idx) where i.idx<=greatest(case when ");
		arrayExpression.accept( walker );
		sqlAppender.append(" is not null then cardinality(" );
		arrayExpression.accept( walker );
		sqlAppender.append( ") else 0 end," );
		indexExpression.accept( walker );
		sqlAppender.append( "))" );
	}

	protected int getMaximumArraySize() {
		return 1000;
	}
}
