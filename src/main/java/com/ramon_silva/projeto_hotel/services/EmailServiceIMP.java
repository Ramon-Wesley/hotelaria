package com.ramon_silva.projeto_hotel.services;


import java.io.ByteArrayOutputStream;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.ramon_silva.projeto_hotel.dto.EmailDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEmailEnum;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.repositories.EmailRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IEmailService;
import com.ramon_silva.projeto_hotel.util.MailConstants;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceIMP  implements IEmailService{


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailRepository emailRepository;
    private final ModelMapper modelMapper;

    private EmailServiceIMP(JavaMailSender javaMailSender,
    TemplateEngine templateEngine,EmailRepository emailRepository,ModelMapper modelMapper){
        this.javaMailSender=javaMailSender;
        this.templateEngine=templateEngine;
        this.emailRepository=emailRepository;
        this.modelMapper=modelMapper;
    }
    
    @Override
    public void sendEmail(EmailModel emailModel,Object object,String typeEmail){
     try {
         EmailDto emailDto=modelMapper.map(emailModel,EmailDto.class);

         MimeMessage mimeMessage=javaMailSender.createMimeMessage();
         MimeMessageHelper helper=new MimeMessageHelper(mimeMessage, true, "utf-8");

         Context context=new Context();
         context.setVariable("name_email", emailDto.getOwerRef());
         context.setVariable("text", emailDto.getText());

         if(typeEmail != MailConstants.CANCEL){
             String emailBody= this.generatedTemplate(context, typeEmail,object);
             byte[] pdf=generatedPDF(emailBody);
             helper.addAttachment("document_pdf",new ByteArrayResource(pdf));
            }

         String email=templateEngine.process("EmailTemplate",context);
         helper.setTo(emailDto.getEmailTo());
         helper.setFrom(emailDto.getEmailFrom());
         helper.setSubject(emailDto.getSubject());
         helper.setText(email,true);
         javaMailSender.send(mimeMessage);
         emailModel.setStatusEmail(StatusEmailEnum.SEND);
         emailRepository.save(emailModel);
     } catch (Exception e) {
        throw new GeralException("Erro ao enviar email"+e.getMessage());
     }

        } 
        
private String generatedTemplate(Context context,String type,Object object){
    switch (type) {
        case MailConstants.RESERVATION:
        ReservationDto reservationDto=(ReservationDto)object;
        context.setVariable("reservation", reservationDto);
        return templateEngine.process("ReservationTemplate",context);    
            
        default:
        case MailConstants.PAYMENT:
        PaymentDto paymentDto=(PaymentDto)object;
        context.setVariable("payment", paymentDto);
        
        return templateEngine.process("PaymentTemplate",context);
    }
}

private byte[] generatedPDF(String bodyPdf){
    try {
        ITextRenderer renderer= new ITextRenderer();
        renderer.setDocumentFromString(bodyPdf);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    } catch (DocumentException e) {
        throw new GeralException("Erro ao gerar pdf"+e.getMessage());  
    }
}
    }
    

