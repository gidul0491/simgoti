package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pageable {
    private int page;
    private int size;
    private String orderBy;
    private int start;

    public Pageable(int page, int size, String orderBy){
        this.page = page;
        this.size = size;
        this.orderBy = orderBy;
        this.start = page * size;
    }
}
