package org.example.services.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import static org.example.statics.StaticData.target;

@Component
public class FileLoaderFromURI {


    public void loadFile(String filename, String url) throws IOException {

        org.apache.commons.io.FileUtils.cleanDirectory(new File(target));

        URL link = new URL(url);
        File file = new File(String.valueOf(Paths.get(target + filename)));

        org.apache.commons.io.FileUtils.copyURLToFile(link, file);

    }

}
