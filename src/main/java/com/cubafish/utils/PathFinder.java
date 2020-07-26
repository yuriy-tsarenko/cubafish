package com.cubafish.utils;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PathFinder {
    public Path getPath() {
        Path path = Paths.get(String.valueOf(PathFinder.class));
        Path absolute = path.toAbsolutePath();
        String[] massive = absolute.toString().split("class");
        String parsed = massive[0].concat("src/main/resources/static/upload_files");
        absolute = Paths.get(parsed);
        return absolute;
    }

    public Path getDownloadPath() {
        Path local = getPath();
        String[] massive = local.toString().split("static");
        String parsed = massive[1];
        local = Paths.get(parsed);
        return local;
    }

    public Path getPathBeforeDelete() {
        Path pathForDelete = getPath();
        String[] massive = pathForDelete.toString().split("upload_files");
        String parsed = massive[0].substring(0, massive[0].length() - 1);
        pathForDelete = Paths.get(parsed);
        return pathForDelete;
    }

    public Path getPathFromTarget() {
        Path path = Paths.get(String.valueOf(PathFinder.class));
        Path absolute = path.toAbsolutePath();
        String[] massive = absolute.toString().split("class");
        String parsed = massive[0].concat("target/classes/static/upload_files");
        absolute = Paths.get(parsed);
        return absolute;
    }

    public Path getPathBeforeDeleteFromTarget() {
        Path pathForDelete = getPathFromTarget();
        String[] massive = pathForDelete.toString().split("upload_files");
        String parsed = massive[0].substring(0, massive[0].length() - 1);
        pathForDelete = Paths.get(parsed);
        return pathForDelete;
    }
}