package com.yohanii.lostandfound.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OverviewResponseDto {

    private long memberCount;
    private long totalPostCount;
    private long lostPostCount;
    private long foundPostCount;
    private long roomCount;

}
