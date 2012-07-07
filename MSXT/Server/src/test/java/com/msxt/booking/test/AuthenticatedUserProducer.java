package com.msxt.booking.test;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.msxt.booking.account.Authenticated;

import com.msxt.model.User;

public class AuthenticatedUserProducer {
    @PersistenceContext
    EntityManager em;

    @Produces
    @Authenticated
    public User getRegisteredUser() {
        return em.find(User.class, "ike");
    }
}
