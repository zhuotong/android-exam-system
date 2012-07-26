package com.msxt.position;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.Examination;
import com.msxt.model.Examination_;
import com.msxt.model.Position;
import com.msxt.model.Position_;

@Stateful
@ConversationScoped
@Named
public class PositionAgent {
	@PersistenceContext
    private EntityManager em;
	
	@Inject
	PositionSearch positionSearch;
	@Inject
	private Conversation conversation;
	    
	@Inject
	private Messages messages;
	
	private Position selectedPosition;
	
    @Begin
    public void selectPosition(final String id) {
    	conversation.setTimeout(600000); //10 * 60 * 1000 (10 minutes)
    	selectedPosition = em.find(Position.class, id);
    }
    
    public void deletePosition(final String id) {
    	Position p = em.find( Position.class, id);
    	Position previousP = getPreviousPosition(p);
    	
    	if( previousP==null ) {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<Examination> cq = cb.createQuery( Examination.class );
    		Root<Examination> rp = cq.from( Examination.class );
    		cq.select( rp ).where( cb.equal( rp.get( Examination_.position), p ) );
    		
    		List<Examination> ees = em.createQuery( cq ).getResultList();
    		if( ees.size()>0 ) {
    			messages.error( new DefaultBundleKey("msxt_position_delete_failure2") )
    			        .params( p.getName(), ees.get(0).getName() );
    		} else {
	    		em.remove( p );
	    		messages.info( new DefaultBundleKey("msxt_position_delete_success") )
		        		.params( p.getName() );
    		}
    	} else {
    		messages.error( new DefaultBundleKey("msxt_position_delete_failure1") )
                    .params( p.getName(), previousP.getName() );
    	}
    	
    }
    
    @Produces
    @RequestScoped
    @Named
    public Position getSelectedPosition() {
        return selectedPosition;
    }
    
    private Position getPreviousPosition( Position p ) {
   	 	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Position> cquery = builder.createQuery(Position.class);
        Root<Position> rp = cquery.from(Position.class);

        cquery.select( rp ).where( builder.equal( rp.get(Position_.nextPosition), p) );
        List<Position> results = em.createQuery(cquery).getResultList();
	   	if( results!=null && results.size()>0 )
	   		return results.get(0);
	   	else
	   		return null;
   }
}
