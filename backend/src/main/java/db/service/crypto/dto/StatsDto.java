package db.service.crypto.dto;

import lombok.Data;

@Data
public class StatsDto {

    private int allCount;

    private int confirmedByAdminCount;
    private int canceledByAdminCount;

    private int allForAdminCount;
}
