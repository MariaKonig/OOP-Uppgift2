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
    public Path readPath = Paths.get("Uppgift2/src/customers.txt");
    public Path writePath = Paths.get("Uppgift2/src/PT.txt");


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

    public ArrayList<Member> readFromFile(Path path) {

        String line1;
        String line2;
        Member member;
        ArrayList<Member> memberList = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while ((line1 = reader.readLine()) != null) {
                line2 = reader.readLine();
                member = createMember(line1, line2);
                memberList.add(member);
            }
        }
        catch(IOException e){
            System.out.println("Filen hittades inte");
        }
        return memberList;
    }
    public Member findMemberInList(String id, ArrayList<Member> list){
        id = id.trim();

        for(Member member : list){
            if(id.equals(member.getIdNr()) || id.equalsIgnoreCase(member.getName())){
                return member;
            }
        }
       return null;
    }

    public void setMembershipStatus(Member member, LocalDate dateNow){
        String signedDate = member.getMembershipDate();
        LocalDate membershipDate = LocalDate.parse(signedDate);
        Boolean status = getIsLessThanAYear(membershipDate,dateNow);
        member.setHasActiveMembership(status);
    }

    public String personSearch(String id, ArrayList<Member> list,LocalDate dateNow){
        Member member = findMemberInList(id,list);
        if(member==null){
            return "obehörig";
        }
        setMembershipStatus(member,dateNow);
        if(!member.getHasActiveMembership()){
            return "f.d. kund";
        }else{
            member.setWorkOutDates(dateNow.toString());
            return "kund";
        }
    }
    public void createFile(Path path){

        if(!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeDataToFile(ArrayList<Member> list, Path path){
        createFile(path);

        try(BufferedWriter bw = Files.newBufferedWriter(path)){
            for(Member member : list){
                if(!(member.getWorkOutDates().isEmpty())){
                    bw.write(member.getIdNr());
                    bw.write(", ");
                    bw.write(member.getName());
                    bw.write("\n");
                    for(String date : member.getWorkOutDates()){
                        bw.write(date);
                        bw.write("\n");
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public String readInput(String OptionalText)throws IllegalArgumentException{
        String input;
        if(test){
            input = OptionalText;
        }
        else{
            input = JOptionPane.showInputDialog(null,"Besökare: Namn Efternamn / personnr ÅÅÅÅMMDD");
        }
        if(!isValidInput(input)){
            throw new IllegalArgumentException();
        }
        return input.trim();
    }

    public Boolean isValidInput(String input){
        input = input.trim();
       if(input.length()==10 && Character.isDigit(input.charAt(0))){
           return true;
       } else if (input.contains(" ") && Character.isLetter(input.charAt(0))){
           return true;
       }else{
           return false;
       }
    }


    public String getInput(){
        String input="";
        try{
             input = readInput("");

        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "Ogiltigt format på indata");
        }
        return input.trim();
    }


    public void run(){
        ArrayList<Member> members = readFromFile(readPath);
        String input = getInput();
        String output = personSearch(input, members, LocalDate.now());
        JOptionPane.showMessageDialog(null,output);
        writeDataToFile(members, writePath);
    }

    public static void main(String[] args) {
        Membership today = new Membership();
        today.run();
    }
}
