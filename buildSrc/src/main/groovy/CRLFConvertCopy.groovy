/* -*- Mode: groovy; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 * This file is dual licensed under the BSD-3-Clause and the MPL.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Converts line endings to CRLF (Windows), copy to build/tmp/crlf
 * <p>
 * Usage:
 * </p>
 * <code>
 * tasks.register('lineEndingConvCopy', CRLFConvertCopy) {
 *     from "path/to/file1.txt"
 *     from "path/to/fileN.txt"
 * }
 * </code>
 */
class CRLFConvertCopy extends DefaultTask {

    private static final String CRLF = "\r\n"
    private static final String LF = "\n"

    private files = []

    @TaskAction
    def action() {
        def tmpDir = new File(project.buildDir, 'tmp');
        def destDir = new File(tmpDir, 'crlf');
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        files.each { path ->
            def file = new File(path)
            if (file.exists()) {
                String content = file.text
                String newContent = content.replaceAll(/\r\n/, LF)
                newContent = newContent.replaceAll(/\n|\r/, CRLF)
                def dest = new File(destDir, file.name);
                dest.write(newContent, 'utf-8')
            } else {
                logger.warn('File ' + path + ' does not exist.')
            }
        }
    }

    def from(String path) {
        this.files << path
    }
}
