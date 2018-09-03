package com.mackleaps.formium.model.util;

import org.springframework.validation.ObjectError;

import java.util.List;

public class Message {

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";
    public static final String INFO = "info";

    private String header;
    private String body;
    private String classifier;

    public Message(List<ObjectError> errors){

        this.header = "Opa,";
        this.classifier = ERROR;
        this.body = "";

        for(ObjectError error : errors) {

            String message = error.getDefaultMessage();

            boolean endsInPeriod = message.endsWith(".");
            body += message;  //message
            if(!endsInPeriod) //period at the end of each sentence
                body += ".";
            body += " ";      //space between sentences
        }
    }

    public Message (String header, String body, String classifier) {
        this.header = header;
        this.body = body;
        this.classifier = classifier;
    }

    public Message() {
    }

    /**
     * A value will always be inject into a controller if it asks for it
     * Even if it means that the model does not exist
     * In which case <code>header</code>, <code>body</code> and <code>classifier</code> would be null
     * So this method checks if those properties are null, in order to have a better way to compare it before adding
     * and sending it back to the view
     *
     *
     * @return whether the values on this class should be considered before sending back to template
     * */
    public boolean isEmpty(){
        return this.header == null && this.body == null && this.classifier == null;
    }

    public String getHeader() {
        return this.header;
    }

    public String getBody() {
        return this.body;
    }

    public String getClassifier() {
        return this.classifier;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String toString() {
        return "Message(header=" + this.getHeader() + ", body=" + this.getBody() + ", classifier=" + this.getClassifier() + ")";
    }
}
