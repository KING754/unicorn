package com.game.rpc.generator;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.game.rpc.RpcConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/04
 */
@Slf4j
public class BaseGenerator {
    public static final String STRUCT_START_FLAG = "struct";

    public static final String ENUM_START_FLAG = "enum";
    public static final String OPENING_BRACE = "{";
    public static final String CLOSING_BRACE = "}";

    private static final VelocityEngine ENGINE = new VelocityEngine();

    static {
        ENGINE.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        ENGINE.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        ENGINE.init();
    }

    public void writeFile(VelocityContext ctx, String templateName, String fileName) throws IOException {
        ctx.put("date", DateUtil.format(new Date(), "yyyy/M/dd HH:mm:ss"));

        Template template = ENGINE.getTemplate(templateName);

        File outputFile = new File(fileName);
        FileWriter writer = new FileWriter(outputFile);

        template.merge(ctx, writer);

        writer.flush();
        writer.close();
        log.info("write suss:{}", fileName);
    }


    public List<String> readEnumContent(String fileFullPath) {
        List<String> fileContent = FileUtil.readLines(fileFullPath, RpcConstant.DEFAULT_CHARSET);
        List<String> enumCotentList = new ArrayList<>();
        for (String line : fileContent) {
            line = line.trim();
            if (line.startsWith("E_")) {
                enumCotentList.add(line);
            }
        }
        return enumCotentList;
    }


    public List<String> readFileContent(File file) {
       return FileUtil.readLines(file, RpcConstant.DEFAULT_CHARSET);
    }


    protected boolean isStartLine(String line) {
        line = line.trim();
        return isStructStartLine(line) || isEnumStartLine(line);
    }

    protected boolean isStructStartLine(String line){
        return line.startsWith(STRUCT_START_FLAG) && (line.endsWith(OPENING_BRACE) || (line.contains(OPENING_BRACE) && line.contains(RpcConstant.COMMENT_START_FLAG)));
    }

    protected boolean isEnumStartLine(String line){
        return line.startsWith(ENUM_START_FLAG) && (line.endsWith(OPENING_BRACE) || (line.contains(OPENING_BRACE) && line.contains(RpcConstant.COMMENT_START_FLAG)));
    }

    protected boolean isEndLine(String line) {
        return line.startsWith(CLOSING_BRACE);
    }

    protected String parseStructName(String structStartLine) {
        String name;
        int commentStartIndex = structStartLine.indexOf(RpcConstant.COMMENT_START_FLAG);
        if (commentStartIndex >= 0) {
            structStartLine = structStartLine.substring(0, commentStartIndex);
        }
        name = structStartLine.replace(STRUCT_START_FLAG, "")
                .replace(ENUM_START_FLAG,"")
                .replace(OPENING_BRACE, "")
                .trim();
        return name;
    }


    protected String parseLineComment(String structStartLine) {
        int commentStartIndex = structStartLine.indexOf(RpcConstant.COMMENT_START_FLAG);
        if (commentStartIndex < 0) {
            return null;
        }
        return structStartLine.substring(commentStartIndex + 2);
    }
}

