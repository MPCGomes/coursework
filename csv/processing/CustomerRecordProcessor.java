package processing;

import files.model.CustomerRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class CustomerRecordProcessor {

    public static void main(String[] args) throws Exception {
        String inputFilePath = "dados.csv";
        String outputFilePath = "dados_ordenados.txt";

        ArrayList<CustomerRecord> recordList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath));

        String header = bufferedReader.readLine();
        String csvLine = bufferedReader.readLine();

        while (csvLine != null) {
            CustomerRecord customerRecord = createCustomerRecordFromCsvLine(csvLine);
            recordList.add(customerRecord);
            csvLine = bufferedReader.readLine();
        }

        bufferedReader.close();

        recordList.sort((recordOne, recordTwo) ->
                recordOne.getName().compareToIgnoreCase(recordTwo.getName()));

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));

        bufferedWriter.write(header);
        bufferedWriter.newLine();

        for (CustomerRecord customerRecord : recordList) {
            bufferedWriter.write(customerRecord.toCsv());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
    }

    private static CustomerRecord createCustomerRecordFromCsvLine(String csvLine) {
        String[] fields = csvLine.split(",", -1);

        return new CustomerRecord(
                fields[0],
                fields[1],
                fields[2],
                fields[3],
                fields[4],
                fields[5],
                fields[6],
                fields[7],
                fields[8],
                fields[9],
                fields[10],
                fields[11],
                fields[12],
                fields[13],
                fields[14],
                fields[15],
                fields[16],
                fields[17],
                fields[18],
                fields[19],
                fields[20],
                fields[21],
                fields[22]
        );
    }
}
