package data;

public class PeopleUpdate extends People{
    private String updatedAt;

    public PeopleUpdate(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public PeopleUpdate() {super();}

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
