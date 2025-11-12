package com.definejae234.cardproject.member.utils;

import java.util.UUID;

public class UUIDRenameStrategy implements FileRenameStrategy{
    @Override
    public String rename(String originalFileName) {
        String filename = originalFileName.substring(0,originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        String uuid = UUID.randomUUID().toString();
        return filename+"_"+uuid+"."+extension;
    }
}
