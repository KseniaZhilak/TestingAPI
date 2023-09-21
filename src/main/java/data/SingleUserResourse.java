package data;


public class SingleUserResourse{
    private Support support;
    private DataUser data;

    /*@JsonProperty("data")
    private DataUser dataUser;*/

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public DataUser getData() {
        return data;
    }

    public void setData(DataUser data) {
        this.data = data;
    }
}
