package com.qualifyguru.qualify_guru_backend.application.port.out;

import java.io.InputStream;

public interface FileStoragePort {
    String uploadAndReturnKey(String fileName, InputStream content, String contentType, long contentLength);
}
