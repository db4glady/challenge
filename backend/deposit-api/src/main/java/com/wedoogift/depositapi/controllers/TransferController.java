package com.wedoogift.depositapi.controllers;

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

import com.wedoogift.depositapi.controllers.model.CreateMoneyTransfer;
import com.wedoogift.depositapi.domain.commands.CompanyMoneyTransferCommand;

/**
 * controller for the gifts
 */
@RestController
@RequestMapping(path = "transfers", consumes = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class TransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final CommandGateway commandGateway;

    public TransferController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * make a money transfer
     *
     * @param createMoneyTransfer the transfer data
     * @return result
     */
    @PostMapping
    public CompletableFuture<Void> create(@NotNull @Valid @RequestBody CreateMoneyTransfer createMoneyTransfer) {
        LOGGER.info("Transfer {} {} to {}",
                createMoneyTransfer.amount().value(),
                createMoneyTransfer.amount().currency(),
                createMoneyTransfer.companyId());

        var transferCommand = new CompanyMoneyTransferCommand(createMoneyTransfer.amount(), createMoneyTransfer.companyId());

        return commandGateway
                .send(transferCommand);
    }
}
