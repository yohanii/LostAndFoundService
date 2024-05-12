package com.yohanii.lostandfound.component.admin.dto;

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
