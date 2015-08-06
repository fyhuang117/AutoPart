package com.autoparts.buyers.model;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.*;

public class ContactUtils {

    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<CommonLetterModel>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;

    private static final String FORMAT = "^[a-z,A-Z].*$";
    private List<CommonLetterModel> mList;


    //字母Z使用了两个标签，这里有２７个值
    //i, u, v都不做声母, 跟随前面的字母
    private char[] chartable = {
            '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
            '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
            '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
    };

    private char[] alphatable = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    //初始化
    private int[] table = new int[27];

    {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }
    //主函数,输入字符,得到他的声母,
    //英文字母返回对应的大写字母
    //其他非简体汉字返回 '0'

    public ContactUtils() {
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<CommonLetterModel>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
    }

    public List<String> getmSections() {
        return mSections;
    }

    public Map<String, List<CommonLetterModel>> getmMap() {
        return mMap;
    }

    public List<Integer> getmPositions() {
        return mPositions;
    }

    public Map<String, Integer> getmIndexer() {
        return mIndexer;
    }

    public char Char2Alpha(char ch) {

        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;
        int gb = gbValue(ch);

        if (gb < table[0]) {
            return '#';
        }
        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb))
                break;
        }
        if (i >= 26) {
            return '#';
        } else {
            return alphatable[i];
        }
    }

    //根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
    public String String2Alpha(String SourceStr) {

        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Alpha(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";//52416 55713
        }
        return Result;
    }

    private boolean match(int i, int gb) {

        if (gb < table[i])
            return false;
        int j = i + 1;

        //字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i])) ++j;
        if (j == 26)
            return gb <= table[j];
        else
            return gb < table[j];
    }

    //取出汉字的编码
    private int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
                    0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    //获取首字母
    public List<CommonLetterModel> getList(List<CommonLetterModel> mList) {
        this.mList = mList;
        if (null != mList) {
            for (int i = 0; i < mList.size(); i++) {
                CommonLetterModel userInfo = mList.get(i);
                String key = String2Alpha(userInfo.user_name.substring(0, 1));
                userInfo.user_key = key;
                mList.set(i, userInfo);
            }
            //根据首字母分类
            return prepareCityList();
        }
        return new ArrayList<CommonLetterModel>();

    }

    //获取首字母
    public List<CommonLetterModel> getListKey(List<CommonLetterModel> mList) {
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<CommonLetterModel>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        this.mList = mList;
        return prepareCityList();

    }

    private List<CommonLetterModel> prepareCityList() {
        for (int i = 0; i < mList.size(); i++) {
            CommonLetterModel friendInfo = mList.get(i);
            String firstName = friendInfo.user_key;// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(friendInfo);
                } else {
                    mSections.add(firstName);
                    List<CommonLetterModel> list = new ArrayList<CommonLetterModel>();
                    list.add(friendInfo);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(friendInfo);
                } else {
                    mSections.add("#");
                    List<CommonLetterModel> list = new ArrayList<CommonLetterModel>();
                    list.add(friendInfo);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        //存储数据
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        //重新派讯
        return getNewList(mMap);
    }

    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mList.size()) {
            return -1;
        }
        int index = Arrays.binarySearch(mPositions.toArray(), position);
        return index >= 0 ? index : -index - 2;
    }

    public int getPositionForSection(int section) {
        // TODO Auto-generated method stub
        if (section < 0 || section >= mPositions.size()) {
            return -1;
        }
        return mPositions.get(section);
    }

    public List<CommonLetterModel> getNewList(Map<String, List<CommonLetterModel>> mMap) {
        List<CommonLetterModel> newList = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < mList.size(); i++) {
            int section = getSectionForPosition(i);
            CommonLetterModel info = mMap.get(mSections.get(section)).get(i - getPositionForSection(section));
            newList.add(info);
        }
        return newList;
    }

    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return String
     */
    public String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    public List<CommonLetterModel> getContactList(Vector<HashMap<String, String>> lists) {
        List<CommonLetterModel> contacts = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < lists.size(); i++) {
            CommonLetterModel contactModel = new CommonLetterModel();
            contactModel.setUser_id(lists.get(i).get("id"));
            contactModel.setUser_image(lists.get(i).get("image"));
            String name = lists.get(i).get("username").trim();
            contactModel.setUser_name(name);
//            String key = new ContactUtils().String2Alpha(name.substring(0, 1));

            String key = new ContactUtils().getPinYinHeadChar(name.substring(0, 1));
            key = key.toUpperCase();
            if (TextUtils.isEmpty(key)) {
                key = "#";
            }

            contactModel.setUser_key(key);
            String desc = "";
            if (lists.get(i).containsKey("description")) {
                desc = lists.get(i).get("description");
            } else {
                desc = lists.get(i).get("relation");
            }
            contactModel.setUser_description(desc);

            contactModel.setUser_sex(lists.get(i).get("sex"));
            contactModel.setUser_degree(lists.get(i).get("degree"));
            contactModel.setUser_resume(lists.get(i).get("resume"));
            contacts.add(contactModel);
        }
        return contacts;

    }

    String[] attr = {"奥迪", "安凯客车", "阿尔法罗密欧", "阿斯顿·马丁", "保时捷",
            "别克", "奔驰", "北汽制造", "北京", "本田", "特斯拉",
            "法拉利", "别克", "夏利", "奔腾", "兰博基尼"};

    String[] year = {"2015年", "2015年", "2015年", "2015年", "2014年", "2013年", "2013年", "2013年", "2012年"};

    public List<CommonLetterModel> getContactList() {
        List<CommonLetterModel> contacts = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < attr.length; i++) {
            CommonLetterModel contactModel = new CommonLetterModel();
            contactModel.setUser_id(i + "");
            contactModel.setUser_image("ii");
            String name = attr[i];
            contactModel.setUser_name(name);
//            String key = new ContactUtils().String2Alpha(name.substring(0, 1));

            String key = new ContactUtils().getPinYinHeadChar(name.substring(0, 1));
            key = key.toUpperCase();
            if (TextUtils.isEmpty(key)) {
                key = "#";
            }

            contactModel.setUser_key(key);
            contacts.add(contactModel);
        }
        return contacts;

    }




    public List<CommonLetterModel> getYearList() {
        List<CommonLetterModel> contacts = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < year.length; i++) {
            CommonLetterModel contactModel = new CommonLetterModel();
            contactModel.setUser_id(i + "");
            contactModel.setUser_image("");
            String name = "发动机";
            contactModel.setUser_name(name);

            contactModel.setUser_key(year[i]);
            contacts.add(contactModel);
        }
        return contacts;

    }

    String[] brands = {"国产", "美国进口","日本", "德国","上海","天津"};

    public List<CommonLetterModel> getBrandSeList() {
        List<CommonLetterModel> contacts = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < brands.length; i++) {
            CommonLetterModel contactModel = new CommonLetterModel();
            contactModel.setUser_id(i + "");
            contactModel.setUser_image("");
            String name = brands[i];
            contactModel.setUser_name(name);
//            String key = new ContactUtils().String2Alpha(name.substring(0, 1));

            String key = new ContactUtils().getPinYinHeadChar(name.substring(0, 1));
            key = key.toUpperCase();
            if (TextUtils.isEmpty(key)) {
                key = "#";
            }

            contactModel.setUser_key(key);
            contacts.add(contactModel);
        }
        return contacts;

    }


}
