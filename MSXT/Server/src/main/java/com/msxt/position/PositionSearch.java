/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msxt.position;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.solder.logging.Logger;
import org.jboss.seam.international.status.builder.TemplateMessage;

import com.msxt.common.FoggySearchCriteria;
import com.msxt.common.PageableSearcher;
import com.msxt.model.Position;
import com.msxt.model.Position_;

@Named
@Stateful
@SessionScoped
public class PositionSearch extends PageableSearcher{

    @Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    @Named("foggySearchCriteria")
    private FoggySearchCriteria criteria;

    @Inject
    private Instance<TemplateMessage> messageBuilder;

    private List<Position> positions = new ArrayList<Position>();
    
    @Produces
    @Named
    public List<Position> getPositions() {
        return positions;
    }
    
    public void doSearch() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Position> cquery = builder.createQuery(Position.class);
        Root<Position> rp = cquery.from(Position.class);

        cquery.select( rp ).where( builder.like(builder.lower( rp.get(Position_.name) ), criteria.getSearchPattern()) );

        List<Position> results = em.createQuery(cquery).setMaxResults(criteria.getFetchSize())
                .setFirstResult(criteria.getFetchOffset()).getResultList();

        nextPageAvailable = results.size() > criteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	positions = new ArrayList<Position>( results.subList(0,  criteria.getPageSize() ) );
        } else {
        	positions = results;
        }
        log.info(messageBuilder.get().text("Found {0} hotel(s) matching search term [ {1} ] (limit {2})")
                .textParams(positions.size(), criteria.getQuery(), criteria.getPageSize()).build().getText());
    }

	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return criteria;
	}
}
