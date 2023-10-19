import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    FileHandler fH = new FileHandler();
    TestLibrary tl = new TestLibrary();
    Path path = Paths.get("Test/TestPT.txt");

    @Test
    void createMemberTest() {
        String testNameAndPnr = "7502031234, Anna Andersson";
        String testDate = "2023-05-03";
        Member testMember = fH.createMember(testNameAndPnr, testDate);

        assert testMember != null;
        assertEquals("7502031234", testMember.getIdNr());
        assertEquals("Anna Andersson".toUpperCase(), testMember.getName().toUpperCase());
        assertEquals("2023-05-03", testMember.getMembershipDate());
    }

    @Test
    void createAllMemberTest() {
        ArrayList<String[]> stringList = new ArrayList<>();
        stringList.add(new String[]{"7502031234, Anna Andersson", "2023-05-03"});
        stringList.add(new String[]{"8505132345, Per Persson", "2019-12-29"});
        ArrayList<Member> memberList = fH.createAllMembers(stringList);

        assertEquals(2, memberList.size());
        assertEquals("Anna Andersson", memberList.get(0).getName());
        assertEquals("2019-12-29", memberList.get(1).getMembershipDate());
    }

    @Test
    void readsFromFileTest() {
        Path path = Paths.get("Test/TestData.txt");
        ArrayList<String[]> list = fH.readFromFile(path);

        assert (list.get(0) != null);
        assertEquals(2, list.size());
        assertEquals("7502031234, Anna Andersson", list.get(0)[0]);
        assertEquals("2019-12-29", list.get(1)[1]);
    }

    @Test
    void createFileTest(){
        fH.createFile(path);
        ArrayList<Member> list = tl.getTestMemberList();

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
    void readWorkOutHistoryTest() {
        ArrayList<Member> list = tl.getTestMemberList();
        Path path = Paths.get("Test/workoutHistoryTest.txt");
        Member testMember1 = list.get(0);
        assertTrue(testMember1.getWorkoutDates().isEmpty());
        fH.readWorkoutHistory(list, path);
        assertFalse(testMember1.getWorkoutDates().isEmpty());
    }

    @Test
    void writeCorrectDataToFileTest() {
        ArrayList<Member> list = tl.getTestMemberList();
        list.get(0).setWorkoutDate("2023-08-10");
        list.get(0).setWorkoutDate("2023-09-10");
        list.get(0).setWorkoutDate("2023-10-23");
        fH.writeDataToFile(list, path);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            assertEquals("7502031234, Anna Andersson", br.readLine());
            assertEquals("2023-08-10, 2023-09-10, 2023-10-23", br.readLine());
            assertNull(br.readLine());
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}