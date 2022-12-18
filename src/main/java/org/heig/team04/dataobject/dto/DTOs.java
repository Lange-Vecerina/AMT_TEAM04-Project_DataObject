package org.heig.team04.dataobject.dto;

public class DTOs {
    public static class ObjectDTO {
        private String uri;

        public ObjectDTO(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class ObjectWithContentDTO {
        private String uri;
        private byte[] content;

        public ObjectWithContentDTO(String uri, byte[] content) {
            this.uri = uri;
            this.content = content;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }

    public static class ObjectWithSourceDTO {
        private String uri;
        private String source;

        public ObjectWithSourceDTO(String uri, String source) {
            this.uri = uri;
            this.source = source;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

}
