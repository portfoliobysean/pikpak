package kr.co.pikpak.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/image")
public class ImageUploadController {

    @PostMapping("/upload")
    @ResponseBody
    public HashMap<String, Object> uploadImage(@RequestParam("upload") MultipartFile uploadFile) {

        HashMap<String, Object> returnMap = new HashMap<>();

        // 파일이 비어있는지 확인
        if (uploadFile.isEmpty()) {
            returnMap.put("uploaded", false);
            returnMap.put("error", "파일이 비어 있습니다.");
            return returnMap;
        }

        // FTP 설정
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8"); // 한글 파일명 깨짐 방지
        FTPClientConfig ftpConfig = new FTPClientConfig();

        try {
            // FTP 서버 접속 정보
            String ftpHost = "172.30.1.68";
            String ftpUser = "erp_master";
            String ftpPass = "erp123123";
            int ftpPort = 10021;

            // 업로드된 파일의 원래 이름과 확장자를 사용하여 UUID 생성
            String originalFileName = uploadFile.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
            String savedFileName = UUID.randomUUID().toString() + fileExtension; // UUID로 고유 파일명 생성

            // FTP 연결 및 로그인
            ftpClient.configure(ftpConfig);
            ftpClient.connect(ftpHost, ftpPort);

            int replyCode = ftpClient.getReplyCode();

            boolean login = ftpClient.login(ftpUser, ftpPass);
            if (!login) {
                returnMap.put("uploaded", false);
                returnMap.put("error", "FTP 로그인 실패.");
                return returnMap;
            }

            // 전송 모드 설정 (액티브 모드)
            ftpClient.enterLocalActiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 파일 입력 스트림 확인
            InputStream inputStream = uploadFile.getInputStream();
            if (inputStream.available() <= 0) {
                returnMap.put("uploaded", false);
                returnMap.put("error", "파일 스트림을 열 수 없습니다.");
                return returnMap;
            }

            // 파일 업로드
            boolean uploadSuccess = ftpClient.storeFile("/public_html/uploads/" + savedFileName, inputStream);

            if (uploadSuccess) {
                String fileUrl = "http://" + ftpHost + "/uploads/" + savedFileName;
                returnMap.put("uploaded", true);
                returnMap.put("url", fileUrl);
            } else {
                int replyCode1 = ftpClient.getReplyCode();
                returnMap.put("uploaded", false);
                returnMap.put("error", "파일 업로드 실패. 응답 코드: " + replyCode1);
            }

        } catch (IOException e) {
            returnMap.put("uploaded", false);
            returnMap.put("error", "파일 업로드 중 오류 발생: " + e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnMap;
    }
}
