package com.codercampus.api.exception;

public class CustomException extends Exception{

//    private Long id;
//    private String name;
//    private String source;
//    private Object data;


    public CustomException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

//    public Long getId() {
//        return this.id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }
}
