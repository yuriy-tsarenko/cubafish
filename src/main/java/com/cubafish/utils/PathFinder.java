package com.cubafish.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Getter
public class PathFinder {

    @Value("${upload.path}")
    String uploadPath;

    public Path getPath() {
        Path path = Paths.get(String.valueOf(PathFinder.class));
        Path absolute = path.toAbsolutePath();
        String[] massive = absolute.toString().split("class");
        String parsed = massive[0].concat(uploadPath);
        absolute = Paths.get(parsed);
        System.out.println(absolute);
        System.out.println(path);
        return absolute;
    }

    public Path getDownloadPath() {
        return Paths.get(uploadPath);
    }

    public Path getPathBeforeDelete() {
        Path pathForDelete = getPath();
        String[] massive = pathForDelete.toString().split("upload_files");
        String parsed = massive[0].substring(0, massive[0].length() - 1);
        pathForDelete = Paths.get(parsed);
        System.out.println(pathForDelete);
        return pathForDelete;
    }
}