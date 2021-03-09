package com.orange.neptunev2.backend_registry.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.orange.neptunev2.backend_registry.entity.Backend;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class CustomPage {

    List<Backend> content;
    CustomPageable pageable;

    public CustomPage(Page<Backend> page) {


        /*this.content = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(), page.getTotalElements());*/
    }


    @Data
    class CustomPageable {


        int page;
        int itemPerPage;
        int fullyItems;

        int pageNumber;
        int pageSize;
        long totalElements;

        public CustomPageable(int pageNumber, int pageSize, long totalElements, ObjectWriter objectWriter) {

            if (objectWriter == null) {
                objectWriter = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")).writer();
            }

            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
        }
    }
}
