package com.cubafish.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Getter
public class PathFinder {

    public Path getDownloadPath(String uploadPath) {
        String[] massive = uploadPath.split("static");
        String parsed = massive[1];
        Path local = Paths.get(parsed);
        return local;
    }

    public Path getPathBeforeDelete(String absolutePath) {
        String[] massive = absolutePath.split("upload_files");
        String parsed = massive[0].substring(0, massive[0].length() - 1);
        Path pathForDelete = Paths.get(parsed).normalize();
        System.out.println(pathForDelete);
        return pathForDelete;
    }
}