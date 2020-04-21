package my.little.abtranslater.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ToWord {
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<HashMap<String, String>> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(ArrayList<HashMap<String, String>> trans_result) {
        this.trans_result = trans_result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
    public String toString(){
        String string ="from:"+this.from+" to:"+this.to+" error_code:"+this.error_code;
        if(trans_result!=null) {
            for (HashMap hashMap : trans_result) {
                string = string + hashMap.toString();
            }
        }
        return string;
    }

    private String from;
    private String to;
    private ArrayList<HashMap<String,String>> trans_result;
    private String error_code;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    private String error_msg;
}
