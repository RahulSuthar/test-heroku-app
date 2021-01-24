package com.smallbazaar.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.smallbazaar.rest.dto.ExcelProductData;

public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERS = { "Name", "Product Type", "Category", "Amount","Quantity","Description","Notes" };
	static String SHEET = "Product_Template_V1";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<ExcelProductData> excelToTutorials(InputStream is) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<ExcelProductData> tutorials= new ArrayList<ExcelProductData>();
			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				ExcelProductData excelData = new ExcelProductData();

				int cellIndex = 0;

				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIndex) {
					case 0:
						excelData.setName(currentCell.getStringCellValue());
						break;

					case 1:
						excelData.setType(currentCell.getStringCellValue());
						break;

					case 2:
						excelData.setCategory(currentCell.getStringCellValue());
						break;

					case 3:
						excelData.setRate(currentCell.getNumericCellValue());
						break;
						
					case 4:
						excelData.setQuantity(currentCell.getNumericCellValue());
						break;
						
					case 5:
						excelData.setDescription(currentCell.getStringCellValue());
						break;
						
					case 6:
						excelData.setNotes(currentCell.getStringCellValue());
						break;

					default:
						break;
					}
					cellIndex++;
				}
				tutorials.add(excelData);
			}
			workbook.close();
			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
