package com.lalke.bookstore.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ImageService {

    private final GridFsOperations gridFsOperations;
    private final GridFsTemplate gridFsTemplate;

    public String uploadImage(MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "cover");

        Object fileId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metaData
        );
        return fileId.toString();
    }

    public void renderImage(String id, HttpServletResponse response) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file != null) {
            response.setContentType(gridFsOperations.getResource(file).getContentType());
            IOUtils.copy(gridFsOperations.getResource(file).getInputStream(), response.getOutputStream());
        }
    }
}