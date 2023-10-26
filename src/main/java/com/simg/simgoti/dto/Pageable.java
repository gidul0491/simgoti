package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pageable {
    int page;
    int size;
    String orderBy;
    int start;

    public Pageable(int page, int size, String orderBy){
        this.page = page;
        this.size = size;
        this.orderBy = orderBy;
        this.start = (page-1) * size;
    }
}
