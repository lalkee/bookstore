package com.lalke.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "authors")
public class Author {
    @Id
    private String id;
    @NonNull
    private String name;

    private String profileImageId;

    private Map<String, String> customAttributes = new HashMap<>();

    public void addAttribute(String key, String value){
        customAttributes.put(key, value);
    }

}
