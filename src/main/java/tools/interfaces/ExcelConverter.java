package tools.interfaces;

import enums.ExcelExtension;
import exceptions.ExtensionNotValidException;
import exceptions.FileAlreadyExistsException;
import exceptions.OpenWorkbookException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ExcelConverter {

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String filename) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String path, String filename) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String path, String filename, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String filename, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String path, String filename, ExcelExtension extension) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, ExcelExtension extension) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, ExcelExtension extension, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String filename, ExcelExtension extension) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String filename, ExcelExtension extension, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    File convertObjectsToExcelFile(List<?> objects, Class<?> clazz, String path, String filename, ExcelExtension extension, Boolean writeHeader) throws IllegalAccessException, IOException, FileAlreadyExistsException;

    List<?> convertExcelFileToObjects(File file, Class<?> clazz) throws ExtensionNotValidException, IOException, OpenWorkbookException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    List<?> convertExcelFileToObjects(File file, Class<?> clazz, String sheetName) throws ExtensionNotValidException, IOException, OpenWorkbookException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;
}
