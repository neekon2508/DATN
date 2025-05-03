// package admin.web;

// import java.io.File;
// import java.io.IOException;

// import org.springframework.core.convert.converter.Converter;
// import org.springframework.stereotype.Component;
// import org.springframework.web.multipart.MultipartFile;


// public class CardSetConverter implements Converter<MultipartFile, String>{

//     @Override
//     public String convert(MultipartFile source) {
//         if (source == null || source.isEmpty()) {
//             return null; // Xử lý trường hợp file trống
//         }
//         try {
//             // Đường dẫn lưu file
//             String uploadDir = "src/main/resources/static/images/";
//             File directory = new File(uploadDir);
//             if (!directory.exists()) {
//                 directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
//             }

//             // Lưu file và trả về đường dẫn
//             String filePath = uploadDir + source.getOriginalFilename();
//             source.transferTo(new File(filePath));
//             return filePath;
//         } catch (IOException e) {
//             throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage(), e);
//         }
//     }
//     }
