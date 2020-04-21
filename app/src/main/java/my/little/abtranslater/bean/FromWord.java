package my.little.abtranslater.bean;

public class FromWord {
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

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
    public FromWord(String q,String f,String t){
        this.query = q;
        this.from = f;
        this.to = t;
    }
    public FromWord(String q,String to){
        this.query = q;
        this.to = to;
        this.from = "auto";
    }
    public String toString(){
        return "query:"+query+" from:"+from+" to"+to;
    }

    private String query;
    private String from;
    private String to;
}
