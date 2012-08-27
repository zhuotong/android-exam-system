package com.msxt.common;

import java.io.Serializable;

import javax.faces.bean.NoneScoped;
import javax.inject.Named;

@Named
@NoneScoped
public class FoggySearchCriteria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final char SQL_WILDCARD_CHAR = '%';
    private static final String SQL_WILDCARD_STR = String.valueOf(SQL_WILDCARD_CHAR);
    private static final String REPEAT_SQL_WIDCARD_REGEX = SQL_WILDCARD_STR + "+";
    private static final char HUMAN_WILDCARD_CHAR = '*';

    private String query = "";
    private int pageSize = 15;
    private int page = 0;

    public String getSearchPattern() {
        if (query == null || query.length() == 0) {
            return SQL_WILDCARD_STR;
        }

        StringBuilder pattern = new StringBuilder();
        pattern.append( query.toLowerCase().replace(HUMAN_WILDCARD_CHAR, SQL_WILDCARD_CHAR)
                                           .replaceAll(REPEAT_SQL_WIDCARD_REGEX, SQL_WILDCARD_STR) );
        if (pattern.length() == 0 || pattern.charAt(0) != SQL_WILDCARD_CHAR) {
            pattern.insert(0, SQL_WILDCARD_CHAR);
        }
        if (pattern.length() > 1 && pattern.charAt(pattern.length() - 1) != SQL_WILDCARD_CHAR) {
            pattern.append(SQL_WILDCARD_CHAR);
        }
        return pattern.toString();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFetchSize() {
        return pageSize + 1;
    }

    public int getFetchOffset() {
        return page * pageSize;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = (query != null ? query.trim() : null);
    }

    public void nextPage() {
        page++;
    }

    public void previousPage() {
        if (page > 0) {
            page--;
        }
    }

    public void firstPage() {
        page = 0;
    }
}
