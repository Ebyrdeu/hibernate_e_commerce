package database.utils;

public class JPQueryBuilder {

    private final StringBuilder query;
    private final String alias;

    public JPQueryBuilder(String table) {
        this.alias = table;
        this.query = new StringBuilder();
    }

    private static String removeBeforeFirstCapital(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replaceFirst("^[^A-Z]*", "");
    }

    private char reduceToOneChar() {
        return alias.toLowerCase().charAt(0);
    }

    private char reduceToOneChar(String string) {
        return string.toLowerCase().charAt(0);
    }


    private void checkOnWhereConstraint(String jpaColumn, String parameter) {
        if (query.toString().contains("WHERE")) query.append(" AND ");
        else query.append(" WHERE ");

        query.append(reduceToOneChar());
        query.append('.');
        query.append(jpaColumn);
        query.append(" ");
        query.append(parameter);
        query.append(" :");
        query.append(jpaColumn);
    }

    public JPQueryBuilder select(String... jpaColumn) {
        query.append("SELECT ");
        query.append("(");
        for (int i = 0; i < jpaColumn.length; i++) {
            query.append(reduceToOneChar()).append('.');
            query.append(jpaColumn[i]);
            if (i < jpaColumn.length - 1) query.append(", ");
        }
        query.append(")");
        return this;
    }

    public JPQueryBuilder from() {
        query.append(" FROM ");
        query.append(alias).append(" ");
        query.append(reduceToOneChar());
        return this;
    }

    public JPQueryBuilder selectFrom() {
        query.append("SELECT ");
        query.append(reduceToOneChar());
        query.append(" FROM ");
        query.append(alias).append(" ");
        query.append(reduceToOneChar());
        return this;
    }

    public JPQueryBuilder where(String... jpaColumn) {
        query.append(" WHERE ");
        for (int i = 0; i < jpaColumn.length; i++) {
            if (query.toString().contains("JOIN")) query.append(reduceToOneChar(jpaColumn[i]));
            else query.append(reduceToOneChar());
            query.append('.');
            if (query.toString().contains("JOIN")) query.append(removeBeforeFirstCapital(jpaColumn[i]).toLowerCase());
            else query.append(jpaColumn[i]);
            query.append(" = :");
            query.append(jpaColumn[i]);
            if (i < jpaColumn.length - 1) query.append(", ");
        }
        return this;
    }

    public JPQueryBuilder moreThan(String jpaColumn) {
        checkOnWhereConstraint(jpaColumn, ">");
        return this;
    }

    public JPQueryBuilder lessThan(String jpaColumn) {
        checkOnWhereConstraint(jpaColumn, "<");
        return this;
    }

    public JPQueryBuilder and(String jpaColumn) {
        query.append(" AND ");
        query.append(reduceToOneChar());
        query.append('.');
        query.append(jpaColumn);
        query.append(" = ");
        query.append(jpaColumn);
        return this;
    }

    public JPQueryBuilder join(String jpaColumn) {
        query.append(" JOIN ");
        query.append(reduceToOneChar());
        query.append(".");
        query.append(jpaColumn);
        query.append(" ");
        query.append(reduceToOneChar(jpaColumn));
        return this;
    }

    public String execute() {
        return query.toString();
    }
}
