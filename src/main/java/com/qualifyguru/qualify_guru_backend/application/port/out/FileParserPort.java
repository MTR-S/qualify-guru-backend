package com.qualifyguru.qualify_guru_backend.application.port.out;

import java.io.InputStream;

public interface FileParserPort {
    String extractText(InputStream fileStream);
}
