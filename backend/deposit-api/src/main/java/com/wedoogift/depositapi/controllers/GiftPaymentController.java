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

import com.wedoogift.depositapi.controllers.model.CreatePayment;
import com.wedoogift.depositapi.domain.commands.PayGiftCommand;
import com.wedoogift.depositapi.domain.entities.GiftPayment;

/**
 * controller for the gifts
 */
@RestController
@RequestMapping(path = "gifts/payments", consumes = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class GiftPaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftPaymentController.class);

    private final CommandGateway commandGateway;

    public GiftPaymentController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * make a payment
     *
     * @param createPayment payment data
     * @return result
     */
    @PostMapping
    public CompletableFuture<Void> create(@NotNull @Valid @RequestBody CreatePayment createPayment) {
        LOGGER.info("Compute {} {} payment for {}",
                createPayment.amount().value(),
                createPayment.amount().currency(),
                createPayment.userId());

        var giftPayment = new GiftPayment();
        giftPayment.setAmount(createPayment.amount());
        giftPayment.setDate(LocalDate.now());

        var command = new PayGiftCommand(giftPayment, createPayment.userId());

        return commandGateway
                .send(command);
    }
}
