package com.wedoogift.depositapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wedoogift.depositapi.services.ExpirationDateVisitor;

@JsonTypeName("gift")
public final class GiftDeposit extends AbstractDeposit {

    public void accept(ExpirationDateVisitor expirationDateVisitor) {
        expirationDateVisitor.visit(this);
    }
}
