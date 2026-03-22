package com.lalke.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "products")
public class Book {
    @Id
    private String id;
    @NonNull
    private String title;

    private String coverImageId;

    @NonNull
    private String authorId;
    @NonNull
    private String authorName;
    @NonNull
    private BigDecimal price;

    private Map<String, String> customAttributes = new HashMap<>();

    public void addAttribute(String key, String value){
        customAttributes.put(key, value);
    }
}
