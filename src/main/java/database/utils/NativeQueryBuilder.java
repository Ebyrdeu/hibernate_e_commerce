package database.utils;

public class NativeQueryBuilder {

    private final StringBuilder query;
    private final String table;


    public NativeQueryBuilder(String table) {
        this.table = table;
        this.query = new StringBuilder();
    }

    public NativeQueryBuilder function(String function) {
        query.append(function.toUpperCase());
        query.append("()");
        return this;
    }

    public NativeQueryBuilder select(String... columns) {
        query.append("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) query.append(", ");
        }
        return this;
    }

    public NativeQueryBuilder from() {
        query.append("FROM ").append(table);
        return this;
    }

    public NativeQueryBuilder selectFrom() {
        query.append("SELECT * ").append("FROM ").append(table);
        return this;
    }

    public <T> NativeQueryBuilder where(String column, T value) {
        query.append(" WHERE ").append(column).append(" = ").append(value);

        return this;
    }

    public NativeQueryBuilder where(String column) {
        query.append(" WHERE ").append(column).append(" = ?");

        return this;
    }

    public NativeQueryBuilder insert(String... columns) {
        query.append("INSERT INTO ").append(table);
        query.append(" (");

        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) query.append(", ");
        }
        query.append(") ");
        query.append("VALUES");
        query.append("(");

        for (int i = 0; i < columns.length; i++) {
            query.append("?");
            if (i < columns.length - 1) query.append(", ");
        }

        query.append(")");
        return this;
    }

    public NativeQueryBuilder update(String... columns) {
        query.append("UPDATE ").append(table).append(" SET ");

        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) query.append(", ");
        }

        return this;
    }

    public NativeQueryBuilder delete(String column) {
        query.append("DELETE FROM ").append(column);
        return this;
    }

    public NativeQueryBuilder count(String column, boolean distinct) {
        query.append("SELECT ").append("COUNT(");
        if (distinct) query.append("DISTINCT ");
        query.append(column).append(") ");
        query.append("FROM ").append(table);
        return this;
    }

    public NativeQueryBuilder join(String join, String on) {
        query.append("SELECT * FROM ").append(table);
        query.append(" LEFT JOIN ").append(join);
        query.append(" ON ").append(table).append(".").append(on).append(" = ").append(join).append(".").append(on);
        return this;
    }

    public String execute() {

        return query.toString();
    }
}
