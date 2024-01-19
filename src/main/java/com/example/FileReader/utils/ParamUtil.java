package com.example.FileReader.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamUtil {
    public static Object get_object(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return null;
        return val;
    }

    public static String get_string(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return null;
        return val.toString().trim();
    }

    public static List<Long> get_list_long(Map<String, Object> params, String key, boolean allow_null) {
        List<Object> vals = (List<Object>) params.get(key);
        if (!allow_null && vals == null)
            throw new RuntimeException("Request param " + key + " can't be null!");

        if (vals == null)
            return null;

        List<Long> longValues = new ArrayList<>();
        for (Object val : vals) {
            if (val instanceof Long) {
                longValues.add((Long) val);
            } else if (val instanceof Integer) {
                longValues.add(((Integer) val).longValue());
            } else {
                throw new RuntimeException("Invalid data type in request param " + key);
            }
        }
        return longValues;
    }
    public static List<Integer> get_list_int(Map<String, Object> params, String key, boolean allow_null) {
        List<Object> vals = (List<Object>) params.get(key);

        if (!allow_null && vals == null)
            throw new RuntimeException("Request param " + key + " can't be null!");

        if (vals == null)
            return null;

        List<Integer> intValues = new ArrayList<>();
        for (Object val : vals) {
            if (val instanceof Integer) {
                intValues.add((Integer) val);
            } else if (val instanceof Long) {
                intValues.add(((Long) val).intValue());
            } else {
                throw new RuntimeException("Invalid data type in request param " + key);
            }
        }
        return intValues;
    }


    public static List<String> get_list_string(Map<String, Object> params, String key, boolean allowNull) {
        List<Object> values = (List<Object>) params.get(key);

        if (!allowNull && values == null)
            throw new RuntimeException("Request param " + key + " can't be null!");

        if (values == null)
            return null;

        List<String> stringValues = new ArrayList<>();
        for (Object value : values) {
            if (value instanceof String) {
                stringValues.add((String) value);
            } else {
                throw new RuntimeException("Invalid data type in request param " + key);
            }
        }
        return stringValues;
    }

    public static String get_string_trim(Map<String, Object> params, String key) {
        Object val = params.get(key);
        if (val == null)
            return "";
        return val.toString().trim();
    }

//    public static int get_int(Map<String, Object> params, String key, boolean allow_null) {
//        Object val = params.get(key);
//        if (!allow_null && val == null)
//            throw new RuntimeException(" Request param " + key + " can't be null!");
//        if (val == null)
//            return 0;
//        return Integer.parseInt(val.toString().trim());
//    }

    public static Integer get_int(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return null;
        return Integer.parseInt(val.toString().trim());
    }

    public static Long get_long(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return null;
        return Long.parseLong(val.toString().trim());
    }

    public static float get_float(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return 0.0f;
        return Float.parseFloat(val.toString().trim());
    }

    public static double get_double(Map<String, Object> params, String key, boolean allow_null) {
        Object val = params.get(key);
        if (!allow_null && val == null)
            throw new RuntimeException(" Request param " + key + " can't be null!");
        if (val == null)
            return 0.0;
        return Double.parseDouble(val.toString().trim());
    }

    public static String get_lang(HttpServletRequest request) {
        String lang = request.getHeader("lang");
        if (lang == null)
            lang = "kk";
        return lang;
    }

    public static String get_request_body(HttpServletRequest request) {
       /* try {

            if (request != null) {
                String submitMehtod = request.getMethod();
                if (submitMehtod.equals("GET")) {
                    return new String(request.getQueryString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).replaceAll("%22", "\"");
                } else {
                    return getRequestPostStr(request);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
        return null;
    }

    public static String getAuthInfo(HttpServletRequest request) {
        try {
            Map<String, String> jwt_payload = (Map<String, String>) request.getAttribute("jwt_payload");
            if (jwt_payload != null) {
                String uid = jwt_payload.get("uid");
                String user_type = jwt_payload.get("user_type");
                String result = null;
                if (uid != null)
                    result = "uid:" + uid + ",";
                if (user_type != null)
                    result += "user_type:" + user_type;
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String request_info_has_log(String log) {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = null;
            if (servletRequestAttributes != null)
                request = servletRequestAttributes.getRequest();
            if (request != null) {
                String auth_info = getAuthInfo(request);
                String request_body = get_request_body(request);
                if (auth_info == null)
                    auth_info = "null";
                if (request_body == null)
                    request_body = "null";

                return "AuthInfo:" + auth_info + ",LogMsg:" + log + ",RequestBody:" + request_body;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        if (buffer == null)
            return null;
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

}
