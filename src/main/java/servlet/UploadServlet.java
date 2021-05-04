package servlet;

import dto.CommentDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@WebServlet(name = "UploadServlet", value = "/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class UploadServlet extends HttpServlet {

    private String path = "C:/Users/Per/Documents/sem4/Security/images/";

    protected String filename;
    private String extension;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Random rand = new Random();
        int randNo = rand.nextInt(10000);

        Part filePart = request.getPart("file");
        String originalFileName = filePart.getSubmittedFileName();
        this.extension = getFileExtension(originalFileName);

        try {
            this.filename = makeMD5(String.valueOf(randNo));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        for (Part part : request.getParts()) {
            part.write(path + this.filename + this.extension);
        }

        /*
        *
        *
        * TAG FAT I COMMENT OG PERSIST
        *
        *
        */

        request.getRequestDispatcher("/").forward(request, response);
    }

    public String getFileExtension(String filename) {
        String name = filename;
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public String makeMD5(String filename) throws NoSuchAlgorithmException {

        //Static getInstance method is called with MD5 Hashing
        MessageDigest md = MessageDigest.getInstance("MD5");

        //Digest() method is called to calculate message digest of an input digest - returns array of bytes
        byte[] messageDigest = md.digest(filename.getBytes());

        //Convert our bytearray into a signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        //Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        System.out.println(hashtext);
        return hashtext;
    }

    public String persistComment(CommentDTO comment) throws NoSuchAlgorithmException {
        String fileExtension = getFileExtension(comment.getImageID());
        String filename = makeMD5(comment.getImageID());

        return "daw";
    }

}
