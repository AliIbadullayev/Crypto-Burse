package db.service.crypto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsDto {

    private int allCount;
    private int confirmedByAdminCount;
    private int canceledByAdminCount;
    private int allForAdminCount;
}
