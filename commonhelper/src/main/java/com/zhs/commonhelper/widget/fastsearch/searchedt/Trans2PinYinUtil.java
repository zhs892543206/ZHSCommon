package com.zhs.commonhelper.widget.fastsearch.searchedt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 汉字转换为拼音
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-08  18:43
 */

public class Trans2PinYinUtil {

    private static int[] pyvalue = new int[]{-20319, -20317, -20304, -20295,
            -20292, -20283, -20265, -20257, -20242, -20230, -20051, -20036,
            -20032, -20026, -20002, -19990, -19986, -19982, -19976, -19805,
            -19784, -19775, -19774, -19763, -19756, -19751, -19746, -19741,
            -19739, -19728, -19725, -19715, -19540, -19531, -19525, -19515,
            -19500, -19484, -19479, -19467, -19289, -19288, -19281, -19275,
            -19270, -19263, -19261, -19249, -19243, -19242, -19238, -19235,
            -19227, -19224, -19218, -19212, -19038, -19023, -19018, -19006,
            -19003, -18996, -18977, -18961, -18952, -18783, -18774, -18773,
            -18763, -18756, -18741, -18735, -18731, -18722, -18710, -18697,
            -18696, -18526, -18518, -18501, -18490, -18478, -18463, -18448,
            -18447, -18446, -18239, -18237, -18231, -18220, -18211, -18201,
            -18184, -18183, -18181, -18012, -17997, -17988, -17970, -17964,
            -17961, -17950, -17947, -17931, -17928, -17922, -17759, -17752,
            -17733, -17730, -17721, -17703, -17701, -17697, -17692, -17683,
            -17676, -17496, -17487, -17482, -17468, -17454, -17433, -17427,
            -17417, -17202, -17185, -16983, -16970, -16942, -16915, -16733,
            -16708, -16706, -16689, -16664, -16657, -16647, -16474, -16470,
            -16465, -16459, -16452, -16448, -16433, -16429, -16427, -16423,
            -16419, -16412, -16407, -16403, -16401, -16393, -16220, -16216,
            -16212, -16205, -16202, -16187, -16180, -16171, -16169, -16158,
            -16155, -15959, -15958, -15944, -15933, -15920, -15915, -15903,
            -15889, -15878, -15707, -15701, -15681, -15667, -15661, -15659,
            -15652, -15640, -15631, -15625, -15454, -15448, -15436, -15435,
            -15419, -15416, -15408, -15394, -15385, -15377, -15375, -15369,
            -15363, -15362, -15183, -15180, -15165, -15158, -15153, -15150,
            -15149, -15144, -15143, -15141, -15140, -15139, -15128, -15121,
            -15119, -15117, -15110, -15109, -14941, -14937, -14933, -14930,
            -14929, -14928, -14926, -14922, -14921, -14914, -14908, -14902,
            -14894, -14889, -14882, -14873, -14871, -14857, -14678, -14674,
            -14670, -14668, -14663, -14654, -14645, -14630, -14594, -14429,
            -14407, -14399, -14384, -14379, -14368, -14355, -14353, -14345,
            -14170, -14159, -14151, -14149, -14145, -14140, -14137, -14135,
            -14125, -14123, -14122, -14112, -14109, -14099, -14097, -14094,
            -14092, -14090, -14087, -14083, -13917, -13914, -13910, -13907,
            -13906, -13905, -13896, -13894, -13878, -13870, -13859, -13847,
            -13831, -13658, -13611, -13601, -13406, -13404, -13400, -13398,
            -13395, -13391, -13387, -13383, -13367, -13359, -13356, -13343,
            -13340, -13329, -13326, -13318, -13147, -13138, -13120, -13107,
            -13096, -13095, -13091, -13076, -13068, -13063, -13060, -12888,
            -12875, -12871, -12860, -12858, -12852, -12849, -12838, -12831,
            -12829, -12812, -12802, -12607, -12597, -12594, -12585, -12556,
            -12359, -12346, -12320, -12300, -12120, -12099, -12089, -12074,
            -12067, -12058, -12039, -11867, -11861, -11847, -11831, -11798,
            -11781, -11604, -11589, -11536, -11358, -11340, -11339, -11324,
            -11303, -11097, -11077, -11067, -11055, -11052, -11045, -11041,
            -11038, -11024, -11020, -11019, -11018, -11014, -10838, -10832,
            -10815, -10800, -10790, -10780, -10764, -10587, -10544, -10533,
            -10519, -10331, -10329, -10328, -10322, -10315, -10309, -10307,
            -10296, -10281, -10274, -10270, -10262, -10260, -10256, -10254};
    private static String[] pystr = new String[]{"a", "ai", "an", "ang",
            "ao", "ba", "bai", "ban", "bang", "bao", "bei", "ben", "beng",
            "bi", "bian", "biao", "bie", "bin", "bing", "bo", "bu", "ca",
            "cai", "can", "cang", "cao", "ce", "ceng", "cha", "chai", "chan",
            "chang", "chao", "che", "chen", "cheng", "chi", "chong", "chou",
            "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci",
            "cong", "cou", "cu", "cuan", "cui", "cun", "cuo", "da", "dai",
            "dan", "dang", "dao", "de", "deng", "di", "dian", "diao", "die",
            "ding", "diu", "dong", "dou", "du", "duan", "dui", "dun", "duo",
            "e", "en", "er", "fa", "fan", "fang", "fei", "fen", "feng", "fo",
            "fou", "fu", "ga", "gai", "gan", "gang", "gao", "ge", "gei", "gen",
            "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui",
            "gun", "guo", "ha", "hai", "han", "hang", "hao", "he", "hei",
            "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang",
            "hui", "hun", "huo", "ji", "jia", "jian", "jiang", "jiao", "jie",
            "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka",
            "kai", "kan", "kang", "kao", "ke", "ken", "keng", "kong", "kou",
            "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la",
            "lai", "lan", "lang", "lao", "le", "lei", "leng", "li", "lia",
            "lian", "liang", "liao", "lie", "lin", "ling", "liu", "long",
            "lou", "lu", "lv", "luan", "lue", "lun", "luo", "ma", "mai", "man",
            "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao",
            "mie", "min", "ming", "miu", "mo", "mou", "mu", "na", "nai", "nan",
            "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang",
            "niao", "nie", "nin", "ning", "niu", "nong", "nu", "nv", "nuan",
            "nue", "nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei",
            "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po",
            "pu", "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing",
            "qiong", "qiu", "qu", "quan", "que", "qun", "ran", "rang", "rao",
            "re", "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui",
            "run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen",
            "seng", "sha", "shai", "shan", "shang", "shao", "she", "shen",
            "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang",
            "shui", "shun", "shuo", "si", "song", "sou", "su", "suan", "sui",
            "sun", "suo", "ta", "tai", "tan", "tang", "tao", "te", "teng",
            "ti", "tian", "tiao", "tie", "ting", "tong", "tou", "tu", "tuan",
            "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen",
            "weng", "wo", "wu", "xi", "xia", "xian", "xiang", "xiao", "xie",
            "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya",
            "yan", "yang", "yao", "ye", "yi", "yin", "ying", "yo", "yong",
            "you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang",
            "zao", "ze", "zei", "zen", "zeng", "zha", "zhai", "zhan", "zhang",
            "zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu",
            "zhua", "zhuai", "zhuan", "zhuang", "zhui", "zhun", "zhuo", "zi",
            "zong", "zou", "zu", "zuan", "zui", "zun", "zuo"};
    private StringBuilder buffer;
    private String resource;
    private static Trans2PinYinUtil chineseSpelling = new Trans2PinYinUtil();

