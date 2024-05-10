package com.seoultech.synergybe.domain.email;

import com.seoultech.synergybe.system.utils.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    private int generateSixNumber() {
        // generate random number

        return 123456;
    }

    public void validateEmail(String email) {
        Integer authNumber = generateSixNumber();
        String from = "jonghuncu@gmail.com";
        String to = email;
        String title = "[Synergy] 인증 이메일입니다.";
        String htmlBody = "<div style='font-family: Arial, sans-serif;'>" +
                "<p>안녕하세요, " + email + " 고객님</p>" +
                "<br><br>" +
                "<p>[Synergy] 를 방문해주셔서 감사합니다.</p>" +
                "<br>" +
                "<p>아래 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해주세요.</p>" +
                "<br>" +
                "<p>개인정보 보호를 위해 인증번호는 5분 간 유효합니다.</p>" +
                "<br><br>" +
                "<p style='font-size: 20px; font-weight: bold; color: #0077ff;'>" + authNumber + "</p>" +
                "<br><br>" +
                "<p>인증번호를 입력해주시면 회원가입이 완료됩니다.</p>" +
                "</div>";
        sendMail(from, to, title, htmlBody, authNumber);
    }

    private void sendMail(String from, String to, String title, String content, Integer authNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(from);//이메일의 발신자 주소 설정
            helper.setTo(to);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            javaMailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        log.info("before setDataExpire");
        redisUtil.setDataExpire(String.valueOf(authNumber), to, 60*5L);
    }

    public boolean checkAuthNumber(String email, String authNumber) {
        if(redisUtil.getData(authNumber)==null){
            return false;
        }
        else if(redisUtil.getData(authNumber).equals(email)){
            return true;
        }
        else{
            return false;
        }
    }
}
