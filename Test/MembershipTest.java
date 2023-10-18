import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {
    Path path = Paths.get("Test/TestPT.txt");
    Membership m = new Membership();

    public ArrayList<Member> getTestMemberList() {
        ArrayList<Member> membersList = new ArrayList<>();

        ArrayList<String> arrList1 = new ArrayList<>();
        ArrayList<String> arrList2 = new ArrayList<>();
        arrList1.add("7502031234");
        arrList1.add("Anna Andersson");
        arrList1.add("2023-05-03");
        arrList2.add("8505132345");
        arrList2.add("Per Persson");
        arrList2.add("2019-12-29");
        Member m1 = new Member(arrList1);
        Member m2 = new Member(arrList2);
        membersList.add(m1);
        membersList.add(m2);
        return membersList;
    }


    @Test
    void lessThanAYearTest() {
        LocalDate compareDate = LocalDate.of(1990, 11, 29);
        LocalDate dateLessThanAYear = LocalDate.of(1991, 11, 28);
        LocalDate dateMoreThanAYear = LocalDate.of(1991, 11, 30);

        assertEquals(true, m.getIsLessThanAYear(compareDate, dateLessThanAYear));
        assertEquals(false, m.getIsLessThanAYear(compareDate, dateMoreThanAYear));
        assertEquals(true, m.getIsLessThanAYear(compareDate, compareDate));
    }

    @Test
    void createMemberTest() {
        String testNameAndIdnr = "7502031234, Anna Andersson";
        String testDate = "2023-05-03";
        Member testMember = m.createMember(testNameAndIdnr, testDate);

        assert testMember != null;
        assertEquals("7502031234", testMember.getIdNr());
        assertEquals("Anna Andersson".toUpperCase(), testMember.getName().toUpperCase());
        assertEquals("2023-05-03", testMember.getMembershipDate());

    }

    @Test
    void readsFromFileTest() {
        Path path = Paths.get("Test/TestData.txt");
        ArrayList<Member> list = m.readFromFile(path);

        assert (list.get(0) != null);
        assertEquals(2, list.size());
        assertEquals("Anna Andersson", list.get(0).getName());
        assertEquals("2019-12-29", list.get(1).getMembershipDate());

    }

    @Test
    void findMemberInTestList() {
        ArrayList<Member> list = getTestMemberList();
        Member m1 = m.findMemberInList("Anna Andersson", list);
        Member m2 = m.findMemberInList("8505132345", list);
        assertEquals("7502031234", m1.getIdNr());
        assertEquals("Per Persson", m2.getName());
    }

    @Test
    void setMembershipStatusTest() {
        ArrayList<Member> list = getTestMemberList();
        Member active = list.get(0); //"2023-05-03"
        Member expired = list.get(1); //"2019-12-29"
        LocalDate testDate = LocalDate.of(2023, 7, 27);

        assertNull(active.getHasActiveMembership());
        assertNull(expired.getHasActiveMembership());

        m.setMembershipStatus(active, testDate);
        m.setMembershipStatus(expired, testDate);

        assertEquals(true, active.getHasActiveMembership());
        assertEquals(false, expired.getHasActiveMembership());
    }

    @Test
    void testSetDaysOfWorkOutToList() {
        ArrayList<Member> list = getTestMemberList();
        list.get(0).setWorkOutDates(LocalDate.now().toString());
        ArrayList<String> workOutlist = list.get(0).getWorkOutDates();
        assertEquals(LocalDate.now().toString(), workOutlist.get(0));
    }

    @Test
    void personSearchOutputTest() {
        ArrayList<Member> list = getTestMemberList();
        LocalDate testDate = LocalDate.of(2023, 7, 27);
        String search1 = "Anna Andersson";
        String search2 = "Per Persson";
        String search3 = "Pelle Snyltare";
        String search4 = "Anna";
        assertEquals("kund", m.personSearch(search1, list, testDate));
        assertEquals("f.d. kund", m.personSearch(search2, list, testDate));
        assertEquals("obehörig", m.personSearch(search3, list, testDate));
        assertEquals("obehörig", m.personSearch(search4, list, testDate));
    }

    @Test
    void createFiletest() throws Exception {
        m.createFile(path);
        ArrayList<Member> list = getTestMemberList();

        try (BufferedReader br = Files.newBufferedReader(path);
             BufferedWriter bw = Files.newBufferedWriter(path)) {

            String text = list.get(0).getMembershipDate();
            bw.write(text);
            bw.close();
            String textInFile = br.readLine();
            assertEquals("2023-05-03", textInFile);
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void writeCorrectDataToFileTest(){
        ArrayList<Member> list = getTestMemberList();
        list.get(0).setWorkOutDates("2023-08-10");
        list.get(0).setWorkOutDates("2023-09-10");
        list.get(0).setWorkOutDates("2023-10-23");
        m.writeDataToFile(list,path);
        try (BufferedReader br = Files.newBufferedReader(path)){
            assertEquals( "7502031234, Anna Andersson",br.readLine());
            assertEquals("2023-08-10",br.readLine());
            assertEquals("2023-09-10",br.readLine());
            assertEquals("2023-10-23",br.readLine());
            assertNull(br.readLine());
            Files.delete(path);
        }
        catch(IOException e) {
           e.printStackTrace();
        }
    }
    @Test
    void inputReadsCorrectTest()throws Exception{
        m.test = true;
        String testInput = "anna andersson";
        String testInputWithSpace = "  7502031234  ";
        assertEquals("anna andersson", m.readInput(testInput));
        assertEquals("7502031234", m.readInput(testInputWithSpace));
    }

    @Test
    void readInputThrowsExceptionTest()throws Exception{
        m.test = true;
        String testInputWithInvalidCharacter = "@nn@ Andersson";
        String testInputTooManyNumbers = "75020312340";
        String testInputWrongFormat = "750203-1234";

        assertThrows(IllegalArgumentException.class,()->m.readInput(testInputWithInvalidCharacter));
        assertThrows(IllegalArgumentException.class,()->m.readInput(testInputTooManyNumbers));
        assertThrows(IllegalArgumentException.class,()->m.readInput(testInputWrongFormat));
    }
}