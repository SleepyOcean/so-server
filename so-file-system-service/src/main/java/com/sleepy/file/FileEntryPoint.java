package com.sleepy.file;

/**
 * The entry point for the {@link com.speedment.runtime.config.Project} named
 * dev
 * <p>
 * This file is safe to edit. It will not be overwritten by the code generator.
 *
 * @author sleepy
 */
public final class FileEntryPoint {

    public static void main(String... args) {
        final FileApplication application = new FileApplicationBuilder()
                // Add bundles, auth information, etc.
                .build();

        // Application logic goes here

        application.stop();
    }
}