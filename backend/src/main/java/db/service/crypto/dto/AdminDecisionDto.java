package db.service.crypto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminDecisionDto {

    private long p2pTransactionId;


    //Аннотация нужна, чтобы труфолс правильно передавался. Без неё не работает
    @JsonProperty
    private boolean isApproved;

}
