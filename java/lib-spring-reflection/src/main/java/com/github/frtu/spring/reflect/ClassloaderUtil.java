package com.github.frtu.spring.reflect;

import org.springframework.util.ClassUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleMavenPlugins/src/main/java/com/github/frtu/maven/plugin/utils/ClassloaderUtil.java">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class ClassloaderUtil {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ClassloaderUtil.class);

    public static void reloadClassloader(List<String> classpathElements) {
        Set<URI> uris = new HashSet<URI>();
        for (String element : classpathElements) {
            final URI uri = new File(element).toURI();
            uris.add(uri);
            logger.debug("Add to url list:{}", uris);
        }

        logger.info("All uris to the Thread.currentThread().setContextClassLoader()");
        // ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader currentContextClassLoader = ClassUtils.getDefaultClassLoader();


        final List<URL> urlList = uris.stream().map(uri -> {
            try {
                return uri.toURL();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }).collect(Collectors.toList());
        ClassLoader newContextClassLoader = URLClassLoader.newInstance(urlList.toArray(new URL[urlList.size()]), currentContextClassLoader);

        // Thread.currentThread().setContextClassLoader(newContextClassLoader);
        ClassUtils.overrideThreadContextClassLoader(newContextClassLoader);
    }
}
