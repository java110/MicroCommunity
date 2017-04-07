package com.java110.common.util;

/**
 * Created by wuxw on 2017/2/26.
 */

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends LinkedHashMap {
    public static final byte[] BOM = {
            -17, -69, -65};
    public static final char[] HexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
    public static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>", 34);
    public static Pattern patternHtmlTag = Pattern.compile("<[^<>]+>", 32);
    public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", 34);
    public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", 34);
    public static final Pattern PDigit = Pattern.compile("^\\d*$", 34);
    private static Pattern chinesePattern = Pattern.compile("[^一-]+", 34);
    private static Pattern idPattern = Pattern.compile("[\\w\\s\\_\\.\\,]*", 34);

    public static String md5Base64FromHex(String md5str) {
        char[] cs = md5str.toCharArray();
        byte[] bs = new byte[16];
        for (int i = 0; i < bs.length; ++i) {
            char c1 = cs[(i * 2)];
            char c2 = cs[(i * 2 + 1)];
            byte m1 = 0;
            byte m2 = 0;
            for (byte k = 0; k < 16; k = (byte) (k + 1)) {
                if (HexDigits[k] == c1)
                    m1 = k;
                if (HexDigits[k] == c2)
                    m2 = k;
            }

            bs[i] = (byte) (m1 << 4 | 0 + m2);
        }

        return "";
    }

    public static String hexEncode(byte[] bs) {
        return new String(new Hex().encode(bs));
    }

    public static String byteToBin(byte[] bs) {
        char[] cs = new char[bs.length * 9];
        for (int i = 0; i < bs.length; ++i) {
            byte b = bs[i];
            int j = i * 9;
            cs[j] = (((b >>> 7 & 0x1) != 1) ? 48 : '1');
            cs[(j + 1)] = (((b >>> 6 & 0x1) != 1) ? 48 : '1');
            cs[(j + 2)] = (((b >>> 5 & 0x1) != 1) ? 48 : '1');
            cs[(j + 3)] = (((b >>> 4 & 0x1) != 1) ? 48 : '1');
            cs[(j + 4)] = (((b >>> 3 & 0x1) != 1) ? 48 : '1');
            cs[(j + 5)] = (((b >>> 2 & 0x1) != 1) ? 48 : '1');
            cs[(j + 6)] = (((b >>> 1 & 0x1) != 1) ? 48 : '1');
            cs[(j + 7)] = (((b & 0x1) != 1) ? 48 : '1');
            cs[(j + 8)] = ',';
        }

        return new String(cs);
    }

    public static boolean isUTF8(byte[] bs) {
        if (hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals("efbbbf"))
            return true;
        int encodingBytesCount = 0;
        for (int i = 0; i < bs.length; ++i) {
            byte c = bs[i];
            if (encodingBytesCount == 0) {
                if ((c & 0x80) == 0) continue;
                if ((c & 0xC0) == 192) {
                    encodingBytesCount = 1;
                    for (c = (byte) (c << 2); (c & 0x80) == 128; ) {
                        c = (byte) (c << 1);
                        ++encodingBytesCount;
                    }
                    continue;
                }

                return false;
            }

            if ((c & 0xC0) == 128)
                --encodingBytesCount;
            else
                return false;
        }

        return (encodingBytesCount == 0);
    }

    public static String javaEncode(String txt) {
        if ((txt == null) || (txt.length() == 0)) {
            return txt;
        }

        txt = replaceEx(txt, "\\", "\\\\");
        txt = replaceEx(txt, "\r\n", "\n");
        txt = replaceEx(txt, "\r", "\\r");
        txt = replaceEx(txt, "\t", "\\t");
        txt = replaceEx(txt, "\n", "\\n");
        txt = replaceEx(txt, "\"", "\\\"");
        txt = replaceEx(txt, "'", "\\'");
        return txt;
    }




    public static String replaceEx(String str, String subStr, String reStr) {
        if (str == null)
            return null;
        if ((subStr == null) || (subStr.equals("")) || (subStr.length() > str.length()) || (reStr == null))
            return str;
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        while (true) {
            int index = str.indexOf(subStr, lastIndex);
            if (index < 0)
                break;
            sb.append(str.substring(lastIndex, index));
            sb.append(reStr);
            lastIndex = index + subStr.length();
        }

        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

    public static String replaceAllIgnoreCase(String source, String oldstring, String newstring) {
        Pattern p = Pattern.compile(oldstring, 34);
        Matcher m = p.matcher(source);
        return m.replaceAll(newstring);
    }



    public static String escape(String src) {
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); ++i) {
            char j = src.charAt(i);
            if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j))) {
                sb.append(j);
            } else if (j < 256) {
                sb.append("%");
                if (j < '\16')
                    sb.append("0");
                sb.append(Integer.toString(j, 16));
            } else {
                sb.append("%u");
                sb.append(Integer.toString(j, 16));
            }
        }

        return sb.toString();
    }

    public static String unescape(String src) {
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length());
        int lastPos = 0;
        int pos = 0;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                char ch;
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    sb.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    sb.append(ch);
                    lastPos = pos + 3;
                }
            } else if (pos == -1) {
                sb.append(src.substring(lastPos));
                lastPos = src.length();
            } else {
                sb.append(src.substring(lastPos, pos));
                lastPos = pos;
            }
        }
        return sb.toString();
    }

    public static String leftPad(String srcString, char c, int length) {
        if (srcString == null)
            srcString = "";
        int tLen = srcString.length();
        if (tLen >= length)
            return srcString;
        int iMax = length - tLen;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < iMax; ++i)
            sb.append(c);

        sb.append(srcString);
        return sb.toString();
    }

    public static String subString(String src, int length) {
        if (src == null)
            return null;
        int i = src.length();
        if (i > length)
            return src.substring(0, length);

        return src;
    }

    public static String getHtmlTitle(String html) {
        Matcher m = PTitle.matcher(html);
        if (m.find())
            return m.group(1).trim();

        return null;
    }


    public static String capitalize(String str) {
        int strLen = 0;
        if (str != null) if ((strLen = str.length()) != 0) {
            return str;
        }

        return strLen + Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }

    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    public static boolean isNotEmpty(String str) {
        return (!(isEmpty(str)));
    }

    public static final String noNull(String string, String defaultString) {
        return ((isEmpty(string)) ? defaultString : string);
    }

    public static final String noNull(String string) {
        return noNull(string, "");
    }

    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    public static String join(Object[][] arr) {
        return join(arr, "\n", ",");
    }

    public static String join(Object[] arr, String spliter) {
        if (arr == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            if (i != 0)
                sb.append(spliter);
            sb.append(arr[i]);
        }

        return sb.toString();
    }

    public static String join(Object[][] arr, String spliter1, String spliter2) {
        if (arr == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            if (i != 0)
                sb.append(spliter2);
            sb.append(join(arr[i], spliter2));
        }

        return sb.toString();
    }

    public static String join(List list) {
        return join(list, ",");
    }

    public static String join(List list, String spliter) {
        if (list == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); ++i) {
            if (i != 0)
                sb.append(spliter);
            sb.append(list.get(i));
        }

        return sb.toString();
    }

    public static int count(String str, String findStr) {
        int lastIndex = 0;
        int length = findStr.length();
        int count = 0;
        for (int start = 0; (start = str.indexOf(findStr, lastIndex)) >= 0; ) {
            lastIndex = start + length;
            ++count;
        }

        return count;
    }

    public static boolean isLetterOrDigit(String str) {
        return PLetterOrDigit.matcher(str).find();
    }

    public static boolean isLetter(String str) {
        return PLetter.matcher(str).find();
    }

    public static boolean isDigit(String str) {
        if (isEmpty(str))
            return false;

        return PDigit.matcher(str).find();
    }

    public static boolean containsChinese(String str) {
        return (!(chinesePattern.matcher(str).matches()));
    }

    public static boolean checkID(String str) {
        if (isEmpty(str))
            return true;
        return idPattern.matcher(str).matches();
    }

    public static String getURLExtName(String url) {
        if (isEmpty(url))
            return null;
        int index1 = url.indexOf(63);
        if (index1 == -1)
            index1 = url.length();
        int index2 = url.lastIndexOf(46, index1);
        if (index2 == -1)
            return null;
        int index3 = url.indexOf(47, 8);
        if (index3 == -1)
            return null;
        String ext = url.substring(index2 + 1, index1);
        if (ext.matches("[^\\/\\\\]*"))
            return ext;

        return null;
    }

    public static String getURLFileName(String url) {
        if (isEmpty(url))
            return null;
        int index1 = url.indexOf(63);
        if (index1 == -1)
            index1 = url.length();
        int index2 = url.lastIndexOf(47, index1);
        if ((index2 == -1) || (index2 < 8)) {
            return null;
        }

        String ext = url.substring(index2 + 1, index1);
        return ext;
    }

    public static String clearForXML(String str) {
        char[] cs = str.toCharArray();
        char[] ncs = new char[cs.length];
        int j = 0;
        for (int i = 0; i < cs.length; ++i)
            if (cs[i] <= 65533) {
                if (cs[i] < ' ')
                    if ((((cs[i] != '\t') ? 1 : 0) & ((cs[i] != '\n') ? 1 : 0) & ((cs[i] != '\r') ? 1 : 0)) != 0)
                        continue;
                ncs[(j++)] = cs[i];
            }
        ncs = ArrayUtils.subarray(ncs, 0, j);
        return new String(ncs);
    }

    public static String urlDecode(String str, String charset) {
        try {
            return new URLCodec().decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String spellImageUrl(String filePath, String fileName, String fileId, String fileSize) {
        StringBuffer sb = new StringBuffer();
        String[] suffixs = fileName.split("\\.");
        String suffix = suffixs[(suffixs.length - 1)];
        sb.append(filePath);
        sb.append("/");
        sb.append(fileId);
        if (isNotEmpty(fileSize)) {
            sb.append("_");
            sb.append(fileSize);
        }
        sb.append(".");
        sb.append(suffix);
        return sb.toString();
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if (c[i] == 12288)
                c[i] = ' ';
            else if ((c[i] > 65280) && (c[i] < 65375))
                c[i] = (char) (c[i] - 65248);

        }

        String returnString = new String(c);
        return returnString;
    }

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min)
            min = three;

        return min;
    }



    public static String removeNumber(String keyValue) {
        String str = keyValue;
        char[] ch = str.toCharArray();
        char[] ch1 = new char[str.length()];
        int j = 0;
        for (int i = 0; i < ch.length; ++i) {
            if ((ch[i] >= '0') && (ch[i] <= '9'))
                continue;

            ch1[j] = ch[i];
            ++j;
        }
        str = new String(ch1).toString().trim();
        return str.trim();
    }

}
