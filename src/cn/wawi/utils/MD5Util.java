package cn.wawi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
 
 
public class MD5Util {
 
    /*
     * ע��
     * 1�� Message Digest Algorithm MD5�����ⳤ�ȵġ��ֽڴ���ӳ��Ϊһ��128bit�Ĵ�������������ͨ����128bit����ԭʼ�ַ��������ѵ�
     * 2��2004��8��17�յ���������ʥ�Ͱ����Ĺ�������ѧ���飨Crypto��2004���ϣ������й�ɽ����ѧ����С�ƽ�����������MD5��HAVAL-128�� 
     * MD4��RIPEMD�㷨�ı��棬������MDϵ���㷨���ƽ����������˹�������������ͨ�������׼MD5�ı��ݺ�Ȼ������
     * ����������ѧ�����Ȼ�󲨡�(ע��:�������������ƽ⣬ֻ�Ǽ������Ӵճ�ײ��
     */
     
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        try {
            byte[] btInput = s.getBytes();
            // ���MD5ժҪ�㷨�� MessageDigest ����
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // ʹ��ָ�����ֽڸ���ժҪ
            mdInst.update(btInput);
            // �������
            byte[] md = mdInst.digest();
            // ������ת����ʮ�����Ƶ��ַ�����ʽ
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
     
   
}
