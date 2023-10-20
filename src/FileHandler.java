import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHandler {
    public Path memberFile = Paths.get("Uppgift2/src/customers.txt");
    public Path ptFile = Paths.get("Uppgift2/src/PT.txt");
    Membership m = new Membership();

    public ArrayList<String[]> readFromFile(Path path) {

        String line1;
        String line2;
        ArrayList<String[]> list = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while ((line1 = reader.readLine()) != null) {
                line2 = reader.readLine();
                list.add(new String[]{line1, line2});
            }
        } catch (IOException e) {
            System.out.println("Filen hittades inte");
        }
        return list;
    }

    public ArrayList<Member> createAllMembers(ArrayList<String[]> stringList) {
        ArrayList<Member> memberList = new ArrayList<>();

        for (String[] s : stringList) {
            memberList.add(createMember(s[0], s[1]));
        }
        return memberList;
    }

    public Member createMember(String personData, String date) {

        ArrayList<String> member = new ArrayList<>();
        String[] placeholder = personData.split(",");
        member.add(placeholder[0].trim());
        member.add(placeholder[1].trim());
        member.add(date.trim());
        return new Member(member);
    }
    //Samling-metod
    public ArrayList<Member> getMembersFromFile(){
        ArrayList<String[]> arrayList = readFromFile(memberFile);
        ArrayList<Member> members = createAllMembers(arrayList);
        setWorkoutHistory(members, ptFile);
        return members;
    }

    public void setWorkoutHistory(ArrayList<Member> memberList, Path path) {

        if (Files.exists(path)) {
            ArrayList<String[]> workOutData = readFromFile(path);

            for (String[] memberWorkout : workOutData) {
                String[] personalInfo = memberWorkout[0].split(",");
                String[] workoutDates = memberWorkout[1].split(",");
                Member member = m.findMemberInList(personalInfo[0], memberList);
                for (String date : workoutDates) {
                    member.setWorkoutDate(date);
                }
            }
        }
    }

    public void createFile(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToPTFile(ArrayList<Member> members){
        writeDataToFile(members, ptFile);
    }

    public void writeDataToFile(ArrayList<Member> list, Path path) {
        createFile(path);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Member member : list) {
                if (!(member.getWorkoutList().isEmpty())) {
                    bw.write(member.getIdNr());
                    bw.write(", ");
                    bw.write(member.getName());
                    bw.write("\n");
                    for (int i = 0; i < member.getWorkoutList().size(); i++) {
                        String date = member.getWorkoutList().get(i);
                        bw.write(date);
                        if (i < member.getWorkoutList().size() - 1) {
                            bw.write(", ");
                        } else {
                            bw.write("\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}