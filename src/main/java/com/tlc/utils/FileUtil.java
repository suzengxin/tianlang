package com.tlc.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;

/**
 * @author zhaojun
 */
public class FileUtil {

    public static ResponseEntity<Object> export(File file, String fileName) {
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 FILE NOT FOUND");
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");

        if (StringUtils.isNotBlank(fileName)) {
            fileName = file.getName();
        }

        headers.setContentDispositionFormData("attachment", file.getName());

        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }

    public static ResponseEntity<Object> export(File file) {
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 FILE NOT FOUND");
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.setContentDispositionFormData("attachment", file.getName());

        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }
}
