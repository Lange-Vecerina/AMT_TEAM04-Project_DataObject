package org.heig.team04.dataobject.dto;

/**
 * SourceDTO is a DTO for the POST/PUT request to specify the source of the object.
 *
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class SourceDTO {
    private String url = "";
    private byte[] content = new byte[0];

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
