package org.zstack.rest.sdk;

/**
 * Created by xing5 on 2016/12/23.
 */
public interface DocumentGenerator {
    enum DocMode {
        RECREATE_ALL,
        CREATE_MISSING,
    }

    void generateDocTemplates(String scanPath, DocMode mode);
}
