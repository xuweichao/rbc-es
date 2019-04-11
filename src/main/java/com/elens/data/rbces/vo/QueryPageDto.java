package com.elens.data.rbces.vo;

import lombok.Data;

@Data
public class QueryPageDto {

    private int page = 1;

    private int size = 10;
}
