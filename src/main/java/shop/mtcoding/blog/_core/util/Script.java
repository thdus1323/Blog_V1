package shop.mtcoding.blog._core.util;

public class Script {
    public static String back(String msg) {
        //에러 alert를 보내
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }

    //해당 url로 가게 해
    public static String href(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("location.href='" + path + "';");
        sb.append("</script>");
        return sb.toString();
    }

    //에러 alert와 해당 url로 가게 해
    public static String href(String path, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("location.href='" + path + "';");
        sb.append("</script>");
        return sb.toString();
    }
}