    public static Trans2PinYinUtil getInstance() {
        return chineseSpelling;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    private int getChsAscii(String chs) {
        int asc = 0;
        try {
            byte[] bytes = chs.getBytes("gb2312");
            if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误
                // log
                throw new RuntimeException("illegal resource string");
                // System.out.println("error");
            }
            if (bytes.length == 1) { // 英文字符
                asc = bytes[0];
            }
            if (bytes.length == 2) { // 中文字符
                int hightByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                asc = (256 * hightByte + lowByte) - 256 * 256;
            }
        } catch (Exception e) {
            System.out
                    .println("ERROR:ChineseSpelling.class-getChsAscii(String chs)"
                            + e);
            // e.printStackTrace();
        }
        return asc;
    }

    /**
     * 转换单个汉字
     *
     * @param str
     * @return
     */
    public String convert(String str) {
        String result = null;
        int ascii = getChsAscii(str);
        if (ascii > 0 && ascii < 160) {
            result = String.valueOf((char) ascii);
        } else {
            for (int i = (pyvalue.length - 1); i >= 0; i--) {
                if (pyvalue[i] <= ascii) {
                    result = pystr[i];
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 转换一个或多个汉字
     *
     * @param str
     * @return
     */
    public String convertAll(String str) {
        String result = "";
        String strTemp = null;
        for (int j = 0; j < str.length(); j++) {
            strTemp = str.substring(j, j + 1);
            int ascii = getChsAscii(strTemp);
            if (ascii > 0 && ascii < 160) {
                result += String.valueOf((char) ascii);
            } else {
                for (int i = (pyvalue.length - 1); i >= 0; i--) {
                    if (pyvalue[i] <= ascii) {
                        result += pystr[i];
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * byzhs
     * convertAl方法搜索必须全拼，所以写了这个都可以的方法
     * 转换一个或多个汉字
     *
     * @param str
     * @return
     */
    public String[] myConvertAll(String str) {
        String result = "";//全拼
        String result2 = "";//简拼
        String strTemp = null;
        for (int j = 0; j < str.length(); j++) {
            strTemp = str.substring(j, j + 1);
            int ascii = getChsAscii(strTemp);
            //160以前是字符，不是中文
            if (ascii > 0 && ascii < 160) {
                String valueStr = String.valueOf((char) ascii);
                if(result.length()>0){
                    result+=",";//逗号分隔这样全拼搜索才能去搜索一个字的全拼，而不是任意一个字母
                }
                result += valueStr;
                result2 += valueStr;

            } //160以后或小于0，中文是小于0 的
            else {
                for (int i = (pyvalue.length - 1); i >= 0; i--) {
                    if (pyvalue[i] <= ascii) {
                        if(result.length()>0){
                            result+=",";//逗号分隔这样全拼搜索才能去搜索一个字的全拼，而不是任意一个字母
                        }
                        result += pystr[i];
                        //对于中文简拼只获取第一个字母就好
                        if(pystr[i]!=null && pystr[i].length()>0){
                            result2 += pystr[i].substring(0, 1);
                        }
                        break;
                    }
                }

            }
        }
        return new String[]{result, result2};
    }

    public String getSelling(String chs) {
        String key, value;
        buffer = new StringBuilder();
        for (int i = 0; i < chs.length(); i++) {
            key = chs.substring(i, i + 1);
            if (key.getBytes().length == 2) {
                value = (String) convert(key);
                if (value == null) {
                    value = "unknown";
                }
            } else {
                value = key;
            }
            buffer.append(value);
        }
        return buffer.toString();
    }

    public String getSpelling() {
        return this.getSelling(this.getResource());
    }

    /**
     * 转换为拼音
     *
     * @param str
     * @return
     */
    public static String trans2PinYin(String str) {
        return Trans2PinYinUtil.getInstance().convertAll(str);
    }

    /**
     * 通过listitem 的name获取对应sorttoken信息
     * @param name
     * @return
     */
    public static SortToken getSortToken(String name){
        String[] letters = Trans2PinYinUtil.myTrans2PinYin(name);
        SortToken sortToken= new SortToken();
        sortToken.chName = name;
        sortToken.simpleSpell = letters[1];//简拼
        sortToken.wholeSpell = letters[0];//全拼
        return  sortToken;
    }

    /**
     * byzhs
     * 转换为拼音.返回全拼和简拼
     * @param str
     * @return
     */
    public static String[] myTrans2PinYin(String str) {
        return Trans2PinYinUtil.getInstance().myConvertAll(str.replace(" ", ""));//去除空格
    }

    /**
     *通过名字或者拼音搜索
     * 多音字暂时不管吧
     * @paramstr
     * @return
     */
    public static List<SortModel> searchContact(final String str, List<SortModel> mAllContactsList){
        List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
        //if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            for (SortModel contact : mAllContactsList) {

                if (contact.getSearchNameStr() != null) {
                    //去除空格
                    String name = contact.getSearchNameStr().replace(" ", "");
                    if (name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }else {
            for (SortModel contact : mAllContactsList) {
                if (contact.getSearchNameStr() != null) {
                    //去除空格
                    String name = contact.getSearchNameStr().replace(" ", "");
                    String wholeSpellStr = contact.sortToken.wholeSpell.replace(",", "");
                    String[] letters = contact.sortToken.wholeSpell.split(",");

                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            //|| wholeSpellStr.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))//字母只需要存在在全拼里面这样不好
                            ||  isContainWhole(letters, 0, 1, str.toLowerCase(Locale.CHINESE))) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        //按id搜索
        for (SortModel contact : mAllContactsList) {
            if (contact.getSearchId() != null) {

                if (contact.getSearchId().toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))) {
                    if (!filterList.contains(contact)) {
                        filterList.add(contact);
                    }
                }
            }
        }
        return filterList;
    }

    /**
     * 搜索条件是字符串中任意连续几个字的全拼
     * @param letters
     * @param start
     * @param end
     * @param searchStr
     * @return
     */
    public static boolean isContainWhole(String[] letters, int start, int end, String searchStr){
        String str = "";
        for(int i = start; i< letters.length && i < end; i++){
            str += letters[i];
        }

        if (str.toLowerCase(Locale.CHINESE).equals(searchStr)) {
            return true;
        }else if((end >=letters.length) && start < (letters.length-1)){//如果end到结尾了，start还没到letters.length-1则start加1。end从start的下一个开始
            return isContainWhole(letters, start+1, (start+1) +1, searchStr);
        }else if(end < letters.length){//end还没到最后则end+1，开始不变
            return isContainWhole(letters, start, end+1, searchStr);
        }else{
            return false;
        }
    }
}
