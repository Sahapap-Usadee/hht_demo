package com.jastec.hht_demo.model;

public class TerTest {

    private String TestId ;
    private String TestPassword ;
    private String Salt ;

    public TerTest() {
    }

    public TerTest(String testId, String testPassword, String salt) {
        TestId = testId;
        TestPassword = testPassword;
        Salt = salt;
    }

    public String getTestId() {
        return TestId;
    }

    public void setTestId(String testId) {
        TestId = testId;
    }

    public String getTestPassword() {
        return TestPassword;
    }

    public void setTestPassword(String testPassword) {
        TestPassword = testPassword;
    }

    public String getSalt() {
        return Salt;
    }

    public void setSalt(String salt) {
        Salt = salt;
    }
}

