package com.ttm.base_lib.kandroid

import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 统计词语个数
 *
 * @param str
 * @return
 */
inline fun String.countChineseWord(): Int {
    var count: Int = 0
    this.toCharArray().forEach {
        if (Pattern.matches("^[\u4E00-\u9Fa5]*$", it + "")) {
            count++
        }
    }
    return count
}

/**
 * 判断是否为数字
 *
 * @param str
 * @return
 */
inline fun String.isNumeric(): Boolean {
    val pattern = Pattern.compile("[0-9]*")
    val isNum = pattern.matcher(this)
    return isNum.matches()
}


/*
     * 手机号验证
     *
     * @param str
     *
     * @return 验证通过返回true
     */
inline fun String.isMobileNumber(): Boolean {
    var p: Pattern = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$") // 验证手机号
    var m: Matcher = p.matcher(this)
    return m.matches()
}


/*
     * 邮箱号验证
     *
     * @param str
     *
     * @return 验证通过返回true
     */
inline fun String.isEmail(): Boolean {
    try {
        // 正常邮箱
        // /^\w+((-\w)|(\.\w))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
        // 含有特殊 字符的 个人邮箱 和 正常邮箱
        // js: 个人邮箱
        // /^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/
        // java：个人邮箱
        // [\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+
        // 范围 更广的 邮箱验证 “/^[^@]+@.+\\..+$/”
        val pattern1 =
            "[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+";
        val pattern = Pattern.compile(pattern1);
        val mat = pattern.matcher(this);
        return mat.matches();
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return false;
}
