package com.cubafish.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Getter
public class PathFinder {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${download.path}")
    private String downloadPath;

    public Path getUploadPath(ServletContext pathFromHttpContext) {
        String absolutePath = pathFromHttpContext.getRealPath(uploadPath);
        String[] massive = absolutePath.split("ROOT");
        String parsed = massive[0].substring(0, massive[0].length() - 1).concat(massive[1]);
        Path goodPath = Paths.get(parsed);
        return goodPath;
    }

    public Path getDownloadPath() {
        Path local = Paths.get(downloadPath);
        return local;
    }

    public Path getPathBeforeDelete(ServletContext pathFromHttpContext, String pathFromDb) {
        String absolutePath = pathFromHttpContext.getRealPath(uploadPath);
        String[] massiveSplitAbsolutePath = absolutePath.split("ROOT");
        String parsedAbsolutePath = massiveSplitAbsolutePath[0]
                .substring(0, massiveSplitAbsolutePath[0].length() - 1).concat(massiveSplitAbsolutePath[1]);
        String[] massiveSplitPath = pathFromDb.split("upload_files/");
        String parsedPath = massiveSplitPath[1];
        Path pathForDelete = Paths.get(parsedAbsolutePath.toString().concat(File.separator).concat(parsedPath));
        return pathForDelete;
    }
}