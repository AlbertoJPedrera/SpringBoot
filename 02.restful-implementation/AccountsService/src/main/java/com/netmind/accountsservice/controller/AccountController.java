package com.netmind.accountsservice.controller;

import com.netmind.accountsservice.exception.AccountNotfoundException;
import com.netmind.accountsservice.model.Account;
import com.netmind.accountsservice.services.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Account>> getAll(@RequestParam(defaultValue = "") String name) {
        return new ResponseEntity<>(accountService.getAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    //@Valid
    @Operation(summary = "Get a product by id", description = "Returns a account as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The account was not found")
    })
    public ResponseEntity<Account> getAnAccounts(
            @Parameter(name = "aid", description = "Account id", example = "1")
            @PathVariable(name = "aid") @Min(1) Long id
    ) {
        Account acc = accountService.getAccount(id);
        if (acc != null){
            return new ResponseEntity<>(acc, HttpStatus.OK);
        }
        else {
            throw new AccountNotfoundException(id);
        }
    }

    @PostMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Add a new account", description = "Returns a presisted account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created"),
            @ApiResponse(responseCode = "4XX", description = "Bad request")
    })
    public ResponseEntity<Account> addAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody( required = true, description = "Account data")
            @RequestBody Account newA
    ) {
        newA.setId(null);
        accountService.create(newA);
        return new ResponseEntity(newA, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account aAccount) {
        aAccount.setId(id);
        accountService.updateAccount(id, aAccount);
        return new ResponseEntity(aAccount, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{accountId}/balance/deposit",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Add account to the balance", description = "Returns a current balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created"),
            @ApiResponse(responseCode = "4XX", description = "Bad request")
    })
    public ResponseEntity<Account> addAccount(
            @PathVariable Long idA, @PathVariable int money, @PathVariable Long owner
    ) {
        Account account = accountService.addBalance(idA, money, owner);
        return new ResponseEntity(account, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{accountId}/balance/withdraw",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Withdraw account to the balance", description = "Returns a current balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created"),
            @ApiResponse(responseCode = "4XX", description = "Bad request")
    })
    public ResponseEntity<Account> withdrawAccount(
            @PathVariable Long idA, @PathVariable int money, @PathVariable Long owner
    ) {
        Account account = accountService.withdrawBalance(idA, money, owner);
        return new ResponseEntity(account, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/owner/{idO}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAccountByOwner(@PathVariable Long accountId, @PathVariable Long idO) {
        accountService.deleteAccountsUsingOwnerId(idO);
        return ResponseEntity.noContent().build();
    }
}