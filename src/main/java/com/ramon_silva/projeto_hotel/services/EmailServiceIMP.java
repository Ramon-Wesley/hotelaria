package com.ramon_silva.projeto_hotel.services;


import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.ramon_silva.projeto_hotel.dto.EmailDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEmailEnum;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.repositories.EmailRepository;
import com.ramon_silva.projeto_hotel.util.MessageMailConstants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceIMP  implements EmailService{


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailRepository emailRepository;
    public EmailServiceIMP(JavaMailSender javaMailSender,
    TemplateEngine templateEngine,EmailRepository emailRepository){
        this.javaMailSender=javaMailSender;
        this.templateEngine=templateEngine;
        this.emailRepository=emailRepository;
    }
    
    @Override
    public void sendEmail(EmailModel emailModel,Object object,String typeEmail) throws MessagingException, DocumentException {
    
            EmailDto emailDto=new EmailDto(emailModel);
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage, true, "utf-8");
            Context context=new Context();
            context.setVariable("name_email", emailDto.owerRef());
            context.setVariable("text", emailDto.text());
            String emailBody= this.generatedTemplate(context, typeEmail,object);
            byte[] pdf=generatedPDF(emailBody);
            String email=templateEngine.process("EmailTemplate",context);
            helper.setTo(emailDto.emailTo());
            helper.setFrom(emailDto.emailFrom());
            helper.setSubject(emailDto.subject());
            helper.setText(email,true);
            helper.addAttachment("document_pdf",new ByteArrayResource(pdf));
            javaMailSender.send(mimeMessage);
            emailModel.setStatusEmail(StatusEmailEnum.SEND);
            emailRepository.save(emailModel);
        } 
        
public String generatedTemplate(Context context,String type,Object object){
        ReservationDto reservationDto=(ReservationDto)object;
        context.setVariable("name", reservationDto.client().name());
        context.setVariable("hotel_name", reservationDto.room().hotel().name());
        context.setVariable("room_type", reservationDto.room().type_room());
        context.setVariable("reservation_value", reservationDto.total_pay());
        
        return templateEngine.process("ReservationTemplate",context);
}

public byte[] generatedPDF(String bodyPdf) throws DocumentException{
    Document document=new Document();
    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
    PdfWriter.getInstance(document,outputStream);
    document.open();
    document.add(new Paragraph(bodyPdf));
    document.getHtmlStyleClass();
    document.close();

    return outputStream.toByteArray();
}
    }
    

