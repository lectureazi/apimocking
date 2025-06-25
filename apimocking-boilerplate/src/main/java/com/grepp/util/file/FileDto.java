package com.grepp.util.file;

public record FileDto(
    String originFileName,
    String renameFileName,
    String depth,
    String savePath
) {

}
