package com.wangpeng.myplayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.wangpeng.myplayer.beans.LrcContent;

/**
 * 歌词工具类
 */
public class LrcUtil {
    public static List<LrcContent> parseLrcStr(String lrcContent) {
        List<LrcContent> lrclists = new ArrayList<>();
        String lines[] = lrcContent.split("\n");
        for (String line : lines) {
            handleOneLine(line, lrclists);
        }
        return lrclists;
    }

    static void handleOneLine(String line, List<LrcContent> lrclists) {
        String s = line.replace("[", ""); // 去掉左边括号
        String lrcData[] = s.split("]");

        // 这句是歌词
        if (lrcData[0].matches("^\\d{2}:\\d{2}.\\d+$")) {
            int len = lrcData.length;
            int end = lrcData[len - 1].matches("^\\d{2}:\\d{2}.\\d+$") ? len
                    : len - 1;

            for (int i = 0; i < end; i++) {
                LrcContent lrcContent = new LrcContent();
                int lrcTime = TimeUtil.getLrcMillTime(lrcData[i]);
                lrcContent.setLrcTime(lrcTime);
                if (lrcData.length == end)
                    lrcContent.setLrcStr(""); // 空白行
                else
                    lrcContent.setLrcStr(lrcData[len - 1]);

                lrclists.add(lrcContent);
            }

        }
    }


}
