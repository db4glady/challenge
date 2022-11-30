package com.wedoogift.depositapi.controllers;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wedoogift.depositapi.controllers.model.CreateGift;
import com.wedoogift.depositapi.domain.commands.DistributeDepositFromCompanyCommand;
import com.wedoogift.depositapi.domain.commands.DistributeGiftDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;

/**
 * controller for the gifts
 */
@RestController
@RequestMapping(path = "gifts/deposits", consumes = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class GiftDepositController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftDepositController.class);

    private final CommandGateway commandGateway;

    public GiftDepositController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * make a gift
     *
     * @param createGift gift data
     * @return result
     */
    @PostMapping
    public CompletableFuture<Void> create(@NotNull @Valid @RequestBody CreateGift createGift) {
        LOGGER.info("Compute {} {} gift deposit from {} to {}",
                createGift.amount().value(),
                createGift.amount().currency(),
                createGift.companyId(),
                createGift.userId());

        var giftDeposit = new GiftDeposit();
        giftDeposit.setAmount(createGift.amount());
        giftDeposit.setCreationDate(LocalDate.now());

        var distributeDepositForUserCommand = new DistributeGiftDepositForUserCommand(giftDeposit, createGift.userId());
        var distributeDepositFromCompanyCommand = new DistributeDepositFromCompanyCommand(giftDeposit, createGift.companyId());

        return commandGateway
                .send(distributeDepositFromCompanyCommand)
                .thenCompose(result -> commandGateway.send(distributeDepositForUserCommand));
    }
}
