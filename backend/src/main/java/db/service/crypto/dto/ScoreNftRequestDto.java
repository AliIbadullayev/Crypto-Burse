package db.service.crypto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScoreNftRequestDto {

    private Long nftId;

    // Без этой аннотации всегда false, так что она нужна
    @JsonProperty
    private boolean isLiked;
}
