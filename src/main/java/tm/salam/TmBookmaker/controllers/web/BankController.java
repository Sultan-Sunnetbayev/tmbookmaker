package tm.salam.TmBookmaker.controllers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bank;
import tm.salam.TmBookmaker.services.BankService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping(path = "/add-bank", produces = "application/json")
    public ResponseEntity<?> addBank(@RequestBody @Validated Bank bank){

        final ResponseTransfer<?> responseTransfer=bankService.addBank(bank);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer);
    }

    @GetMapping(path = "/get-bank", params = {"bankUuid"}, produces = "application/json")
    public ResponseEntity<?>getBank(@RequestParam("bankUuid")UUID bankUuid){

        final ResponseTransfer<?>responseTransfer=bankService.getBankDTOByUuid(bankUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer);
    }

    @PostMapping(path = "/edit-bank", produces = "application/json")
    public ResponseEntity<?>editBank(@RequestBody @Validated Bank bank){

        final ResponseTransfer<?>responseTransfer=bankService.editBank(bank);

        return  ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer);
    }

    @PostMapping(path = "/remove-bank", params = {"bankUuid"}, produces = "application/json")
    public ResponseEntity<?>removeBank(@RequestParam("bankUuid")UUID bankUuid){

        final ResponseTransfer<?>responseTransfer=bankService.removeBankByUuid(bankUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer);
    }

}
