package com.msxt.security;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.msxt.booking.account.Authenticated;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.impl.api.PasswordCredential;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.User;
import com.msxt.model.User_;

/**
 * This implementation of a <strong>Authenticator</strong> that uses Seam security.
 *
 * @author <a href="http://community.jboss.org/people/spinner)">Jose Rodolfo freitas</a>
 */
@Stateful
@Named("adminAuthenticator")
public class AdminAuthenticator extends BaseAuthenticator{

    @Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Credentials credentials;

    @Inject
    private Messages messages;

    @Inject
    @Authenticated
    private Event<User> loginEventSrc;

    public void authenticate() {    	
        log.info("Logging in " + credentials.getUsername());
        if ((credentials.getUsername() == null) || (credentials.getCredential() == null)) {
            messages.error( new DefaultBundleKey("identity_loginFailed") ).defaults( "Invalid username or password" );
            setStatus(AuthenticationStatus.FAILURE);
        }
        User user = checkUser(credentials);
        if (user != null) {
        	if( credentials.getCredential() instanceof PasswordCredential ) {
        		String password = ((PasswordCredential) credentials.getCredential()).getValue();
        		if( user.getPassword().equals( password ) ) {
		            loginEventSrc.fire(user);
		            messages.info( new DefaultBundleKey("identity_loggedIn"), user.getName()).defaults("You're signed in as {0}").params(user.getName() );
		            setStatus( AuthenticationStatus.SUCCESS );
		            setUser( user ); 
		            return;
        		}
        	}
        }

        messages.error( new DefaultBundleKey("identity_loginFailed") ).defaults("Invalid username or password");
        setStatus( AuthenticationStatus.FAILURE );
    }
    
    private User checkUser(Credentials credentials){
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<User> cq = cb.createQuery(User.class);
    	Root<User> fr = cq.from(User.class);
    	cq.select( fr ).where( cb.equal(fr.get(User_.loginname), credentials.getUsername()) );
    	
    	TypedQuery<User> q = em.createQuery( cq );
    	List<User> users = q.getResultList();
    	if( users.size()>0 )
    		return users.get(0);
    	else
    		return null;
    }
}
