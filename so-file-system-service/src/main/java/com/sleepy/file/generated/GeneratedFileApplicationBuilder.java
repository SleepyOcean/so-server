package com.sleepy.file.generated;

import com.sleepy.file.FileApplication;
import com.sleepy.file.FileApplicationBuilder;
import com.sleepy.file.FileApplicationImpl;
import com.sleepy.file.FileInjectorProxy;
import com.sleepy.file.img.so.so_gallery.SoGalleryManagerImpl;
import com.sleepy.file.img.so.so_gallery.SoGallerySqlAdapter;
import com.sleepy.file.img.so.so_gallery_tag_map.SoGalleryTagMapManagerImpl;
import com.sleepy.file.img.so.so_gallery_tag_map.SoGalleryTagMapSqlAdapter;
import com.sleepy.file.img.so.so_tag.SoTagManagerImpl;
import com.sleepy.file.img.so.so_tag.SoTagSqlAdapter;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.common.injector.Injector;
import com.speedment.runtime.application.AbstractApplicationBuilder;
import com.speedment.runtime.connector.mysql.MySqlBundle;

/**
 * A generated base {@link
 * com.speedment.runtime.application.AbstractApplicationBuilder} class for the
 * {@link com.speedment.runtime.config.Project} named dev.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 *
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedFileApplicationBuilder extends AbstractApplicationBuilder<FileApplication, FileApplicationBuilder> {

    protected GeneratedFileApplicationBuilder() {
        super(FileApplicationImpl.class, GeneratedFileMetadata.class);
        withManager(SoGalleryManagerImpl.class);
        withManager(SoGalleryTagMapManagerImpl.class);
        withManager(SoTagManagerImpl.class);
        withComponent(SoGallerySqlAdapter.class);
        withComponent(SoGalleryTagMapSqlAdapter.class);
        withComponent(SoTagSqlAdapter.class);
        withBundle(MySqlBundle.class);
        withInjectorProxy(new FileInjectorProxy());
    }

    @Override
    public FileApplication build(Injector injector) {
        return injector.getOrThrow(FileApplication.class);
    }
}