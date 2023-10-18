import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;


public class Membership {

    public Boolean test = false;
    public Path memberFile = Paths.get("Uppgift2/src/customers.txt");
    public Path PTFile = Paths.get("Uppgift2/src/PT.txt");


    public Boolean getIsLessThanAYear(LocalDate fromDate, LocalDate toDate) {

        Period p = Period.between(fromDate, toDate);
        Period exactlyAYear = Period.of(1, 0, 0);
        return p.getYears() == 0 || p.equals(exactlyAYear);
    }

    public Member createMember(String personData, String date) {

        ArrayList<String> member = new ArrayList<>();
        String[] placeholder = personData.split(",");
        member.add(placeholder[0].trim());
        member.add(placeholder[1].trim());
        member.add(date.trim());
        return new Member(member);
    }

    public ArrayList<Member> createAllMembers(ArrayList<String[]> stringList) {
        ArrayList<Member> memberList = new ArrayList<>();
        for (String[] s : stringList) {
            memberList.add(createMember(s[0], s[1]));
        }
        return memberList;
    }

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

    public Member findMemberInList(String id, ArrayList<Member> list) {
        id = id.trim();

        for (Member member : list) {
            if (id.equals(member.getIdNr()) || id.equalsIgnoreCase(member.getName())) {
                return member;
            }
        }
        return null;
    }

    public void setMembershipStatus(Member member, LocalDate toDate) {
        String stringFromDate = member.getMembershipDate();
        LocalDate fromDate = LocalDate.parse(stringFromDate);
        Boolean status = getIsLessThanAYear(fromDate, toDate);
        member.setHasActiveMembership(status);
    }

    public String personSearch(String id, ArrayList<Member> list, LocalDate today) {
        Member member = findMemberInList(id, list);
        if (member == null) {
            return "obehörig";
        }
        setMembershipStatus(member, today);
        if (!member.getHasActiveMembership()) {
            return "f.d. kund";
        } else {
            member.setWorkoutDate(today.toString());
            return "kund";
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

    public void writeDataToFile(ArrayList<Member> list, Path path) {
        createFile(path);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Member member : list) {
                if (!(member.getWorkoutDates().isEmpty())) {
                    bw.write(member.getIdNr());
                    bw.write(", ");
                    bw.write(member.getName());
                    bw.write("\n");
                    for (int i = 0; i < member.getWorkoutDates().size(); i++) {
                        String date = member.getWorkoutDates().get(i);
                        bw.write(date);
                        if (i < member.getWorkoutDates().size() - 1) {
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

    public String readInput(String OptionalText) {
        String input = "";
        if (test) {
            input = OptionalText;
        } else {
            input = JOptionPane.showInputDialog("Besökare: Namn Efternamn / personNr ÅÅÅÅMMDD");
        }
        if (input == null) {
            return null;
        }
        if (!isValidInput(input)) {
            return "Ogiltigt format";
        }
        return input.trim();
    }

    public Boolean isValidInput(String input) {
        input = input.trim();
        if (input.length() == 10 && Character.isDigit(input.charAt(0))) {
            return true;
        } else return input.contains(" ") && Character.isLetter(input.charAt(0));
    }


    public void readWorkoutHistory(ArrayList<Member> memberList, Path path) {

        if (Files.exists(path)) {
            ArrayList<String[]> workOutData = readFromFile(path);

            for (String[] memberWorkout : workOutData) {
                String[] personalInfo = memberWorkout[0].split(",");
                String[] workoutDates = memberWorkout[1].split(",");
                Member member = findMemberInList(personalInfo[0], memberList);
                for (String date : workoutDates) {
                    member.setWorkoutDate(date);
                }
            }
        }
    }

    public void run() {

        ArrayList<String[]> arrayList = readFromFile(memberFile);
        ArrayList<Member> members = createAllMembers(arrayList);
        readWorkoutHistory(members, PTFile);
        while (true) {
            String input = readInput("");
            if (input == null) {
                break;
            } else if (input.equals("Ogiltigt format")) {
                JOptionPane.showMessageDialog(null, input);
                continue;
            }
            String output = personSearch(input, members, LocalDate.now());
            JOptionPane.showMessageDialog(null, output);
        }
        writeDataToFile(members, PTFile);
    }

    public static void main(String[] args) {
        Membership today = new Membership();
        today.run();
    }
}