package db.service.crypto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class ScoreNftRequestDto {

    private Long nftId;

//    без этой аннотации всегда false по дефолту, так что она нужна
    @JsonProperty
    private boolean isLiked;
}
