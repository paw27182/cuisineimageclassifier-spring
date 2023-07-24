package com.example.demo;

public class PostResultPayload {
    protected String headersData;  // data on HTTP headers
    protected String valueData;  // value data(JSON format)
    protected byte[] imageData;  // image data(byte[] array)

    // setter
    public void setHeadersData(String headersData) {
        this.headersData = headersData;
    }

    public void setValueData(String valueData) {
        this.valueData = valueData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    // getter
    public String getHeadersData() {
        return this.headersData;
    }

    public String getValueData() {
        return this.valueData;
    }

    public byte[] getImageData() {
        return this.imageData;
    }
}
