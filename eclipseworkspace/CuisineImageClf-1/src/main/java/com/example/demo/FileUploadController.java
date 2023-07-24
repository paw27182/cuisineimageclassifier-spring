package com.example.demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FileUploadController {
    
    @GetMapping("/")
    public ModelAndView index(ModelAndView mav) {
		mav.setViewName("topview");  // topview.html
    	return mav;
    }
    
    @PostMapping(value="/upload")
    public ModelAndView upload(HttpServletResponse response,
    		@RequestParam MultipartFile data_file,
    		ModelAndView mav) throws IOException {
    	
    	System.out.println("[FileUploadController] /upload is called.");
		
        // set parameters(json) unused
        String params = "{\"command\":\"predict\", \"category\":\"ComputerVision\", \"algorithm\":\"ImageClassifier\"}";
        System.out.println("[FileUploadController] params= " + params);

        if (data_file.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // HTTP 400 upload.js .ajax error code
            mav.setViewName("topview");  // topview.html
            return mav;
        }

        String file_name = data_file.getOriginalFilename();
        System.out.println("[FileUploadController] file_name= " + file_name);

        // image to byte
        BufferedInputStream in = new BufferedInputStream(data_file.getInputStream());
        int numByte = in.available();
        byte[] byteImageSrc = new byte[numByte];
        in.read(byteImageSrc);
        System.out.println("[FileUploadController] byteImageSrc " + byteImageSrc);
        
        // create multipart/form-data(params + byteImageSrc)
        HttpSendData hsbd = new HttpSendData();
        byte[] data = hsbd.makeMultipartPostData(params, "data_file", file_name, "image/jpg", byteImageSrc);
        
        // REQUEST
        String url = "http://localhost:8000/appmain";
        System.out.println("[FileUploadController] url= " + url);

        PostResultPayload prp = hsbd.post(url, data);  // post result payload

        // RESPONSE
        // retrieve headers data
        String headersData = prp.getHeadersData();
        System.out.println("[FileUploadController] headersData= " + headersData);

        Pattern pattern = Pattern.compile("\"answer\":.*\".*\"");
        Matcher matcher = pattern.matcher(headersData);
        matcher.find();
        String answer = matcher.group();  // e.g.) "answer": "salad"
        mav.addObject("answer", answer);
        
        // retrieve value data
        String valueData = prp.getValueData();
        System.out.println("[FileUploadController] valueData= " + valueData);
        
        // retrieve image data
        byte[] byteImageDst = prp.getImageData();
        System.out.println("[FileUploadController] byteImageDst= " + byteImageDst);
        
        // add destination image
        String base64ImageDst = Base64.getEncoder().encodeToString(byteImageDst);  // byte to BASE64
        mav.addObject("img_dst", "data:image/jpg;base64," + base64ImageDst);

        // add source image
        String base64ImageSrc = Base64.getEncoder().encodeToString(byteImageSrc);  // byte to BASE64
        mav.addObject("img_src", "data:image/jpg;base64," + base64ImageSrc);

        System.out.println("[FileUploadController]　Done!");
        mav.setViewName("topview");  // topview.html
    	return mav;
    }
}
