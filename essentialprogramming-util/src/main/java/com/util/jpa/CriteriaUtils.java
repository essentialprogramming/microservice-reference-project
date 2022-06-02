package com.util.jpa;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class CriteriaUtils {

    public <T> void addWhereInClause(
            final Root<T> root,
            final AbstractQuery<?> query,
            final CriteriaBuilder criteriaBuilder,
            final String conditionColumnName,
            final Serializable... conditionColumnValues
    ) {

        final Path<Object> path = root.get(conditionColumnName);
        final CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
        for (Serializable conditionColumnValue : conditionColumnValues) {
            in.value(conditionColumnValue);
        }
        query.where(in);
    }
}
