package tools;

import enums.ExcelExtension;
import exceptions.ExtensionNotValidException;
import exceptions.OpenWorkbookException;
import exceptions.SheetNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtilsImpl implements ExcelUtils {

    @Override
    public Integer countAllRows(File file) throws ExtensionNotValidException, IOException, OpenWorkbookException, SheetNotFoundException {
        return countAllRows(file, true, null);
    }

    @Override
    public Integer countAllRows(File file, Boolean alsoEmptyRows) throws ExtensionNotValidException, IOException, OpenWorkbookException, SheetNotFoundException {
        return countAllRows(file, alsoEmptyRows, null);
    }

    @Override
    public Integer countAllRows(File file, Boolean alsoEmptyRows, String sheetName) throws ExtensionNotValidException, IOException, OpenWorkbookException, SheetNotFoundException {

        /* Check extension */
        String extension = FilenameUtils.getExtension(file.getName());
        if(!isValidExcelExtension(extension)) {
            throw new ExtensionNotValidException("Pass a file with the XLS or XLSX extension");
        }

        /* Open file excel */
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = openWorkbook(fileInputStream, extension);
        Sheet sheet = (sheetName == null || sheetName.isEmpty())
                ? workbook.getSheetAt(0)
                : workbook.getSheet(sheetName);

        if(sheet == null) {
            throw new SheetNotFoundException("No sheet was found");
        }

        /* Count all rows */
        int numRows = alsoEmptyRows
                ? sheet.getPhysicalNumberOfRows()
                : countOnlyRowsNotEmpty(sheet);

        /* Close file */
        fileInputStream.close();
        workbook.close();

        return numRows;
    }

    @Override
    public Workbook openWorkbook(FileInputStream fileInputStream, String extension) throws ExtensionNotValidException, IOException, OpenWorkbookException {

        /* Check the extension */
        if(!isValidExcelExtension(extension)) {
            throw new ExtensionNotValidException("Pass a file with the XLS or XLSX extension");
        }

        /* Open workbook */
        try {
            return new XSSFWorkbook(fileInputStream);
        } catch (OfficeXmlFileException | OLE2NotOfficeXmlFileException e) {
            try {
                return new HSSFWorkbook(fileInputStream);
            } catch (NotOLE2FileException ex) {
                throw new OpenWorkbookException("The workbook could not be opened", ex);
            }
        }
    }

    @Override
    public Workbook createWorkbook() {
        return createWorkbook(ExcelExtension.XLSX);
    }

    @Override
    public Workbook createWorkbook(String extension) throws ExtensionNotValidException {
        if(!isValidExcelExtension(extension)) {
            throw new ExtensionNotValidException("Pass a file with the XLS or XLSX extension");
        }
        return createWorkbook(ExcelExtension.getExcelExtension(extension));
    }

    @Override
    public Workbook createWorkbook(ExcelExtension extension) {
        Workbook workbook = null;
        switch (extension) {
            case XLS -> workbook = new HSSFWorkbook();
            case XLSX -> workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    @Override
    public Boolean isValidExcelExtension(String extension) {
        return extension.equalsIgnoreCase(ExcelExtension.XLS.getExt()) || extension.equalsIgnoreCase(ExcelExtension.XLSX.getExt());
    }

    @Override
    public Integer countAllSheets(File file) throws ExtensionNotValidException, IOException, OpenWorkbookException {
        /* Check extension */
        String extension = FilenameUtils.getExtension(file.getName());
        if(!isValidExcelExtension(extension)) {
            throw new ExtensionNotValidException("Pass a file with the XLS or XLSX extension");
        }

        /* Open file excel */
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = openWorkbook(fileInputStream, extension);

        Integer totalSheets = workbook.getNumberOfSheets();

        /* Close file */
        fileInputStream.close();
        workbook.close();

        return totalSheets;
    }

    @Override
    public List<String> getAllSheetNames(File file) throws ExtensionNotValidException, IOException, OpenWorkbookException {

        /* Check extension */
        String extension = FilenameUtils.getExtension(file.getName());
        if(!isValidExcelExtension(extension)) {
            throw new ExtensionNotValidException("Pass a file with the XLS or XLSX extension");
        }

        /* Open file excel */
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = openWorkbook(fileInputStream, extension);

        /* Iterate all the sheets */
        Iterator<Sheet> sheetIterator = workbook.iterator();
        List<String> sheetNames = new LinkedList<>();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            sheetNames.add(sheet.getSheetName());
        }

        /* Close file */
        fileInputStream.close();
        workbook.close();

        return sheetNames;
    }

    private int countOnlyRowsNotEmpty(Sheet sheet) {
        int numRows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            boolean isEmptyRow = true;

            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                Cell cell = row.getCell(j);

                if (cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                    isEmptyRow = false;
                    break;
                }
            }

            if(isEmptyRow) {
                numRows--;
            }
        }

        return numRows;
    }
}
