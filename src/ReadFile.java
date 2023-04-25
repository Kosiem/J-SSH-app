
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.io.FileInputStream;

public class ReadFile{
    String[][] data;
    int numRows;
    int numCols;

    ReadFile() throws IOException {
        File inputFile = new File("C:\\Users\\Patryk\\Desktop\\ExampleFile.xlsx");
        FileInputStream inpFl = new FileInputStream(inputFile);
        //System.out.println(new File("platformList.xlsx").getAbsoluteFile());
        XSSFWorkbook workbook = new XSSFWorkbook(inpFl);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int numRows = sheet.getLastRowNum() + 1;
        int numCols = sheet.getRow(0).getPhysicalNumberOfCells();

        this.data = new String[numRows][numCols];

        for(int i = 0; i < numRows; i++){
            XSSFRow row = sheet.getRow(i);
            for(int j = 0; j < numCols ; j++){
                XSSFCell cell = row.getCell(j);
                String cellValue = cell.getStringCellValue();
                data[i][j] = cellValue;

            }

        }


    }

    public String[][] getFileData(){
        System.out.print(data);
        return this.data;
    }
    public int getNumRows(){
        return this.numRows;
    }
    public int getNumCols(){
        return this.numCols;
    }

    public String getHostname(int i){
        return data[i][0].toString();
    }

    public String getIp(int i){
        return data[i][1].toString();
    }

}