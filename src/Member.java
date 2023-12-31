import java.util.ArrayList;

public class Member {

    protected String idNr;
    protected String name;
    protected String membershipDate;
    protected Boolean hasActiveMembership;
    protected ArrayList<String> workoutList = new ArrayList<>();


    public Member(ArrayList<String> personalData){
        idNr = personalData.get(0);
        name = personalData.get(1);
        membershipDate = personalData.get(2);
    }

    public String getIdNr() {
        return idNr;
    }

    public String getName() {
        return name;
    }

    public String getMembershipDate() {
        return membershipDate;
    }

    public Boolean getHasActiveMembership() {
        return hasActiveMembership;
    }

    public void setHasActiveMembership(Boolean status) {
        hasActiveMembership = status;
    }

    public ArrayList<String> getWorkoutList() {
        return workoutList;
    }

    public void setWorkoutDate(String dateOfGymVisit) {

        workoutList.add(dateOfGymVisit);
    }
}
