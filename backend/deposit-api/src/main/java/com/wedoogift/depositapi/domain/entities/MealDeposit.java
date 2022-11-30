package com.wedoogift.depositapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wedoogift.depositapi.services.ExpirationDateVisitor;

@JsonTypeName("meal")
public final class MealDeposit extends AbstractDeposit {

    public void accept(ExpirationDateVisitor expirationDateVisitor) {
        expirationDateVisitor.visit(this);
    }
}
