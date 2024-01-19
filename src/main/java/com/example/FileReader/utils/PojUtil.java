package com.example.FileReader.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class PojUtil {

    private static final Logger log = LoggerFactory.getLogger(PojUtil.class);
    private static final String hi = "f7T8glE(M9bS6dLt";

    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());

            return byte2Hex(secretBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public  static String aes_enc(String value,String pass)
    {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(hi.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(pass.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return base64_encode(encrypted);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";


    }

    public  static String aes_des(String value,String pass)
    {
        try {

            IvParameterSpec ivParameterSpec = new IvParameterSpec(hi.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(pass.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(base64_decode(value));

            return new String(decrypted);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";

    }


    public static String dateFormat(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(date);
    }


    public static String String2SHA256Str(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static String base64_encode(byte[] data)
    {
        Base64 base64 = new Base64();
        return new String(base64.encode(data));
    }

    public static byte[] base64_decode(String bstr)
    {
        Base64 base64 = new  Base64();
        return base64.decode(bstr.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateRandomString(int len) {
        String chars = "1234567890-=!@#$%^&*()_+qwertyuiop[]QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:zxcvbnm,./ZXCVBNM<>?";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public static String format_kz_phone(String phone)
    {
        if(phone==null)
            return phone;
        phone= phone.replaceAll("\\s","");
        if(phone.startsWith("8"))
        {
            phone=phone.substring(1);
            phone="+7"+phone;

        }
        if(!phone.startsWith("+"))
            phone="+7"+phone;
        return phone;
    }

    public static String random_number(int len)
    {
        String chars = "1234567890";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

/*    public static String password_hash(String password)
    {
        return md5(password);
    }*/


    public static boolean password_check(String password)
    {
        return password != null && password.length() >= 6 && password.length() <= 32;
    }

    public static String random_uuid()
    {
        return UUID.randomUUID().toString();
    }

    public static Date format2date(String str_date)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return  simpleDateFormat.parse(str_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timestamp_sec_to_str(long timestamp)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(timestamp*1000);
        return simpleDateFormat.format(date);
    }

    public static Date timestamp_sec_to_date(long timestamp)
    {
        return new Date(timestamp*1000);
    }

    public static String timeid()
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");

        return simpleDateFormat.format(new Date());
    }

    public static boolean inarray(String src,String[] array)
    {
        for(int i=0;i<array.length;++i)
            if(src.equals(array[i]))
                return true;
        return false;
    }

    public static String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }

    public static boolean email_format_is_valid(String email)
    {
        if(email==null || email.isEmpty())
            return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static boolean phone_format_is_valid(String phone)
    {
        phone = format_kz_phone(phone);

        if(kazakh_phone_format_is_valid(phone)||china_phone_format_is_valid(phone))
            return true;
        return false;
    }

    private static boolean china_phone_format_is_valid(String phone) {
        if (phone == null || phone.isEmpty())
            return false;
        char[] nums = phone.toCharArray();
        if (nums[0] != '+')
            return false;

        if (!Pattern.matches("^((\\+86)|(86))?(1)\\d{10}$",phone))
            return false;

        return true;
    }
    public static boolean kazakh_phone_format_is_valid(String phone)
    {
        if(phone==null || phone.isEmpty())
            return false;
        char[] nums=phone.toCharArray();
        if(nums[0]!='+')
            return false;

        if (!Pattern.matches("^\\+?77(\\d{9})$",phone))
            return false;
        return true;

    }

    public static String sha256password_hash(String password)
    {
        String generatedPassword = null;
        String salt="be44361a-96c2-4edd-8789-9d0b1593af61";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String PBKDF2_hash(String password,String salt,int itercount)
    {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), itercount, 20 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return PojUtil.base64_encode(hash);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean check_tracecode(String tracecode)
    {
        if(tracecode==null) return false;
        tracecode=tracecode.trim();
        //^[a-zA-Z0-9]*$
        return Pattern.matches("^[a-zA-Z0-9]*$", tracecode);
    }

    public static boolean check_tncode(String tncode)
    {
        if(tncode==null) return false;
        tncode=tncode.trim();
        if(tncode.isEmpty())
            return false;
        //^[a-zA-Z0-9]*$
        return Pattern.matches("^[a-zA-Z0-9]*$", tncode);
    }



    public static float random(float begin,float end)
    {
        Random r=new Random();
        return r.nextFloat()*(end-begin)+begin;
    }


    public static <O> O clone(O o,Class<O> clazz)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            O deepCopy = objectMapper
                    .readValue(objectMapper.writeValueAsString(o), clazz);
            return deepCopy;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static float orderWeightCeil(float weight){
        return (float)Math.ceil(weight * 10) / 10.0f;
    }

    public static double orderWeightCeil2(double weight, int step){
        weight *= 1000;
        long remainder = (long)(weight % 1000);
        if(remainder % step != 0)
            weight += step - (remainder % step);
        return weight/1000;
    }

    public static float getWeightPrice()
    {
        return 4.55f;
    }


    private boolean isNullOrEmpty(Object obj)
    {
        if(obj==null)
            return true;
        return obj.toString().trim().isEmpty();
    }


    public static List<String> getAllUrlRequestMapping(ConfigurableApplicationContext run) {

        List<String> result=new ArrayList<>();

        String[] beanNamesForAnnotation = run.getBeanNamesForAnnotation(RestController.class);
        for (String str : beanNamesForAnnotation) {
            Object bean = run.getBean(str);
            Class<?> forName = bean.getClass();
            RequestMapping declaredAnnotation = AnnotationUtils.findAnnotation(forName,RequestMapping.class);

            String url_path = "";
            if (declaredAnnotation != null) {
                String[] value = (declaredAnnotation.value());
                //获取类的url路径
                url_path = value[0];
                for (Method method : forName.getDeclaredMethods()) {
                    RequestMapping annotation2 = AnnotationUtils.findAnnotation(method,RequestMapping.class);
                    if (annotation2 != null && annotation2.value().length >= 1) {
                        url_path += annotation2.value()[0];
                        result.add(url_path);
                    }
                    url_path = value[0];
                }
            }
        }

        return result;
    }


    public static List<String> getAllUrlGetMapping(ConfigurableApplicationContext run) {
        List<String> result=new ArrayList<>();
        String[] beanNamesForAnnotation = run.getBeanNamesForAnnotation(RestController.class);
        for (String str : beanNamesForAnnotation) {
            Object bean = run.getBean(str);
            Class<?> forName = bean.getClass();
            RequestMapping declaredAnnotation = AnnotationUtils.findAnnotation(forName,RequestMapping.class);
            String url_path = "";
            if (declaredAnnotation != null) {
                String[] value = (declaredAnnotation.value());
                //获取类的url路径
                url_path = value[0];
                for (Method method : forName.getDeclaredMethods()) {
                    GetMapping annotation2 = AnnotationUtils.findAnnotation(method,GetMapping.class);
                    if (annotation2 != null && annotation2.value().length >= 1) {
                        url_path += annotation2.value()[0];
                        result.add(url_path);
                    }
                    url_path = value[0];
                }
            }
        }

        return result;
    }



    public static List<String> getAllUrlPostMapping(ConfigurableApplicationContext run) {
        List<String> result=new ArrayList<>();
        String[] beanNamesForAnnotation = run.getBeanNamesForAnnotation(RestController.class);
        for (String str : beanNamesForAnnotation) {
            Object bean = run.getBean(str);
            Class<?> forName = bean.getClass();
            RequestMapping declaredAnnotation = AnnotationUtils.findAnnotation(forName,RequestMapping.class);
            String url_path = "";
            if (declaredAnnotation != null) {
                String[] value = (declaredAnnotation.value());
                //获取类的url路径
                url_path = value[0];
                for (Method method : forName.getDeclaredMethods()) {
                    PostMapping annotation2 = AnnotationUtils.findAnnotation(method,PostMapping.class);
                    if (annotation2 != null && annotation2.value().length >= 1) {
                        url_path += annotation2.value()[0];
                        result.add(url_path);
                    }
                    url_path = value[0];
                }
            }
        }

        return result;
    }


    public static String toString(Object obj)
    {
        if(obj==null)
            return "null";
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
