package com.example.demo_chatbot.service.question;

import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.dto.document.QuestionRequestDTO;
import com.example.demo_chatbot.entity.ClientEntity;
import com.example.demo_chatbot.entity.InforQuestionEntity;
import com.example.demo_chatbot.mapper.QuestionMapper;
import com.example.demo_chatbot.repository.ClientRepository;
import com.example.demo_chatbot.repository.QuestionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class InforQuestionService implements IQuestionService{

    QuestionRepository questionRepository;
    ClientRepository clientRepository;
    private static CellStyle cellStyleFormatNumber = null;

    @Override
    public QuestionDTO addQuestion(QuestionRequestDTO dto) {
        Optional<ClientEntity> client = clientRepository.findById(dto.getClient_id());

        InforQuestionEntity entity = InforQuestionEntity.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .client(client.get())
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .time(LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond())
                .build();


        return QuestionMapper.INSTANCE.toDTO(questionRepository.save(entity));
    }

    @Override
    public List<QuestionDTO> getQuestionByIdClient(String id) {

        List<InforQuestionEntity> listQuestion = questionRepository.getQuestionByClientId(id);

        List<QuestionDTO> listDTO = new ArrayList<>();

        for (InforQuestionEntity entity : listQuestion){

            listDTO.add(QuestionDTO.builder()
                            .id(entity.getId())
                            .question(entity.getQuestion())
                            .answer(entity.getAnswer())
                            .date(entity.getDate())
                            .time(entity.getTime())
                    .build());
        }

        return listDTO;
    }


    public void writeExcel(List<QuestionDTO> question , String excelPath) throws IOException {

        Workbook workbook = getWorkbook();
        // Create sheet
        Sheet sheet = workbook.createSheet("Danh sách câu hỏi"); // Create sheet with sheet name

        int rowIndex = 0;
        // Write header
        writeHeader(sheet, rowIndex);


        // Write data
        rowIndex++;
        for (QuestionDTO dto : question) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            // Write data on row
            writeQuestion(dto, row);
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        // Create file excel
        createOutputFile(workbook, excelPath);
    }

    // Create workbook
    private static Workbook getWorkbook() throws IOException {

        return new XSSFWorkbook();
    }

    // Write header with format
    private static void writeHeader(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        // Create row
        Row row = sheet.createRow(rowIndex);

        // Create cells
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Id");

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Câu hỏi");

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("chatbot trả lời");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ngày câu hỏi được hỏi");

    }

    // Create CellStyle for header
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    private static void writeQuestion(QuestionDTO question, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }

        Cell cell = row.createCell(0);
        cell.setCellValue(question.getId());

        cell = row.createCell(1);
        cell.setCellValue(question.getQuestion());

        cell = row.createCell(2);

        cell.setCellValue(question.getAnswer());
        cell.setCellStyle(cellStyleFormatNumber);

        cell = row.createCell(3);
        cell.setCellValue(question.getDate());


    }

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    // Create output file
    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }

    @Override
    public void exportExcel() throws IOException {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String time = "_" +LocalDateTime.now().getSecond();

        String directoryPath = "D:\\excel\\";
        String fileName = "listQuestion_"+date +time+".xlsx";

        String excelFilePath = directoryPath + fileName;

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục và thư mục cha nếu cần
        }
// Tạo file
        File file = new File(excelFilePath);
        if (file.createNewFile()) {
           log.warn("File được tạo thành công: " + excelFilePath);
        } else {
            log.warn("File đã tồn tại: " + excelFilePath);
        }


        List<InforQuestionEntity> entity = questionRepository.findAll();

        List<QuestionDTO> dto = new ArrayList<>();

        for(InforQuestionEntity entity1 : entity){
            dto.add(QuestionMapper.INSTANCE.toDTO(entity1));
        }

        writeExcel(dto,excelFilePath);



    }

    @Override
    public List<QuestionDTO> getQuestionUserbyId(String userId) {

        List<InforQuestionEntity> chatHistory = questionRepository.getQuestionByClientId(userId);
        List<QuestionDTO> chat = new ArrayList<>();

        for (InforQuestionEntity entity : chatHistory){

           chat.add(QuestionMapper.INSTANCE.toDTO(entity));
        }

        return chat;
    }
}
