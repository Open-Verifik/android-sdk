package co.mat.verifikkit;

class HttpUtils {
    static String parseProdKeyText(String prodKeyText){
        prodKeyText = prodKeyText.replace("{","");
        prodKeyText = prodKeyText.replace("}","");
        prodKeyText = prodKeyText.replace(",","\n");
        prodKeyText = prodKeyText.replace("_",",");
        prodKeyText = prodKeyText.replace("\"","");
        prodKeyText = prodKeyText.replace(":","=");
        return prodKeyText;
    }
}
