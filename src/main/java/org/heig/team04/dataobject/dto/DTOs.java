package org.heig.team04.dataobject.dto;

public class DTOs {
    DTOs() {
    }

    public static class UriDTO {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class UriWithContentDTO extends UriDTO {
        private byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }

    public static class UriWithSourceDTO extends UriDTO {
        private String source;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    public static class ContentDTO {
        private byte[] content;

        public ContentDTO(byte[] content) {
            this.content = content;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }

    public static class LinkDTO {
        private String link;

        public LinkDTO(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
