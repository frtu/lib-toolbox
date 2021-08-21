package com.github.frtu.test.data;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Allow to easily read data from resources
 *
 * @author Frédéric TU
 * @since 1.1.3
 */
public class ResourceHelper {
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public String readFromFile(String location) {
        try {
            val absolutePath = resourceLoader.getResource(location).getFile().getAbsolutePath();
            val file = new File(absolutePath);
            if (file.exists() && file.canRead()) {
                logger.debug("Reading file from location:{} & absolutePath:{}", location, absolutePath);
                val text = readFromFile(file);
                logger.debug("Deserialize text:{}", text);
                return text;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.warn("File doesn't exist or not readable at location:$location & absolutePath:$absolutePath");
        return null;
    }

    public String readFromFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return contentBuilder.toString();
    }

    private final static Logger logger = LoggerFactory.getLogger(ResourceHelper.class);
}
