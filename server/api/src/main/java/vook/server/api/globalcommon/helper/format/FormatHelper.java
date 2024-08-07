package vook.server.api.globalcommon.helper.format;

import org.slf4j.helpers.MessageFormatter;

public class FormatHelper {
    /**
     * Slf4J 스타일로 문자열 포멧팅을 해주는 함수
     */
    public static String slf4j(String pattern, Object... params) {
        return MessageFormatter.arrayFormat(pattern, params).getMessage();
    }
}
