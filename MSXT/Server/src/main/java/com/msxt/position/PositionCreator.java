package com.msxt.position;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.component.UIInput;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.Position;
import com.msxt.model.Position_;

@Stateful
@Model
public class PositionCreator {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Messages messages;

    private UIInput positionNameInput;

    private final Position newPosition = new Position();
    
    private String nextPostionId;
    
    public void create() {
        if (verifyPositonNameIsAvailable()) {
        	if( nextPostionId != null ) {
        		Position nextPosition = em.find( Position.class, nextPostionId);        	
        		newPosition.setNextPosition( nextPosition );
        	}
        	
            em.persist( newPosition );

            messages.info(new DefaultBundleKey("registration_registered"))
                    .defaults("You have been successfully create as the position {0}!")
                    .params(newPosition.getName());
        } 
    }


    @Produces
    @Named
    public Position getNewPosition() {
        return newPosition;
    }

    public UIInput getPositionNameInput() {
        return positionNameInput;
    }

    public void setPositionNameInput(final UIInput positionNameInput) {
        this.positionNameInput = positionNameInput;
    }

    private boolean verifyPositonNameIsAvailable() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Position> cq = cb.createQuery(Position.class);
    	Root<Position> fr = cq.from(Position.class);
    	cq.select( fr ).where( cb.equal(fr.get(Position_.name), newPosition.getName()) );
    	
    	TypedQuery<Position> q = em.createQuery( cq );
    	List<Position> existing = q.getResultList();
    	
        if ( existing != null && existing.size()>0 ) {
            messages.warn( new BundleKey("messages", "account_usernameTaken") )
                    .defaults( "The position name '{0}' is already taken. Please choose another position name.")
                    .params( newPosition.getName() )
                    .targets( positionNameInput.getClientId() );
            return false;
        }

        return true;
    }


	public List<Position> getExistingPositions() {
		@SuppressWarnings("unchecked")
		List<Position> existing = em.createQuery("select p from Position p").getResultList();
		return existing;
	}


	public String getNextPostionId() {
		return nextPostionId;
	}


	public void setNextPostionId(String nextPostionId) {
		this.nextPostionId = nextPostionId;
	}

}
