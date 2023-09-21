package data;

public class PeopleCreated extends People{

    private String createdAt;
    private String id;

    public PeopleCreated() {super();}

    public PeopleCreated(String name, String job, String createdAt, String id) {
        super(name, job);
        this.createdAt = createdAt;
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name){
        super.setName(name);
    }
    public void setJob(String job){
        super.setJob(job);
    }
    public String getName() {
        return super.getName();
    }
    public String getJob() {
        return super.getJob();
    }
}
